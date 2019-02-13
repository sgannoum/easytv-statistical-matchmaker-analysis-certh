package com.certh.iti.easytv.stmm.user.profile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONObject;

public class UserProfile implements Clusterable {
	
	private final double NoiseReduction = 0.2;
	private Boolean _IsAbstract = true;
	private File _File;
	private General general = null;
	private Visual visualCapabilities = null;
	private Auditory auditoryCapabilities = null;
	private UserPreferences userPreferences = null;
	private JSONObject jsonObj = null;
	
	public UserProfile() {
		_IsAbstract = true;
		_File = null;
		general = null;
		visualCapabilities = null;
		auditoryCapabilities = null;
		userPreferences = null;
	}
	
	public UserProfile(File _File) throws IOException {
		_IsAbstract = false;
		_File = null;
		jsonObj = null;
		ReadProfileJSON(_File);
	}
	
	public UserProfile(JSONObject json) throws IOException {
		_IsAbstract = false;
		_File = null;
		jsonObj = null;
		setJSONObject(json);
	}
	
	public UserProfile(General general, Visual visualCapabilities, Auditory auditoryCapabilities, UserPreferences userPreferences) throws IOException {
		_File = null;
		_IsAbstract = true;
		jsonObj = null;
		this.setGeneral(general);
		this.setVisualCapabilities(visualCapabilities);
		this.setAuditoryCapabilities(auditoryCapabilities);
		this.setUserPreferences(userPreferences);
	}
	
	public UserProfile(General general, Visual visualCapabilities, Auditory auditoryCapabilities, UserPreferences userPreferences, boolean isAbstract) throws IOException {
		_File = null;
		_IsAbstract = isAbstract;
		jsonObj = null;
		this.setGeneral(general);
		this.setVisualCapabilities(visualCapabilities);
		this.setAuditoryCapabilities(auditoryCapabilities);
		this.setUserPreferences(userPreferences);
	}
	
	public Boolean IsAbstract() {
		return _IsAbstract;
	}

	public File get_File() {
		return _File;
	}

	public double distanceTo(UserProfile other) {
		return general.distanceTo(other.general) +
				visualCapabilities.distanceTo(other.visualCapabilities)+
					auditoryCapabilities.distanceTo(other.auditoryCapabilities) + 
					 	userPreferences.distanceTo(other.userPreferences);
	}
	
	public void setJSONObject(JSONObject json) {		
		if(general == null)
			general = new General(json.getJSONObject("general"));
		else 
			general.setJSONObject(json.getJSONObject("general"));
		
		if(visualCapabilities == null)
			visualCapabilities = new Visual(json.getJSONObject("visual"));
		else
			visualCapabilities.setJSONObject(json.getJSONObject("visual"));
		
		if(auditoryCapabilities == null)
			auditoryCapabilities = new Auditory(json.getJSONObject("auditory"));
		else 
			auditoryCapabilities.setJSONObject(json.getJSONObject("auditory"));
		
		if(userPreferences == null)
			userPreferences = new UserPreferences(json.getJSONObject("user_preferences"));
		else
			userPreferences.setJSONObject(json.getJSONObject("user_preferences"));
		
		jsonObj = json;
	}
	
	public JSONObject getJSONObject() {
		if(jsonObj == null) {
			jsonObj = new JSONObject();
			jsonObj.put("general", general.getJSONObject());
			jsonObj.put("visual", visualCapabilities.getJSONObject());
			jsonObj.put("auditory", auditoryCapabilities.getJSONObject());
			jsonObj.put("user_preferences", userPreferences.getJSONObject());
		}
		return jsonObj;
	}

	public General getGeneral() {
		return general;
	}

	public void setGeneral(General general) {
		this.general = general;
	}

	public Visual getVisualCapabilities() {
		return visualCapabilities;
	}

	public void setVisualCapabilities(Visual visualCapabilities) {
		this.visualCapabilities = visualCapabilities;
	}

	public Auditory getAuditoryCapabilities() {
		return auditoryCapabilities;
	}

	public void setAuditoryCapabilities(Auditory auditoryCapabilities) {
		this.auditoryCapabilities = auditoryCapabilities;
	}
	
	public UserPreferences getUserPreferences() {
		return userPreferences;
	}

	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}
	
	public double[] getPoint() {
		double[] generalPoints = general.getPoint();
		double[] visualPoints = visualCapabilities.getPoint();
		double[] auditoryPoints = auditoryCapabilities.getPoint();
		double[] defaultPreferencePoints = userPreferences.getPoint();
		
		int size = generalPoints.length + visualPoints.length + auditoryPoints.length + defaultPreferencePoints.length;
		double [] userProfilePoints = new double[size];
		int index = 0;
		
		for(int i = 0; i < generalPoints.length; i++, index++)
			userProfilePoints[index] = generalPoints[i];
		
		for(int i = 0; i < visualPoints.length; i++, index++)
			userProfilePoints[index] = visualPoints[i];
		
		for(int i = 0; i < auditoryPoints.length; i++, index++)
			userProfilePoints[index] = auditoryPoints[i];
		
		for(int i = 0; i < defaultPreferencePoints.length; i++, index++)
			userProfilePoints[index] = defaultPreferencePoints[i];
		
		return userProfilePoints;
	}
	
	private void ReadProfileJSON(File file) throws IOException {
		System.out.println("Reading profile: " + file.getAbsolutePath() + "");
		
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		StringBuffer json = new StringBuffer();
		while((line = reader.readLine()) != null) {
			json.append(line);
		}
		
		this.setJSONObject(new JSONObject(json.toString()));
	}
}