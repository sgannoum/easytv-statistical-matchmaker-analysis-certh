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
import java.util.TreeMap;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import com.certh.iti.easytv.stmm.clustering.Config;
import com.certh.iti.easytv.stmm.clustering.iCluster;
import com.certh.iti.easytv.stmm.preferences.Abstracts;
import com.certh.iti.easytv.stmm.similarity.DistanceMeasureFactory;
import com.certh.iti.easytv.user.UserProfile;

public class Main {

	// Arguments
	private static final String _ArgConfigFile = "-c";
	private static final String _ArgProfilesDirectory = "-p";
	private static final String _ArgOutputFile = "-o";
	
	// Profiles
	private static File _ConfigFile = null;
	private static File _OutputFile = null;
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
			else if (arg.equals(_ArgOutputFile)) 
				_OutputFile = new File(value.trim());
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
		
		if(_OutputFile == null  ) {
			String pwd = System.getProperty("user.dir");
			_OutputFile = new File(_ProfilesDirectory.getAbsolutePath() + File.separator + "StatisticalMatchMakerData.js");
            System.out.println("Could not find profiles directory, reverted to :'" + _OutputFile.getAbsolutePath() + "'");
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
        
        //Cluster
        List<Cluster<UserProfile>> clusteres = new ArrayList<Cluster<UserProfile>>();
        clusteres.add(_Profiles);

        for(iCluster clusterer : Config.getInstance().Config) {

        	System.out.println("["+clusterer.get_Name()+"] "+ clusterer.toString());
        	
        	List<Cluster<UserProfile>> tmp = new ArrayList<Cluster<UserProfile>>();
        	for(Cluster<UserProfile> cluster : clusteres) 
        		tmp.addAll(clusterer.getClusterer().cluster(cluster.getPoints()));
        			
            System.out.println("\tClusters generated... " + tmp.size());
            for(int i = 0; i < tmp.size(); i++)
            	System.out.println("\tcluster_" + (i + 1) + " : "+ tmp.get(i).getPoints().size());

        	clusteres.clear();
        	clusteres = tmp;
        }
        
        //Start processing
        List<UserProfile> generalized = new ArrayList<UserProfile>();
        
        //TO-DO replace all dimensions distance measurement with a proper distance for finding the cluster center
        DistanceMeasure allDimensionsDistance = DistanceMeasureFactory.getInstance(new String[] {"ALL"});
        for(Cluster<UserProfile> aCluster : clusteres) {
        	UserProfile clusterCenter = new UserProfile();
        	TreeMap<Double, HashSet<UserProfile>> distances = new TreeMap<Double, HashSet<UserProfile>>();
        	Abstracts.FindCenter(allDimensionsDistance, aCluster, clusterCenter, distances);
        	
        	//Generalized.Add(Preferences.GeneralizeProfile(center, distances, _Profiles))
        	generalized.add(clusterCenter);
        }
        
        System.out.println("--------");
		System.out.println("write JS file to "+_OutputFile.getAbsolutePath());

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
		
		if(!_OutputFile.exists())
			_OutputFile.createNewFile();
				
		PrintWriter writer = new PrintWriter(_OutputFile);
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
        writer.println("var fluid = fluid || require(\"universal\");");
        writer.println("var stat = fluid.registerNamespace(\"easytv.matchMaker.statistical\");");

        //entry count
        writer.println("stat.entryCount = " + clusters.size() + ";");
        
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

}
