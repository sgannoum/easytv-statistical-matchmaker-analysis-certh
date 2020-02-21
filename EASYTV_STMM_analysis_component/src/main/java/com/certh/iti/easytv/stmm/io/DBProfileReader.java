package com.certh.iti.easytv.stmm.io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Logger;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.json.JSONObject;

import com.certh.iti.easytv.user.Profile;

public class DBProfileReader implements ProfileReader{

	private final static Logger logger = Logger.getLogger(DBProfileReader.class.getName());
	
	private String Url;
	private String userName;
	private String Password;
	private Connection con;

	public DBProfileReader(String Url, String userName, String Password) {
		this.Url = Url;
		this.userName = userName;
		this.Password = Password;
	}

	public Cluster<Profile> readProfiles() {
		Cluster<Profile> profiles = new Cluster<Profile>();
		logger.info("\nTry to connect with db : "+ "jdbc:mysql://"+ Url);
		
		try 
		{			
			con = DriverManager.getConnection("jdbc:mysql://"+ Url, userName, Password);
			
			logger.info("\nConnection success....");
			
			// here sonoo is database name, root is username and password
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select userModel from userModels");
			
			logger.info("\nParse user profiles...");
			
			while (rs.next()) {
				
				//convert to json
				JSONObject user_profile =  new JSONObject(rs.getString(1));
				
				//add a pseudo id and user profile
				JSONObject json = new JSONObject()
										.put("user_id", 0)
										.put("user_profile", user_profile);
				
				
				Profile profile = new Profile(json);
				
				//add to profile list
				profiles.addPoint(profile);
				
				logger.info("\nReading profile user_id: " + profile.getUserId());
			}
			
			//close
			con.close();
			
		} catch (Exception e) {
			logger.info("\nConnection failed....\n"+e.getMessage());
			e.printStackTrace();
		}
		
		return profiles;
		
		
	}

}
