package com.certh.iti.easytv.stmm.io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Logger;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.json.JSONObject;

import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

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
		Profile profile;
		Cluster<Profile> profiles = new Cluster<Profile>();
		logger.info("Try to connect with db : "+ "jdbc:mysql://"+ Url);
		
		try 
		{			
			con = DriverManager.getConnection("jdbc:mysql://"+ Url, userName, Password);
			
			logger.info("Connection success....");
			
			// here sonoo is database name, root is username and password
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT userId, userModel as user_profile, userContext, userContent "
										   + "FROM userModels");
			
			logger.info("Parse user profiles...");
			
			while (rs.next()) {
				
				JSONObject json = new JSONObject()
						.put("user_id", rs.getInt("userId"))
						.put("user_profile", new JSONObject(rs.getString("user_profile")));
				
				if(rs.getString("userContext") != null) 
					json
						.put("user_context", new JSONObject(rs.getString("userContext")));

				if(rs.getString("userContent") != null) 
					json
						.put("user_content", new JSONObject(rs.getString("userContent")));
				
				try 
				{
					profile = new Profile(json);
				} catch (UserProfileParsingException e1) {
					//Print ERROR message and ignore error
					logger.warning("Problem loading profile: "+e1.getMessage());
					continue;
				} 
				
				//add to profile list
				profiles.addPoint(profile);
				
				logger.info("Reading profile user_id: " + profile.getUserId());
			}
			
			//close
			con.close();
			
		} catch (Exception e2) {
			logger.info("Connection failed...."+e2.getMessage());
			e2.printStackTrace();
		}
		
		return profiles;
	}

}
