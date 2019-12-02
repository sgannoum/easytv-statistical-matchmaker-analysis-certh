package com.certh.iti.easytv.stmm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import com.certh.iti.easytv.stmm.clustering.Config;
import com.certh.iti.easytv.stmm.clustering.iCluster;
import com.certh.iti.easytv.stmm.io.DBProfileReader;
import com.certh.iti.easytv.stmm.io.DirectoryProfileReader;
import com.certh.iti.easytv.stmm.io.ProfileReader;
import com.certh.iti.easytv.stmm.io.ProfileWriter;
import com.certh.iti.easytv.stmm.io.StmmJSWriter;
import com.certh.iti.easytv.stmm.io.StmmWriter;
import com.certh.iti.easytv.stmm.preferences.Abstracts;
import com.certh.iti.easytv.stmm.similarity.DistanceMeasureFactory;
import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

public class Main {

	private final static Logger logger = Logger.getLogger(Main.class.getName());

	// Arguments
	private static final String _ArgConfigFile = "-c";
	private static final String _ArgProfilesDirectory = "-p";
	private static final String _ArgOutputDirectory = "-o";
	
	// Profiles
	private static File _ConfigFile = null;
	private static File _OutputDirectory = null;
	private static File _ProfilesDirectory = null;
	private static Cluster<Profile> _Profiles;
	private static ProfileReader profileReader;
	private static ProfileWriter profileWriter;
	private static String STMM_HOST = "localhost";
	private static String STMM_PORT = "8077";

	private static String DB_HOST = "localhost";
	private static String DB_PORT = "8077";
	private static String DB_NAME = "easytv";
	private static String DB_USER = "easytv";
	private static String DB_PASSWORD = "easytv";
	
	public static void main(String[] args) 
			throws NumberFormatException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, 
				   SecurityException, IOException, UserProfileParsingException 
	{	
		
		
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
            logger.info("Could not find profiles directory, reverted to :'" + _ConfigFile.getAbsolutePath() + "'");
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
		
		if(System.getenv("DB_PASSWORD_FILE") != null) {
			String line = "";
			BufferedReader reader = new BufferedReader(new FileReader(System.getenv("DB_PASSWORD_FILE")));
			StringBuffer buff = new StringBuffer();
			
			while((line = reader.readLine()) != null) 
				buff.append(line);
			
			//close
			reader.close();
			
			//trim
			buff.trimToSize();
			
			DB_PASSWORD = buff.toString();
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
		} else {

			//Load profiles from db
			DBProfileReader dbReader = new DBProfileReader(DB_HOST + ":"+DB_PORT+"/"+DB_NAME, DB_USER, DB_PASSWORD);
			_Profiles = dbReader.readProfiles();
		}
		

		logger.info("Finished loading " + _Profiles.getPoints().size() + " profiles.");
		logger.info(Profile.getStatistics());
		logger.info("Start clustering...");
		
        //clusters
        List<Cluster<Profile>> foundedClusters = new ArrayList<Cluster<Profile>>();
    	List<Cluster<Profile>> tmp = new ArrayList<Cluster<Profile>>();

        //start with all profile as first cluster
        foundedClusters.add(_Profiles);

        //run all clustering algorithms
        for(iCluster clusterer : Config.getInstance().Clusteres) {

        	logger.info("["+clusterer.get_Name()+"] "+ clusterer.toString());
        	
        	//cluster each cluster
        	for(Cluster<Profile> cluster : foundedClusters) 
        		tmp.addAll(clusterer.getClusterer().cluster(cluster.getPoints()));
        			
            logger.info("Clusters generated... " + tmp.size());
            for(int i = 0; i < tmp.size(); i++)
            	logger.info("cluster_" + (i + 1) + " : "+ tmp.get(i).getPoints().size());

        	foundedClusters.clear();
        	foundedClusters.addAll(tmp);
        	tmp.clear();
        }
        
        //When no clusters produced, don't proceed
        if(foundedClusters.isEmpty()) 
			return ;
        
        
        //Start processing
        List<Profile> generalized = new ArrayList<Profile>();
        
        //Generalize clusters
        DistanceMeasure allDimensionsDistance = DistanceMeasureFactory.getInstance();
        for(Cluster<Profile> aCluster : foundedClusters) {
        	Profile clusterCenter = new Profile();
        	
        	//Find the cluster center
        	Abstracts.FindCenter(allDimensionsDistance, aCluster, clusterCenter);
        	
        	//TO-DO generalize cluster
        	//Generalized.Add(Preferences.GeneralizeProfile(center, distances, _Profiles))
        	
        	generalized.add(clusterCenter);
        }
        
        logger.info("--------");
		if(_OutputDirectory != null && _ProfilesDirectory != null) {
		
			logger.info("Write dimensions handlers and clutering data JS files.");

	        //Write JS
			profileWriter = new StmmJSWriter(_OutputDirectory, generalized); 
			profileWriter.write();
			
		} else {
			logger.info("Inform stmm via: " + "http://"+STMM_HOST+":"+STMM_PORT+"/EasyTV_STMM_Restful_WS/analysis/clusters");
	        
			//inform stmm runtime via http request
			StmmWriter stmmWriter = new StmmWriter("http://"+STMM_HOST+":"+STMM_PORT+"/EasyTV_STMM_Restful_WS/analysis/clusters", generalized); 
			stmmWriter.write();
		}
        

	}

}
