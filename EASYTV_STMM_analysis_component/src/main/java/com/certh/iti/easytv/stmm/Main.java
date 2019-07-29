package com.certh.iti.easytv.stmm;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import com.certh.iti.easytv.stmm.clustering.Config;
import com.certh.iti.easytv.stmm.clustering.iCluster;
import com.certh.iti.easytv.stmm.preferences.Abstracts;
import com.certh.iti.easytv.stmm.similarity.DistanceMeasureFactory;
import com.certh.iti.easytv.stmm.similarity.dimension.AsymmetricBinary;
import com.certh.iti.easytv.stmm.similarity.dimension.Nominal;
import com.certh.iti.easytv.stmm.similarity.dimension.Numeric;
import com.certh.iti.easytv.stmm.similarity.dimension.Ordinal;
import com.certh.iti.easytv.stmm.similarity.dimension.SymmetricBinary;
import com.certh.iti.easytv.user.UserProfile;
import com.certh.iti.easytv.user.preference.Preference;
import com.certh.iti.easytv.user.preference.attributes.AsymmetricBinaryAttribute;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.ColorAttribute;
import com.certh.iti.easytv.user.preference.attributes.DoubleAttribute;
import com.certh.iti.easytv.user.preference.attributes.IntegerAttribute;
import com.certh.iti.easytv.user.preference.attributes.NominalAttribute;
import com.certh.iti.easytv.user.preference.attributes.NumericAttribute;
import com.certh.iti.easytv.user.preference.attributes.OrdinalAttribute;
import com.certh.iti.easytv.user.preference.attributes.SymmetricBinaryAttribute;

public class Main {

	// Arguments
	private static final String _ArgConfigFile = "-c";
	private static final String _ArgProfilesDirectory = "-p";
	private static final String _ArgOutputDirectory = "-o";
	
	// Profiles
	private static File _ConfigFile = null;
	private static File _OutputDirectory = null;
	private static File _ProfilesDirectory = null;
	private static Cluster<UserProfile> _Profiles = new Cluster<UserProfile>();

	public static void main(String[] args) throws NumberFormatException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, IOException {
		//Parse arguments
		int argn = args.length;
		for(int i = 0; i < argn; i+=2 ) {
			String arg = args[i].trim();
			String value = args[i+1];
			if(arg.equals(_ArgProfilesDirectory)) 
				_ProfilesDirectory = new File(value.trim());
			else if (arg.equals(_ArgConfigFile)) 
				_ConfigFile = new File(value.trim());
			else if (arg.equals(_ArgOutputDirectory)) 
				_OutputDirectory = new File(value.trim());
		}
		
		if(_ProfilesDirectory == null || !_ProfilesDirectory.exists() ) {
			String pwd = System.getProperty("user.dir");
            _ProfilesDirectory = new File(pwd + File.separator + "auto_generated_testing_profiles");
            System.out.println("Could not find profiles directory, reverted to :'" + _ProfilesDirectory.getAbsolutePath() + "'");
		}
		
		if(_ConfigFile == null || !_ConfigFile.exists() ) {
			String pwd = System.getProperty("user.dir");
			_ConfigFile = new File(pwd + File.separator + "config.ini");
            System.out.println("Could not find profiles directory, reverted to :'" + _ConfigFile.getAbsolutePath() + "'");
		}
		
		if(_OutputDirectory == null  ) {
			String pwd = System.getProperty("user.dir");
			_OutputDirectory = new File(_ProfilesDirectory.getAbsolutePath() + File.separator + "StatisticalMatchMakerData.js");
            System.out.println("Could not find profiles directory, reverted to :'" + _OutputDirectory.getAbsolutePath() + "'");
		}
		
		//Check if everything is there
		if(!_ProfilesDirectory.exists()) {
			System.err.println("CRITICAL: Profiles directory ('" + _ProfilesDirectory.getAbsolutePath() + "') does not exist.");
			System.exit(-1);
		}
		
		if(!_ConfigFile.exists()) {
			System.err.println("CRITICAL: Profiles directory ('" + _ConfigFile.getAbsolutePath() + "') does not exist.");
			System.exit(-1);
		}

	
		//Read config
		Config.getInstance().ReadConfiguration(_ConfigFile);
		File generatedDirectory = new File(_ProfilesDirectory.getAbsolutePath()+ File.separator +"generated");
		if(!generatedDirectory.exists())
			generatedDirectory.mkdirs();
		
		PreprocessFrom(_ProfilesDirectory, generatedDirectory);
		
		//Read profiles
		if(_ConfigFile == null || !_ConfigFile.exists())
			throw new IllegalStateException("Config File not set.");
		if(_ProfilesDirectory != null && _ProfilesDirectory.exists())
			ReadProfilesFrom(_ProfilesDirectory);
		else
			throw new IllegalStateException("Preference Directory not set."); 
		
        System.out.println("--------");
        System.out.println("Finished loading " + _Profiles.getPoints().size() + " profiles.");
        System.out.println("--------");
        System.out.println("Clustering...");
        
        //clusters
        List<Cluster<UserProfile>> clusteres = new ArrayList<Cluster<UserProfile>>();
        
        //start with all profile as first cluster
        clusteres.add(_Profiles);

        //run clustering algorithm
    	List<Cluster<UserProfile>> tmp = new ArrayList<Cluster<UserProfile>>();
        for(iCluster clusterer : Config.getInstance().Config) {

        	System.out.println("["+clusterer.get_Name()+"] "+ clusterer.toString());
        	
        	//cluster each cluster
        	for(Cluster<UserProfile> cluster : clusteres) 
        		tmp.addAll(clusterer.getClusterer().cluster(cluster.getPoints()));
        			
            System.out.println("\tClusters generated... " + tmp.size());
            for(int i = 0; i < tmp.size(); i++)
            	System.out.println("\tcluster_" + (i + 1) + " : "+ tmp.get(i).getPoints().size());

        	clusteres.clear();
        	clusteres.addAll(tmp);
        	tmp.clear();
        }
        
        //Start processing
        List<UserProfile> generalized = new ArrayList<UserProfile>();
        
        //TO-DO replace all dimensions distance measurement with a proper distance for finding the cluster center
        DistanceMeasure allDimensionsDistance = DistanceMeasureFactory.getInstance(new String[] {"ALL"});
        for(Cluster<UserProfile> aCluster : clusteres) {
        	UserProfile clusterCenter = new UserProfile();
        	TreeMap<Double, HashSet<UserProfile>> distances = new TreeMap<Double, HashSet<UserProfile>>();
        	
        	//Find the cluster center
        	Abstracts.FindCenter(allDimensionsDistance, aCluster, clusterCenter, distances);
        	
        	//TO-DO generilize cluster
        	//Generalized.Add(Preferences.GeneralizeProfile(center, distances, _Profiles))
        	generalized.add(clusterCenter);
        }
        
        System.out.println("--------");
		System.out.println("write JS file to "+_OutputDirectory.getAbsolutePath());

        //Write JS
        WriteJavaScript(generalized);
	}

	/**
	 * @brief Process files from the given directory
	 * 
	 * @param directory
	 * @param generatedDirectory
	 * @throws IOException
	 */
	private static void PreprocessFrom(File directory, File generatedDirectory) throws IOException {
		if (directory == null || !directory.exists())
			return;

		class dirFileFilter implements FileFilter {
			public boolean accept(File dir) {
				return dir.isDirectory();
			}
		}

		File[] directories = directory.listFiles(new dirFileFilter());
		for (int i = 0; i < directories.length; i++)
			PreprocessFrom(directories[i], generatedDirectory);
	}

	/**
	 * @brief Recursively process ".ini" files contained in the given directory and all its directories.
	 * 
	 * @param directory
	 * @throws IOException
	 */
	private static void ReadProfilesFrom(File directory) throws IOException {
		if (directory == null || !directory.exists())
			return;

		class IniFileFilter implements FilenameFilter {
			public boolean accept(File dir, String name) {
				return name.endsWith(".json");
			}
		}

		File[] iniFiles = directory.listFiles(new IniFileFilter());
		for (int i = 0; i < iniFiles.length; i++) {
			System.out.println("Reading file: " +iniFiles[i].getPath());
			_Profiles.addPoint(new UserProfile(iniFiles[i]));
		}

		class dirFileFilter implements FileFilter {
			public boolean accept(File dir) {
				return dir.isDirectory();
			}
		}

		File[] directories = directory.listFiles(new dirFileFilter());
		for (int i = 0; i < directories.length; i++)
			ReadProfilesFrom(directories[i]);
	}
	
	/**
	 * Write a javascript file that represent all the given clusters, the file is used by 
	 * the runtime component to answer matching requests.
	 * @param clusters
	 * @throws IOException
	 */
	private static void WriteJavaScript(List<UserProfile> clusters) throws IOException {
		WriteSTMMJavaScript(clusters);
		WriteHBMMJavaScript(clusters);
	}

	/**
	 * Write a javascript file that represent all the given clusters, the file is used by 
	 * the runtime component to answer matching requests.
	 * @param clusters
	 * @throws IOException
	 */
	private static void WriteSTMMJavaScript(List<UserProfile> clusters) throws IOException {
		
		File _stmmOutputFile = new File(_OutputDirectory.getAbsolutePath()+ File.separatorChar + "StatisticalMatchMakerData.js");
		if(!_stmmOutputFile.exists())
			_stmmOutputFile.createNewFile();
				
		PrintWriter writer = new PrintWriter(_stmmOutputFile);
		Date now = new Date();
		DateFormat df = new SimpleDateFormat("dd MMM HH:mm:ss");

        writer.println("/*!");
        writer.println();
        writer.println("EasyTV Statistical Matchmaker ");
        writer.println();
        writer.println("Copyright 2017-2020 Center for Research & Technology - HELLAS");
        writer.println();
        writer.println("Licensed under the New BSD License. You may not use this file except in");
        writer.println("compliance with this licence.");
        writer.println();
        writer.println("You may obtain a copy of the licence at");
        writer.println("https://github.com/REMEXLabs/GPII-Statistical-Matchmaker/blob/master/LICENSE.txt");
        writer.println();
        writer.println("The research leading to these results has received funding from");
        writer.println("the European Union's H2020-ICT-2016-2, ICT-19-2017 Media and content convergence");
        writer.println("under grant agreement no. 761999.");
        writer.println("*/");
        writer.println();
        writer.println("//Generated " +  df.format(now));
        writer.println("var fluid = fluid || require(\"gpii-universal\");");
        writer.println("var stat = fluid.registerNamespace(\"easytv.matchMaker.statistical\");");

        //entry count
        writer.println("stat.entryCount = " + clusters.size() + ";");
        
        //import classes
        writer.println();
        writer.println("var Numeric = require(\"./DimensionHandlers\").Numeric");
        writer.println("var IntegerNumeric = require(\"./DimensionHandlers\").IntegerNumeric");
        writer.println("var Nominal = require(\"./DimensionHandlers\").Nominal");
        writer.println("var Ordinal = require(\"./DimensionHandlers\").Ordinal");
        writer.println("var SymmetricBinary = require(\"./DimensionHandlers\").SymmetricBinary");
        writer.println("var Color = require(\"./DimensionHandlers\").Color");
        writer.println();

        
        writer.println("stat.dimensionsHandlers = new Map();");

        //add preference handlers
        for(Entry<String, Attribute> entry : Preference.preferencesAttributes.entrySet()) {
        	Attribute operand =  entry.getValue();
        	String handlerInstance = "";
        	
        	if(ColorAttribute.class.isInstance(operand)) {
				ColorAttribute colorAttribute = (ColorAttribute) operand;
				
				for (NumericAttribute attribte : colorAttribute.getDimensions()) 
					handlerInstance += "new IntegerNumeric("+String.valueOf(attribte.getMaxValue())+", "+ String.valueOf(attribte.getMinValue())+", "+String.valueOf(attribte.getOperandMissingValue())+" ),";
				
				handlerInstance =  "new Color("+handlerInstance.substring(0, handlerInstance.length() - 1 ) +")";
				
			}
        	else if (IntegerAttribute.class.isInstance(operand)) {
        		IntegerAttribute intNumeric = (IntegerAttribute) operand;
 				
 				handlerInstance = "new IntegerNumeric("+String.valueOf(intNumeric.getMaxValue())+", "+ String.valueOf(intNumeric.getMinValue())+", "+String.valueOf(intNumeric.getOperandMissingValue())+" )";
 				
 			}
        	else if (DoubleAttribute.class.isInstance(operand)) {
        		DoubleAttribute doubleNumeric = (DoubleAttribute) operand;
				
				handlerInstance = "new Numeric("+String.valueOf(doubleNumeric.getMaxValue())+", "+ String.valueOf(doubleNumeric.getMinValue())+", "+String.valueOf(doubleNumeric.getOperandMissingValue())+" )";
				
			} 
        	else if (OrdinalAttribute.class.isInstance(operand)) {
				OrdinalAttribute ordinal = (OrdinalAttribute) operand;
				
				String states = "";
				for(String state : ordinal.getStates())
					states += "\""+state+"\",";
				
				handlerInstance = "new Ordinal(["+states.substring(0, states.length() - 1).toLowerCase()+"], "+ String.valueOf(ordinal.getMaxValue())+", "+ String.valueOf(ordinal.getMinValue())+", "+String.valueOf(ordinal.getOperandMissingValue())+" )";
				
			} 
        	else if (NominalAttribute.class.isInstance(operand)) {
				NominalAttribute nominal = (NominalAttribute) operand;
				
				
				String states = "";
				for(String state : nominal.getStates())
					states += "\""+state+"\",";
				
				handlerInstance = "new Nominal(["+states.substring(0, states.length() - 1).toLowerCase()+"], "+String.valueOf(operand.getOperandMissingValue())+")";
				
			} 
        	else if (SymmetricBinaryAttribute.class.isInstance(operand)) {
				
				handlerInstance = "new SymmetricBinary("+String.valueOf(operand.getOperandMissingValue())+")";

			} 
        	else if (AsymmetricBinaryAttribute.class.isInstance(operand)) {
				//handlerInstance = new AsymmetricBinary();
			}
        	
        	 writer.println("stat.dimensionsHandlers.set(\"" +entry.getKey() + "\", "+ handlerInstance +");");
        }
        
        writer.println();

        //clusters
        writer.println("stat.clusters = [");
        
        for(int i = 0 ; i < clusters.size() - 1; i++) {
        	writer.println(clusters.get(i).getJSONObject().toString(4)+",");
        }
        
        if(clusters.size() > 0) {
        	writer.println(clusters.get(clusters.size() - 1).getJSONObject().toString(4));
        }
        
        writer.println("];");        
        writer.close();
	}
	
	/**
	 * Write a javascript file that has maps preferences URL to proper dimension handler
	 * @param clusters
	 * @throws IOException
	 */
	private static void WriteHBMMJavaScript(List<UserProfile> clusters) throws IOException {
		
		File _hbmmOutputFile = new File(_OutputDirectory.getAbsolutePath()+ File.separatorChar + "HybridMatchMakerData.js");
		if(!_hbmmOutputFile.exists())
			_hbmmOutputFile.createNewFile();
				
		PrintWriter writer = new PrintWriter(_hbmmOutputFile);
		Date now = new Date();
		DateFormat df = new SimpleDateFormat("dd MMM HH:mm:ss");

        writer.println("/*!");
        writer.println();
        writer.println("EasyTV Hybrid Matchmaker ");
        writer.println();
        writer.println("Copyright 2017-2020 Center for Research & Technology - HELLAS");
        writer.println();
        writer.println("Licensed under the New BSD License. You may not use this file except in");
        writer.println("compliance with this licence.");
        writer.println();
        writer.println("You may obtain a copy of the licence at");
        writer.println("https://github.com/REMEXLabs/GPII-Statistical-Matchmaker/blob/master/LICENSE.txt");
        writer.println();
        writer.println("The research leading to these results has received funding from");
        writer.println("the European Union's H2020-ICT-2016-2, ICT-19-2017 Media and content convergence");
        writer.println("under grant agreement no. 761999.");
        writer.println("*/");
        writer.println();
        writer.println("//Generated " +  df.format(now));
        writer.println("var fluid = fluid || require(\"gpii-universal\");");
        writer.println("var hybrid = fluid.registerNamespace(\"easytv.matchMaker.hybrid\");");
        
        //import classes
        writer.println();
        writer.println("var Numeric = require(\"./DimensionHandlers\").Numeric");
        writer.println("var IntegerNumeric = require(\"./DimensionHandlers\").IntegerNumeric");
        writer.println("var Nominal = require(\"./DimensionHandlers\").Nominal");
        writer.println("var Ordinal = require(\"./DimensionHandlers\").Ordinal");
        writer.println("var SymmetricBinary = require(\"./DimensionHandlers\").SymmetricBinary");
        writer.println("var Color = require(\"./DimensionHandlers\").Color");
        writer.println();

        
        writer.println("hybrid.dimensionsHandlers = new Map();");

        //add preference handlers
        for(Entry<String, Attribute> entry : Preference.preferencesAttributes.entrySet()) {
        	Attribute operand =  entry.getValue();
        	String handlerInstance = "";
        	
        	if(ColorAttribute.class.isInstance(operand)) {
				ColorAttribute colorAttribute = (ColorAttribute) operand;
				
				for (NumericAttribute attribte : colorAttribute.getDimensions()) 
					handlerInstance += "new IntegerNumeric("+String.valueOf(attribte.getMaxValue())+", "+ String.valueOf(attribte.getMinValue())+", "+String.valueOf(attribte.getOperandMissingValue())+" ),";
				
				handlerInstance =  "new Color("+handlerInstance.substring(0, handlerInstance.length() - 1 ) +")";
				
			}
        	else if (IntegerAttribute.class.isInstance(operand)) {
        		IntegerAttribute intNumeric = (IntegerAttribute) operand;
 				
 				handlerInstance = "new IntegerNumeric("+String.valueOf(intNumeric.getMaxValue())+", "+ String.valueOf(intNumeric.getMinValue())+", "+String.valueOf(intNumeric.getOperandMissingValue())+" )";
 				
 			}
        	else if (DoubleAttribute.class.isInstance(operand)) {
        		DoubleAttribute doubleNumeric = (DoubleAttribute) operand;
				
				handlerInstance = "new Numeric("+String.valueOf(doubleNumeric.getMaxValue())+", "+ String.valueOf(doubleNumeric.getMinValue())+", "+String.valueOf(doubleNumeric.getOperandMissingValue())+" )";
				
			} 
        	else if (OrdinalAttribute.class.isInstance(operand)) {
				OrdinalAttribute ordinal = (OrdinalAttribute) operand;
				
				String states = "";
				for(String state : ordinal.getStates())
					states += "\""+state+"\",";
				
				handlerInstance = "new Ordinal(["+states.substring(0, states.length() - 1).toLowerCase()+"], "+ String.valueOf(ordinal.getMaxValue())+", "+ String.valueOf(ordinal.getMinValue())+", "+String.valueOf(ordinal.getOperandMissingValue())+" )";
				
			} 
        	else if (NominalAttribute.class.isInstance(operand)) {
				NominalAttribute nominal = (NominalAttribute) operand;
				
				
				String states = "";
				for(String state : nominal.getStates())
					states += "\""+state+"\",";
				
				handlerInstance = "new Nominal(["+states.substring(0, states.length() - 1).toLowerCase()+"], "+String.valueOf(operand.getOperandMissingValue())+")";
				
			} 
        	else if (SymmetricBinaryAttribute.class.isInstance(operand)) {
				
				handlerInstance = "new SymmetricBinary("+String.valueOf(operand.getOperandMissingValue())+")";

			} 
        	else if (AsymmetricBinaryAttribute.class.isInstance(operand)) {
				//handlerInstance = new AsymmetricBinary();
			}
        	
        	 writer.println("hybrid.dimensionsHandlers.set(\"" +entry.getKey() + "\", "+ handlerInstance +");");
        }
        
        writer.println();
        writer.close();
	}

}
