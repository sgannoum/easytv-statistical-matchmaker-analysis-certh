package com.certh.iti.easytv.stmm.io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

	@Override
	public Cluster<Profile> readProfiles() {
		Profile profile;
		Cluster<Profile> profiles = new Cluster<Profile>();
		logger.info("Try to connect with db : "+ "jdbc:mysql://"+ Url);
		
		try 
		{			
			con = DriverManager.getConnection("jdbc:mysql://"+ Url, userName, Password);
						
			// here sonoo is database name, root is username and password
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT userId, userModel as user_profile, userContext, userContent "
										   + "FROM userModels");
						
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
	
	@Override
	public Cluster<Profile> readUserHisotryOfInteractionOfModel(int modelId, long timeInterval) {
		Profile profile;
		Cluster<Profile> profiles = new Cluster<Profile>();
		

		try 
		{			
			con = DriverManager.getConnection("jdbc:mysql://"+ Url, userName, Password);
						
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT preferences, context, time "
										   + "FROM InteractionEvents "
										   + "WHERE modelId="+ modelId +" "
										   + "ORDER BY time");
			Time previous = new Time(0);
			int n = 0;
			while (rs.next()) {
				
				//ignore an event that has time interval less than one second 
				long timeDiff = rs.getTime("time").getTime() - previous.getTime();
				if(timeDiff < timeInterval) continue;
				
				previous = rs.getTime("time");
				
				JSONObject json = new JSONObject()
						.put("user_id", modelId)
						.put("user_profile",new JSONObject()
						.put("user_preferences", new JSONObject()
						.put("default", new JSONObject()
						.put("preferences", new JSONObject(rs.getString("preferences"))))));
				
				
				if(rs.getString("context") != null) 
					json
						.put("user_context", new JSONObject(rs.getString("context")));

				try 
				{
					profile = new Profile(json);
				} catch (UserProfileParsingException e1) {
					logger.warning("Problem loading interaction event: "+e1.getMessage());
					continue;
				} 
				
				//add to profile list
				profiles.addPoint(profile);
				n++;
			}
			
			if(n > 0)
				logger.info(String.format("Finished loading %d events associated with model: %d", n, modelId));

			//close
			con.close();
			
		} catch (Exception e2) {
			logger.info("Connection failed...."+e2.getMessage());
			e2.printStackTrace();
		}
		
		return profiles;
	}
	
	@Override
	public void clearHisotryOfInteraction() {	
		try 
		{			
			con = DriverManager.getConnection("jdbc:mysql://"+ Url, userName, Password);
						
			ResultSet rs = con.createStatement()
							  .executeQuery("DELETE "
										  + "FROM InteractionEvents "
										  + "WHERE id >= 0 ");
			
			logger.info("User history of interaction cleared...."+rs.getFetchSize());

			//close
			con.close();
			
		} catch (Exception e2) {
			logger.info("Connection failed...."+e2.getMessage());
			e2.printStackTrace();
		}
		
	}
	
	@Override
	public List<Integer> getModelsId() {
		List<Integer> ids = new ArrayList<Integer>();
		
		try 
		{			
			con = DriverManager.getConnection("jdbc:mysql://"+ Url, userName, Password);
						
			// here sonoo is database name, root is username and password
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT id "
										   + "FROM userModels");
						
			while (rs.next()) {
				ids.add(rs.getInt("id"));
			}
			
			//close
			con.close();
			
		} catch (Exception e2) {
			logger.info("Connection failed...."+e2.getMessage());
			e2.printStackTrace();
		}
		
		return ids;
	}

	@Override
	public void writeUserModificationSuggestions(int modelId, JSONObject suggestions) {
		
		String suggestion = suggestions.toString();
		
		try 
		{			
			con = DriverManager.getConnection("jdbc:mysql://"+ Url, userName, Password);
			boolean rs = con.createStatement()
							  .execute("INSERT INTO ModificationSuggestions "+
									   String.format("(id, suggestion) VALUE (%d, '%s') ", modelId, suggestion) + 
									   "ON DUPLICATE KEY UPDATE "+ 
									   String.format("id = %d, suggestion = '%s' ", modelId, suggestion));
			
			//close
			con.close();
			
			logger.info(String.format("Finished writing suggestions to model: %d", modelId));
			
		} catch (Exception e2) {
			logger.info("Connection failed...."+e2.getMessage());
			e2.printStackTrace();
		}
		
	}
	
	@Override
	public void writeUserModificationSuggestions(Map<Integer, JSONObject> params) {
		
		try 
		{			
			con = DriverManager.getConnection("jdbc:mysql://"+ Url, userName, Password);
			PreparedStatement pstmt = con.prepareStatement(
									  "INSERT INTO ModificationSuggestions "+
							   		  "(id, suggestion) VALUE ( ? , ? ) " + 
							   		  "ON DUPLICATE KEY UPDATE "+ 
							          "id = ? , suggestion = ? ");
			
			
			for(Entry<Integer, JSONObject> entry : params.entrySet()) {
				pstmt.setInt(1, entry.getKey().intValue());
				pstmt.setString(2, entry.getValue().toString());

				pstmt.setInt(3, entry.getKey().intValue());
				pstmt.setString(4, entry.getValue().toString());

				//add for batch execution
				pstmt.addBatch();
			}
			
			//execute batch updates
			int[] updates = pstmt.executeBatch();
			
			//close
			con.close();
			
			logger.info(String.format("Finished updating %d model suggestions", updates.length));
			
		} catch (Exception e2) {
			logger.info("Connection failed...."+e2.getMessage());
			e2.printStackTrace();
		}
		
	}

}
