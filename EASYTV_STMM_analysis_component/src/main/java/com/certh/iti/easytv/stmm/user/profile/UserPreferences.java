package com.certh.iti.easytv.stmm.user.profile;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONObject;

import com.certh.iti.easytv.stmm.user.profile.preference.ConditionalPreference;
import com.certh.iti.easytv.stmm.user.profile.preference.Preference;

public class UserPreferences implements Clusterable {
	
	private List<Preference> preferences;
	private JSONObject jsonObj;
	
	public UserPreferences(JSONObject json) {
		this.setJSONObject(json);
	}
	
	public UserPreferences(List<Preference> preferences) {
		this.setPreferences(preferences);
		jsonObj = null;
	}

	public List<Preference> getPreferences() {
		return preferences;
	}
	
	public Preference getDefaultPreference() {
		return preferences.get(0);
	}

	public void setPreferences(List<Preference> preferences) {
		this.preferences = preferences;
	}

	public JSONObject getJSONObject() {
		if(jsonObj == null) {
			toJSON();
		}
		return jsonObj;
	}

	public void setJSONObject(JSONObject json) {
		String[] fields = JSONObject.getNames(json);
		
		preferences = new ArrayList<Preference>();
		preferences.add(new Preference("default", json.getJSONObject("default")));

		//Add conditional preferences
		for(int i = 0 ; i < fields.length; i++) {
			if(fields[i].equals("default")) {
				continue;
			} else {
				preferences.add(new ConditionalPreference(fields[i], json.getJSONObject(fields[i])));
			}
		}
		this.jsonObj = json;
	}
	
	public JSONObject toJSON() {
		if(jsonObj == null) {
			jsonObj = new JSONObject();
			for (int i = 0; i < preferences.size(); i++) 
				jsonObj.put(preferences.get(i).getName(), preferences.get(i).getJSONObject());
		}
		return jsonObj;
	}
	
	public double distanceTo(UserPreferences other) {
		//TO-DO include the conditional preferences
		Preference  pref1 = this.getDefaultPreference();
		Preference  pref2 = other.getDefaultPreference();
		return pref1.distanceTo(pref2);
	}
	
	public double distanceTo(UserProfile other) {
		return distanceTo(other.getUserPreferences());
	}

	public double[] getPoint() {
		//TO-DO include the conditional preferences
		return this.getDefaultPreference().getPoint();
	}

}
