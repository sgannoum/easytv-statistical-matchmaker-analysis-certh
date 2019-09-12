package com.certh.iti.easytv.stmm.io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.json.JSONObject;

import com.certh.iti.easytv.user.UserProfile;

public class DBProfileReader implements ProfileReader{

	private String Url;
	private String userName;
	private String Password;
	private Connection con;

	public DBProfileReader(String Url, String userName, String Password) {
		this.Url = Url;
		this.userName = userName;
		this.Password = Password;
	}

	public Cluster<UserProfile> readProfiles() {
		Cluster<UserProfile> profiles = new Cluster<UserProfile>();
		System.out.println("Try to connect with db : "+ "jdbc:mysql://"+ Url);
		try 
		{			
			con = DriverManager.getConnection("jdbc:mysql://"+ Url, userName, Password);
			
			System.out.println("Connection success....");
			
			// here sonoo is database name, root is username and password
			java.sql.Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select userModel from userModels");
			
			while (rs.next()) {
				
				JSONObject json =  new JSONObject(rs.getString(1));
				profiles.addPoint(new UserProfile(new JSONObject(rs.getString(1))));
				
				System.out.println("Reading file: " + json.toString());
			}
			
			//close
			con.close();
			
		} catch (Exception e) {
			System.out.println("Connection failed....");
			e.printStackTrace();
		}
		
		return profiles;
		
		
	}

}
