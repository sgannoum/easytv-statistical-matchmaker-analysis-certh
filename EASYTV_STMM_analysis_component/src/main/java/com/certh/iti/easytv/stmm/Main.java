package com.certh.iti.easytv.stmm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.json.JSONArray;

import com.certh.iti.easytv.stmm.association.analysis.RuleRefiner;
import com.certh.iti.easytv.stmm.association.analysis.rules.RbmmRuleWrapper;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper;
import com.certh.iti.easytv.stmm.clustering.Clustere;
import com.certh.iti.easytv.stmm.clustering.Config;
import com.certh.iti.easytv.stmm.io.DBProfileReader;
import com.certh.iti.easytv.stmm.io.DirectoryProfileReader;
import com.certh.iti.easytv.stmm.io.EmailHandler;
import com.certh.iti.easytv.stmm.io.ProfileReader;
import com.certh.iti.easytv.stmm.io.ProfileWriter;
import com.certh.iti.easytv.stmm.io.JsFileWriter;
import com.certh.iti.easytv.stmm.io.HttpHandler;
import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;


public class Main {

	private final static Logger logger = Logger.getLogger(Main.class.getName());

	// Arguments
	private static final String _ArgConfigFile = "-c";
	private static final String _ArgProfilesDirectory = "-p";
	private static final String _ArgOutputDirectory = "-o";
	private static final String _ArgRbmmRulesFile = "-r";
	private static final String _ArgEnviroment = "-e";
	
	// Profiles
	private static File _ConfigFile = null;
	private static File _OutputDirectory = null;
	private static File _ProfilesDirectory = null;
	private static File _rbmmRulesFile = null;
	private static Cluster<Profile> _Profiles;
	private static ProfileReader profileReader;
	private static ProfileWriter profileWriter;
	private static String STMM_HOST = "localhost";
	private static String STMM_PORT = "8077";
	private static String RBMM_HOST = "localhost";
	private static String RBMM_PORT = "8080";

	private static String DB_HOST = "172.20.0.2";
	private static String DB_PORT = "3306";
	private static String DB_NAME = "easytv";
	private static String DB_USER = "easytv";
	private static String DB_PASSWORD = "easytv";
	private static String ENVIRONMENT = "development";
	
	private static Vector<RbmmRuleWrapper> rbmmRules;
	private static double RULES_MIN_SUPPORT = 0.8;
	private static double RULES_MIN_CONFIDENCE = 0.9;
	
	
	private static DBProfileReader dbReader = null;
	
	public static void main(String[] args) 
			throws NumberFormatException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, 
				   SecurityException, IOException, UserProfileParsingException, AddressException, NoSuchAlgorithmException, MessagingException 
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
			else if (arg.equals(_ArgRbmmRulesFile)) 
				_rbmmRulesFile = new File(value.trim());
			else if (arg.equals(_ArgEnviroment)) 
				ENVIRONMENT = value.trim();
		}
		
		if(_ConfigFile == null || !_ConfigFile.exists() ) {
			String pwd = System.getProperty("user.dir");
			_ConfigFile = new File(pwd + File.separator + "config.ini");
            logger.info("Could not find profiles directory, reverted to :'" + _ConfigFile.getAbsolutePath() + "'");
		}
			
		if(!_ConfigFile.exists()) {
            logger.info("CRITICAL: Profiles directory ('" + _ConfigFile.getAbsolutePath() + "') does not exist.");
			System.exit(-1);
		}

		if(System.getenv("STMM_HOST") != null)  				STMM_HOST = System.getenv("STMM_HOST") ;
		if(System.getenv("STMM_PORT") != null) 					STMM_PORT = System.getenv("STMM_PORT") ;
		if(System.getenv("RBMM_HOST") != null) 					RBMM_HOST = System.getenv("RBMM_HOST") ;
		if(System.getenv("RBMM_PORT") != null) 					RBMM_PORT = System.getenv("RBMM_PORT") ;
		if(System.getenv("DB_HOST") != null) 					DB_HOST = System.getenv("DB_HOST") ;
		if(System.getenv("DB_PORT") != null)					DB_PORT = System.getenv("DB_PORT") ;
		if(System.getenv("DB_NAME") != null) 					DB_NAME= System.getenv("DB_NAME") ;
		if(System.getenv("DB_USER") != null) 					DB_USER = System.getenv("DB_USER") ;
		if(System.getenv("DB_PASSWORD") != null)				DB_PASSWORD = System.getenv("DB_PASSWORD") ;
		if(System.getenv("RULES_MIN_SUPPORT") != null)			RULES_MIN_SUPPORT = Double.valueOf(System.getenv("RULES_MIN_SUPPORT"));
		if(System.getenv("RULES_MIN_CONFIDENCE") != null)		RULES_MIN_CONFIDENCE = Double.valueOf(System.getenv("RULES_MIN_CONFIDENCE"));
		if(System.getenv("ENVIRONMENT") != null)				ENVIRONMENT = System.getenv("ENVIRONMENT");
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
		
		/**
		 *	READ PROFILES
		 */
		if(_ProfilesDirectory != null && _ProfilesDirectory.exists()) {
			//From directory
			profileReader = new DirectoryProfileReader(_ProfilesDirectory);
		} else {
			//From DB
			dbReader = new DBProfileReader(DB_HOST + ":"+DB_PORT+"/"+DB_NAME, DB_USER, DB_PASSWORD);
		}
		
		//read profiles
		_Profiles = dbReader.readProfiles();

		//exit when there are no profiles
		if(_Profiles.getPoints().isEmpty()) {
			logger.info("No profiles loaded...exit");
			return;
		}
		
		logger.info("Finished loading " + _Profiles.getPoints().size() + " profiles.");
		
		if(!ENVIRONMENT.equals("development")) {
			logger.info("Send email with statistics...");
			EmailHandler.sendAttachmenentMail("noreply@easytvproject.eu", "salgan@iti.gr", Profile.getStatistics());
		}
		
		/**
		 *	ASSOCIATION ANALYSIS
		 */
		RULES_RFINEMENT(_Profiles);
		        
		/**
		 *	CLUSTERING
		 */
		CLUSTERING_ANALYSIS(_Profiles);
	}
	
	
	public static void RULES_RFINEMENT(Cluster<Profile> profiles) throws IOException {
		logger.info("Start mining rules...");
		if(_rbmmRulesFile != null) {
			logger.info("Read RBMM rules from file " + _rbmmRulesFile.getAbsolutePath());
			
			rbmmRules = new Vector<RbmmRuleWrapper>();
			
			String line;
			BufferedReader reader = new BufferedReader(new FileReader(_rbmmRulesFile));
			StringBuffer buff = new StringBuffer();
			
			while((line = reader.readLine()) != null) 
				buff.append(line);

			//close file
			reader.close();
			
			JSONArray rules = new JSONArray(buff.toString());
			for(int i = 0; i < rules.length(); i++)
				rbmmRules.add(new RbmmRuleWrapper(rules.getJSONObject(i)));
			
		} else {
			logger.info("Get RBMM rules from "+"http://"+RBMM_HOST+":"+RBMM_PORT+"/EasyTV_RBMM_Restful_WS/personalize/rules");
			rbmmRules = HttpHandler.readRules("http://"+RBMM_HOST+":"+RBMM_PORT+"/EasyTV_RBMM_Restful_WS/personalize/rules");
		}
		
		logger.info(""+rbmmRules.size()+" rules have been received.");
		
        RuleRefiner ruleRefiner = new RuleRefiner(Profile.getAggregator(),profiles.getPoints(), RULES_MIN_SUPPORT, RULES_MIN_CONFIDENCE);
        Vector<RuleWrapper> rules =  ruleRefiner.refineRules(rbmmRules);

        //Inform RBMM
        if(!rules.isEmpty() && !ENVIRONMENT.equals("development")) 
			HttpHandler.writeRules("http://"+RBMM_HOST+":"+RBMM_PORT+"/EasyTV_RBMM_Restful_WS/personalize/rules", rules);
	}
	
	public static void HISOTRY_OF_INTERACTION() throws IOException {
		logger.info("Start mining users history of interaction...");

		List<Integer> ids = dbReader.getUsersIds();
		for(Integer id : ids) {
			Cluster<Profile> history = dbReader.readUserHisotryOfInteraction(id);
			
	        RuleRefiner ruleRefiner = new RuleRefiner(Profile.getAggregator(), history.getPoints(), RULES_MIN_SUPPORT, RULES_MIN_CONFIDENCE);
	        Vector<RuleWrapper> rules =  ruleRefiner.refineRules(rbmmRules);
	        
	        //TODO write suggestions to db
	        
	        //TODO remove history of interaction
		}
	}
	
	public static void CLUSTERING_ANALYSIS(Cluster<Profile> profiles) throws UserProfileParsingException, IOException {
        logger.info("Start clustering...");
        List<Cluster<Profile>> foundedClusters = new ArrayList<Cluster<Profile>>();    	

        //start with all profile as first cluster
        foundedClusters.add(profiles);
               
        //Cluster user profiles
        Clustere clutere = new Clustere(Config.getInstance().Clusteres);
        List<Profile> generalized = clutere.getGeneralizedClusters(foundedClusters);
        
		/**
		 *	WRITE JS FILES
		 */
		if(_OutputDirectory != null) {
		
			logger.info("Write dimensions handlers and clutering data JS files.");

	        //Write JS
			profileWriter = new JsFileWriter(_OutputDirectory); 
			profileWriter.write(generalized);
			
		} else if(!generalized.isEmpty()){
			logger.info("Update stmm clusters: " + "http://"+STMM_HOST+":"+STMM_PORT+"/EasyTV_STMM_Restful_WS/analysis/clusters");
	        
			//inform stmm runtime via http request
			HttpHandler stmmWriter = new HttpHandler("http://"+STMM_HOST+":"+STMM_PORT+"/EasyTV_STMM_Restful_WS/analysis/clusters"); 
			stmmWriter.write(generalized);
		}
	}

}
