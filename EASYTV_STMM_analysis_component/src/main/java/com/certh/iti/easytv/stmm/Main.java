package com.certh.iti.easytv.stmm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import com.certh.iti.easytv.stmm.clustering.Config;
import com.certh.iti.easytv.stmm.clustering.iCluster;
import com.certh.iti.easytv.stmm.io.DBProfileReader;
import com.certh.iti.easytv.stmm.io.DirectoryProfileReader;
import com.certh.iti.easytv.stmm.io.ProfileReader;
import com.certh.iti.easytv.stmm.io.ProfileWriter;
import com.certh.iti.easytv.stmm.io.StmmWriter;
import com.certh.iti.easytv.stmm.preferences.Abstracts;
import com.certh.iti.easytv.stmm.similarity.DistanceMeasureFactory;
import com.certh.iti.easytv.user.UserProfile;

public class Main {

	// Arguments
	private static final String _ArgConfigFile = "-c";
	private static final String _ArgProfilesDirectory = "-p";
	private static final String _ArgOutputDirectory = "-o";
	
	// Profiles
	private static File _ConfigFile = null;
	private static File _OutputDirectory = null;
	private static File _ProfilesDirectory = null;
	private static Cluster<UserProfile> _Profiles;
	private static ProfileReader profileReader;
	private static ProfileWriter profileWriter;
	private static String STMM_HOST = "localhost";
	private static String STMM_PORT = "8077";

	private static String DB_HOST = "localhost";
	private static String DB_PORT = "8077";
	private static String DB_NAME = "easytv";
	private static String DB_USER = "easytv";
	private static String DB_PASSWORD = "easytv";
	
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
		
		if(_ConfigFile == null || !_ConfigFile.exists() ) {
			String pwd = System.getProperty("user.dir");
			_ConfigFile = new File(pwd + File.separator + "config.ini");
            System.out.println("Could not find profiles directory, reverted to :'" + _ConfigFile.getAbsolutePath() + "'");
		}
		
		if(_OutputDirectory == null && _ProfilesDirectory != null) {
			String pwd = System.getProperty("user.dir");
			_OutputDirectory = new File(_ProfilesDirectory.getAbsolutePath() + File.separator + "StatisticalMatchMakerData.js");
            System.out.println("Could not find profiles directory, reverted to :'" + _OutputDirectory.getAbsolutePath() + "'");
		}
		
		if(!_ConfigFile.exists()) {
			System.err.println("CRITICAL: Profiles directory ('" + _ConfigFile.getAbsolutePath() + "') does not exist.");
			System.exit(-1);
		}

		if(System.getenv("STMM_HOST") != null) {
			STMM_HOST = System.getenv("STMM_HOST") ;
		}
		
		if(System.getenv("STMM_PORT") != null) {
			STMM_PORT = System.getenv("STMM_PORT") ;
		}
		
		if(System.getenv("DB_HOST") != null) {
			DB_HOST = System.getenv("DB_HOST") ;
		}
		
		if(System.getenv("DB_PORT") != null) {
			DB_PORT = System.getenv("DB_PORT") ;
		}
		
		if(System.getenv("DB_NAME") != null) {
			DB_NAME= System.getenv("DB_NAME") ;
		}
		
		if(System.getenv("DB_USER") != null) {
			DB_USER = System.getenv("DB_USER") ;
		}
		
		if(System.getenv("DB_PASSWORD") != null) {
			DB_PASSWORD = System.getenv("DB_PASSWORD") ;
		}
	
		//Read config
		Config.getInstance().ReadConfiguration(_ConfigFile);
				
		//Read profiles
		if(_ConfigFile == null || !_ConfigFile.exists())
			throw new IllegalStateException("Config File not set.");
		
		//Read profiles from directory
		if(_ProfilesDirectory != null && _ProfilesDirectory.exists()) {
			profileReader = new DirectoryProfileReader(_ProfilesDirectory);
			_Profiles = profileReader.readProfiles();
		}

		//Load profiles from db
		DBProfileReader dbReader = new DBProfileReader(DB_HOST + ":"+DB_PORT+"/"+DB_NAME, DB_USER, DB_PASSWORD);
		_Profiles = dbReader.readProfiles();
		
        System.out.println("--------");
        System.out.println("Finished loading " + _Profiles.getPoints().size() + " profiles.");
        System.out.println("--------");
        System.out.println("Clustering...");
        
        //clusters
        List<Cluster<UserProfile>> clusteres = new ArrayList<Cluster<UserProfile>>();
        
        //start with all profile as first cluster
        clusteres.add(_Profiles);

        //run clustering algorithms
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
		System.out.println("Inform stmm via http request");
        
		//inform stmm runtime via http request
		StmmWriter stmmWriter = new StmmWriter("http://"+STMM_HOST+":"+STMM_PORT+"/EasyTV_STMM_Restful_WS/clusters", generalized); 
		stmmWriter.write();
		
		/*       System.out.println("--------");
		System.out.println("write JS file to "+_OutputDirectory.getAbsolutePath());
		
        //Write JS
		profileWriter = new JSWriter(_OutputDirectory, generalized); 
		profileWriter.write();*/
	}

}
