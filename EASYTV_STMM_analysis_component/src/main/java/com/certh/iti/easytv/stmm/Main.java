package com.certh.iti.easytv.stmm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import com.certh.iti.easytv.stmm.clustering.Config;
import com.certh.iti.easytv.stmm.clustering.iClustering;
import com.certh.iti.easytv.stmm.parser.Parser;
import com.certh.iti.easytv.stmm.preferences.Abstracts;
import com.certh.iti.easytv.stmm.preferences.Entry;
import com.certh.iti.easytv.stmm.preferences.EntryManager;
import com.certh.iti.easytv.stmm.preferences.Profile;

public class Main {

	// Arguments
	private static final String _ArgConfigFile = "-c";
	private static final String _ArgProfilesDirectory = "-p";
	private static final String _ArgOutputFile = "-o";
	
	// Profiles
	private static File _ConfigFile = null;
	private static File _OutputFile = null;
	private static File _ProfilesDirectory = null;
	private static List<Profile> _Profiles = new ArrayList<Profile>();

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
            _ProfilesDirectory = new File(pwd + File.separator + "GPII-Statistical-Matchmaker-Data");
            System.out.println("Could not find profiles directory, reverted to :'" + _ProfilesDirectory.getAbsolutePath() + "'");
		}
		
		if(_ConfigFile == null || !_ConfigFile.exists() ) {
			String pwd = System.getProperty("user.dir");
			_ConfigFile = new File(pwd + File.separator + "GPII-Statistical-Matchmaker-Data"+ File.separator+ "config.ini");
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
        System.out.println("Finished loading " + _Profiles.size() + " profiles.");
        System.out.println("--------");
        System.out.println("Analyzing preferences...");
        EntryManager.GetMinsAndMaxs(_Profiles);
        System.out.println("--------");
        System.out.println("Clustering...");
        
        //Cluster
        List<HashSet<Profile>> clusters = new ArrayList<HashSet<Profile>>();
        HashSet<Profile> noise = new HashSet<Profile>();
        Iterator<iClustering> clusterer = Config.getInstance().Config.iterator();
        while(clusterer.hasNext()) 
        	clusterer.next().Run(_Profiles, clusters, noise);
        
        
        //Start processing
        System.out.println("--------");
        System.out.println("Reducing clusters..." + clusters.size());
        List<Profile> generalized = new ArrayList<Profile>();
        
        Iterator<HashSet<Profile>> clustersIter = clusters.iterator();
        while(clustersIter.hasNext()) {
        	HashSet<Profile> aCluster = clustersIter.next();
            Profile clusterCenter = new Profile();
        	TreeMap<Double, HashSet<Profile>> distances = new TreeMap<Double, HashSet<Profile>>();
        	Abstracts.FindCenter(aCluster, clusterCenter, distances);
        	
        	//Generalized.Add(Preferences.GeneralizeProfile(center, distances, _Profiles))
        	generalized.add(clusterCenter);
        }

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

		class IniFileFilter implements FilenameFilter {
			public boolean accept(File dir, String name) {
				return name.endsWith(".ini") && name.contains(".gen");
			}
		}

		File[] iniFiles = directory.listFiles(new IniFileFilter());
		for (int i = 0; i < iniFiles.length; i++)
			Parser.PreprocessProfile(iniFiles[i], generatedDirectory);

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
				return name.endsWith(".ini") && !name.contains(".gen");
			}
		}

		File[] iniFiles = directory.listFiles(new IniFileFilter());
		for (int i = 0; i < iniFiles.length; i++)
			Parser.ReadProfile(iniFiles[i], _Profiles);

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
	 * The extracted js file contain information about the data type of each preference type
	 * in addition to the min and max value.
	 * 
	 * @param clusters
	 * @throws IOException
	 */
	private static void WriteJavaScript(List<Profile> clusters) throws IOException {
		
		class PreferenceType {
			public String name;
			public boolean IsEnum;
			public double Min, Max;
			
			public PreferenceType() {}
		}
		
		if(!_OutputFile.exists())
			_OutputFile.createNewFile();
		
		PrintWriter writer = new PrintWriter(_OutputFile);
		Date now = new Date();
		DateFormat df = new SimpleDateFormat("dd MMM HH:mm:ss");

        writer.println("/*!");
        writer.println();
        writer.println("GPII/Cloud4all Statistical Matchmaker ");
        writer.println();
        writer.println("Copyright 2012-2015 Hochschule der Medien (HdM) / Stuttgart Media University");
        writer.println();
        writer.println("Licensed under the New BSD License. You may not use this file except in");
        writer.println("compliance with this licence.");
        writer.println();
        writer.println("You may obtain a copy of the licence at");
        writer.println("https://github.com/REMEXLabs/GPII-Statistical-Matchmaker/blob/master/LICENSE.txt");
        writer.println();
        writer.println("The research leading to these results has received funding from");
        writer.println("the European Union's Seventh Framework Programme (FP7/2007-2013)");
        writer.println("under grant agreement no. 289016.");
        writer.println("*/");
        writer.println();
        writer.println("//Generated " +  df.format(now));
        writer.println("var fluid = fluid || require(\"universal\");");
        writer.println("var stat = fluid.registerNamespace(\"gpii.matchMaker.statistical\");");

        //entry count
        writer.println("stat.entryCount = " + EntryManager.EntryNames().size() + ";");
        
        //clusters
        writer.println("stat.clusters = [");
        
        int clusterCount = 0;
        Iterator<Profile> clustersIter = clusters.iterator();
        while(clustersIter.hasNext()) {
        	clusterCount++;
        	Profile cluster = clustersIter.next();
        	if(cluster.PreferenceEntries.size() == 0) 
        		continue;
            
        	writer.println("\t"+ "{");
            
        	HashMap<String, HashSet<Entry>> prefsByApp = new HashMap<String, HashSet<Entry>>();
        	Iterator<String> entryNamesIter = EntryManager.EntryNames().iterator();
            while(entryNamesIter.hasNext()){
            	Entry entry = null;
            	if(( entry = cluster.PreferenceEntries.get(entryNamesIter.next())) != null) {
            		HashSet<Entry> appPrefs = null;
            		if((appPrefs = prefsByApp.get(entry.get_Application())) == null) {
            			appPrefs = new HashSet<Entry>();
            			prefsByApp.put(entry.get_Application(), appPrefs);
            		}
            		appPrefs.add(entry);
            	}
            }
            
            //Print
            Iterator<java.util.Map.Entry<String, HashSet<Entry>>> prefsByAppIter = prefsByApp.entrySet().iterator();
            int preferenceCount = 0;
            while(prefsByAppIter.hasNext()) {
            	preferenceCount++;
            	java.util.Map.Entry<String, HashSet<Entry>> pair = prefsByAppIter.next();
            	writer.println("\t\t" + "\"" + pair.getKey() + "\": {");
            	Iterator<Entry> pairIter = pair.getValue().iterator();
            	int pairCount = 0;
            	while(pairIter.hasNext()) {
            		pairCount++;
            		if(pairCount < pair.getValue().size()) 
            			writer.println("\t\t\t" + WriteJavaScriptEntry(pairIter.next()) + ",");
            		else 
                        writer.println("\t\t\t" + WriteJavaScriptEntry(pairIter.next()));
            	}
                	//Get the last element from the map
                	if(preferenceCount == prefsByApp.entrySet().size()) 
                		writer.println("\t\t" + "}");
                	else 
                		writer.println("\t\t" + "},");
            	}
            	
            	if(clusterCount < clusters.size()) 
            		writer.println("\t" + "},");
            	else 
            		writer.println("\t" + "}");	

            } 
            writer.println("];");
            
            //Properties
            writer.println("stat.preferenceTypes = {");
            HashMap<String, List<PreferenceType>> typesByApp = new HashMap<String, List<PreferenceType>>();
        	Iterator<String> entryNamesIter = EntryManager.EntryNames().iterator();
            while(entryNamesIter.hasNext()){
            	String entryName = entryNamesIter.next();
            	List<PreferenceType> appPrefs = null;
            	if((appPrefs = typesByApp.get(EntryManager.EntryApp(entryName))) == null) {
            		appPrefs = new ArrayList<PreferenceType>();
            		typesByApp.put(EntryManager.EntryApp(entryName), appPrefs);
            	}
            	
            	PreferenceType newPrefType = new PreferenceType();
                newPrefType.name = entryName;
                newPrefType.IsEnum = EntryManager.IsEnumeration(entryName);
                newPrefType.Min = EntryManager.EntryMin(entryName);
                newPrefType.Max = EntryManager.EntryMax(entryName);
                appPrefs.add(newPrefType);
            }
        	
            //Print
            int typesCount = 0;
            Iterator<java.util.Map.Entry<String, List<PreferenceType>>> typesByAppIter = typesByApp.entrySet().iterator();
            while(typesByAppIter.hasNext()) {
            	typesCount++;
            	java.util.Map.Entry<String, List<PreferenceType>> pair = typesByAppIter.next();
            	writer.println("\t" + "\"" + pair.getKey() + "\": {");
            	for(int n = 0; n < pair.getValue().size(); n++) {
                    writer.println("\t\t" + "\"" + EntryManager.EntryPrintName(pair.getValue().get(n).name) + "\": {");
                    if(pair.getValue().get(n).IsEnum) {
                        writer.println("\t\t\t" + "\"isEnum\": true,");
                        writer.println("\t\t\t" + "\"min\": 0,");
                        writer.println("\t\t\t" + "\"max\": " + EntryManager.EnumerationSize(pair.getValue().get(n).name));
                    } else {
                        writer.println("\t\t\t" + "\"isEnum\": false,");
                        writer.println("\t\t\t" + "\"min\": " + pair.getValue().get(n).Min + ",");
                        writer.println("\t\t\t" + "\"max\": " + pair.getValue().get(n).Max);
                    }      
                    
                    if(n < pair.getValue().size() - 1) 
                    	writer.println("\t\t" + "},");
                    else 
                    	writer.println("\t\t" + "}");
            	}
            	

            	if(typesCount == typesByApp.entrySet().size()) 
            		writer.println("\t" + "}");
            	else
            		writer.println("\t" + "},");
            }
            
            writer.println("};");
            writer.close();
	}

	/**
	 * 
	 * @param entry
	 * @return
	 */
	private static String WriteJavaScriptEntry(Entry entry) {
		if (EntryManager.IsEnumeration(entry)) {
			String enumValue = EntryManager.ToEnumeration(entry.get_Name(), (int) entry.get_Value()).trim();
			enumValue = enumValue.replace(" |,|\"", "");
			if (!(enumValue.toLowerCase().equals("true") || enumValue.toLowerCase().equals("false")
					|| enumValue.charAt(0) == '[' || enumValue.charAt(0) == '{')) {
				enumValue = "\"" + enumValue + "\"";
			}
			return "\"" + EntryManager.EntryPrintName(entry.get_Name()) + "\": " + enumValue;
		} else {
			return "\"" + EntryManager.EntryPrintName(entry.get_Name()) + "\": " + entry.get_Value();
		}
	}

}
