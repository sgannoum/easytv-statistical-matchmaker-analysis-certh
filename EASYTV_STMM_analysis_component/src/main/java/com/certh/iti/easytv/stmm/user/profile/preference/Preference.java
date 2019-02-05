package com.certh.iti.easytv.stmm.user.profile.preference;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONException;
import org.json.JSONObject;

import com.certh.iti.easytv.stmm.user.profile.preference.entry.Entry;
import com.certh.iti.easytv.stmm.user.profile.preference.entry.LanguageEntry;
import com.certh.iti.easytv.stmm.user.profile.preference.entry.NumericEntry;
import com.certh.iti.easytv.stmm.user.profile.preference.entry.StringEntry;
import com.certh.iti.easytv.stmm.user.profile.preference.condition.operand.BooleanLiteral;
import com.certh.iti.easytv.stmm.user.profile.preference.condition.operand.ColorLiteral;
import com.certh.iti.easytv.stmm.user.profile.preference.condition.operand.LanguageLiteral;
import com.certh.iti.easytv.stmm.user.profile.preference.condition.operand.NumericLiteral;
import com.certh.iti.easytv.stmm.user.profile.preference.condition.operand.OperandLiteral;
import com.certh.iti.easytv.stmm.user.profile.preference.condition.operand.StringLiteral;
import com.certh.iti.easytv.stmm.user.profile.preference.entry.BooleanEntry;
import com.certh.iti.easytv.stmm.user.profile.preference.entry.ColorEntry;

public class Preference implements Clusterable {

	
	protected static final String[] PREFERENCE_ATTRIBUTE = {"audio_volume",
															"language_audio",
															"contrast",
															"font_size",
														//	"font", 
															"language_subtitles",
															"language_sign", 
															"tts_speed", 
															"tts_volume", 
															"tts_language", 
															"cs_accessibility_imageMagnification_scale",
															"cs_accessibility_textDetection", 
															"cs_audio_volume", 
															"cs_audio_track", 
															"cs_audio_description", 
															"cs_cc_audio_subtitles", 
															"cs_cc_subtitles_language", 
															"cs_cc_subtitles_font_size",
															"fontColor", 
															"backgroundColor"};
	protected String name;
	protected Map<String, OperandLiteral> preferences;
	protected JSONObject jsonObj;
	
	public Preference(String name, Map<String, OperandLiteral> entries) {
		this.name = name;
		this.preferences = new HashMap<String, OperandLiteral>();
		this.setPreferences(entries);
		jsonObj = null;
	}
	
	public Preference(String name, JSONObject json) {
		this.name = name;
		this.preferences = new HashMap<String, OperandLiteral>();
		setJSONObject(json);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, OperandLiteral> getPreferences() {
		return preferences;
	}

	public void setPreferences(Map<String, OperandLiteral> entries) {
		this.preferences = entries;
	}

	public JSONObject getJSONObject() {
		return toJSON();
	}

	public void setJSONObject(JSONObject json) {
		JSONObject jsonPreference = json.getJSONObject("preferences");
		String[] fields = JSONObject.getNames(jsonPreference);
		
		for(int i = 0 ; i < fields.length; i++) {
			boolean found = false;
			OperandLiteral entry = null;
			
			// try convert to string
			if(!found)
			try {
					String obj = jsonPreference.getString(fields[i]);
					if(fields[i].equalsIgnoreCase("fontColor") || 
							fields[i].equalsIgnoreCase("backgroundColor")) 
					{
						entry = new ColorLiteral(obj);
						
					} else if (fields[i].equalsIgnoreCase("language_subtitles") || 
								 fields[i].equalsIgnoreCase("language_sign") || 
									fields[i].equalsIgnoreCase("language_audio")) 
					{
						entry = new LanguageLiteral(obj);
						
					} else 
					{
						entry = new StringLiteral(obj);
					}
					found = true;
			} catch (JSONException e) {}
			
			// try convert to number
			if(!found)
			try {
				Number obj = jsonPreference.getNumber(fields[i]);
				entry = new NumericLiteral(obj);
				found = true;
			} catch (JSONException e) {}
			
			// try convert to boolean
			if(!found)
			try {
				boolean obj = jsonPreference.getBoolean(fields[i]);
				entry = new BooleanLiteral(obj);
				found = true;
			} catch (JSONException e) {}
			
			preferences.put(fields[i], entry);
		}
		this.jsonObj = json;
	}
	
	public JSONObject toJSON() {
		if(jsonObj == null) {
			jsonObj = new JSONObject();
			JSONObject jsonPreferences = new JSONObject();
			Iterator<java.util.Map.Entry<String, OperandLiteral>> interator = preferences.entrySet().iterator();
			while(interator.hasNext()) {
				Map.Entry<String, OperandLiteral> entry = interator.next();
				jsonPreferences.put(entry.getKey(), entry.getValue().getValue());
			}
			jsonObj.put("preferences", jsonPreferences);
		}
		return jsonObj;
	}
	
	public double distanceTo(Preference other) {
		double dist = 0;
		Iterator<java.util.Map.Entry<String, OperandLiteral>> iter1 = preferences.entrySet().iterator();
		while(iter1.hasNext()) {
			OperandLiteral otherOperand = null;
			java.util.Map.Entry<String, OperandLiteral> entry = iter1.next();
			//TO-DO
			if((otherOperand = other.getPreferences().get(entry.getKey())) != null) {
				dist += entry.getValue().distanceTo(otherOperand);
			}   
		}
		return  dist ;
	}

	public double[] getPoint() {
		double[] points = new double[PREFERENCE_ATTRIBUTE.length + 4];
		int index = 0;
		for(int i = 0 ; i < PREFERENCE_ATTRIBUTE.length - 2; i++) {
			OperandLiteral operand = preferences.get(PREFERENCE_ATTRIBUTE[i]);
			if(operand == null) {
				points[index++] = 0.0;
			} else {
				//TO-DO get all the operand values
				points[index++] = operand.getPoint()[0];
			}
		}
		
		//Add color dimensions 
		for(int i = PREFERENCE_ATTRIBUTE.length - 2 ; i < PREFERENCE_ATTRIBUTE.length; i++) {
			OperandLiteral operand = preferences.get(PREFERENCE_ATTRIBUTE[i]);
			if(operand == null) {
				points[index++] = 0.0; 
				points[index++] = 0.0; 
				points[index++] = 0.0;
			} else {
				//TO-DO get all the operand values
				points[index++] = operand.getPoint()[0];
				points[index++] = operand.getPoint()[1];
				points[index++] = operand.getPoint()[2];
			}
		}
		
		return points;
	}
	
}
