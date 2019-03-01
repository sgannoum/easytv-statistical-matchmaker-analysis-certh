package com.certh.iti.easytv.stmm.user.profile.preference;

import java.lang.reflect.InvocationTargetException;
import java.nio.channels.IllegalSelectorException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONException;
import org.json.JSONObject;

import com.certh.iti.easytv.stmm.user.profile.preference.entry.Entry;
import com.certh.iti.easytv.stmm.user.profile.preference.entry.LanguageEntry;
import com.certh.iti.easytv.stmm.user.profile.preference.entry.NumericEntry;
import com.certh.iti.easytv.stmm.user.profile.preference.entry.StringEntry;
import com.certh.iti.easytv.stmm.generator.preference.operand.RandomBooleanLiteral;
import com.certh.iti.easytv.stmm.generator.preference.operand.RandomColorLiteral;
import com.certh.iti.easytv.stmm.generator.preference.operand.RandomDisplayContrastLiteral;
import com.certh.iti.easytv.stmm.generator.preference.operand.RandomImageMagnificationScaleLiteral;
import com.certh.iti.easytv.stmm.generator.preference.operand.RandomIntLiteral;
import com.certh.iti.easytv.stmm.generator.preference.operand.RandomLanguageLiteral;
import com.certh.iti.easytv.stmm.generator.preference.operand.RandomTTSQualityLiteral;
import com.certh.iti.easytv.stmm.generator.preference.operand.RandomTTSSpeed;
import com.certh.iti.easytv.stmm.generator.preference.operand.RandomTTSVolume;
import com.certh.iti.easytv.stmm.user.profile.preference.condition.operand.BooleanLiteral;
import com.certh.iti.easytv.stmm.user.profile.preference.condition.operand.ColorLiteral;
import com.certh.iti.easytv.stmm.user.profile.preference.condition.operand.FontLiteral;
import com.certh.iti.easytv.stmm.user.profile.preference.condition.operand.LanguageLiteral;
import com.certh.iti.easytv.stmm.user.profile.preference.condition.operand.NumericLiteral;
import com.certh.iti.easytv.stmm.user.profile.preference.condition.operand.OperandLiteral;
import com.certh.iti.easytv.stmm.user.profile.preference.condition.operand.StringLiteral;
import com.certh.iti.easytv.stmm.user.profile.preference.entry.BooleanEntry;
import com.certh.iti.easytv.stmm.user.profile.preference.entry.ColorEntry;

public class Preference implements Clusterable {

	
	protected static final String common_pefix = "https://easytvproject.eu/registry/common/";
	protected static final String application_pefix = "https://easytvproject.eu/registry/application/";
	
	public static final String[] PREFERENCE_ATTRIBUTE = { 
			common_pefix + "audioVolume",
			common_pefix + "audioLanguage",
			common_pefix + "displayContrast",
			common_pefix + "fontSize",
			common_pefix + "font", 
			common_pefix + "subtitles",
			common_pefix + "signLanguage", 
			application_pefix + "tts/speed", 
			application_pefix + "tts/volume", 
			application_pefix + "tts/language", 
			application_pefix + "tts/audioQuality",
			application_pefix + "cs/accessibility/imageMagnification/scale",
			application_pefix + "cs/accessibility/textDetection", 
			application_pefix + "cs/accessibility/faceDetection", 
			application_pefix + "cs/audio/volume", 
			application_pefix + "cs/audio/track", 
			application_pefix + "cs/audio/audioDescription", 
			application_pefix + "cs/cc/audioSubtitles", 
			application_pefix + "cs/cc/subtitles/language", 
			application_pefix + "cs/cc/subtitles/fontSize",
			application_pefix + "cs/cc/subtitles/fontColor",
			application_pefix + "cs/cc/subtitles/backgroundColor",
			common_pefix + "fontColor", 
			common_pefix + "backgroundColor"};
	
	protected static final OperandLiteral[] PREFERENCE_CLASSES = {  
			  new NumericLiteral(1),	 		//"audioVolume",
			  new LanguageLiteral("english"),	 	//"audioLanguage",
			  new NumericLiteral(1), 	 	//"displayContrast",
			  new NumericLiteral(1),	 		//"fontSize",
			  new FontLiteral("fantasy"), 			//font 
			  new LanguageLiteral("english"),   		//"subtitles",
			  new LanguageLiteral("english"),    	//"signLanguage", 
			  
			  new NumericLiteral(1),   		//"tts/speed", 
			  new NumericLiteral(1),   		//"tts/volume", 
			  new LanguageLiteral("english"),  		//"tts/language", 
			  new NumericLiteral(1),  		//"tts/audioQuality",
			  
			  new NumericLiteral(1),   		//"cs/accessibility/imageMagnification/scale",
			  new BooleanLiteral(true),   		//"cs/accessibility/textDetection", 
			  new BooleanLiteral(true),   		//"cs/accessibility/faceDetection", 
			  
			  new NumericLiteral(1),   		//"cs/audio/volume", 
			  new LanguageLiteral("english"),  		//"cs/audio/track", 
			  new BooleanLiteral(true),  		//"cs/audio/audioDescription", 
			  
			  new BooleanLiteral(true),   		//"cs/cc/audioSubtitles", 
			  new LanguageLiteral("english"), 		//"cs/cc/subtitles/language", 
			  new NumericLiteral(1),  	   	//"cs/cc/subtitles/fontSize",
			  
			  new ColorLiteral("#ffffff"),   		//"cs/cc/subtitles/fontColor", 
			  new ColorLiteral("#ffffff"),    		//"cs/cc/subtitles/backgroundColor"
			  new ColorLiteral("#ffffff"),   		//"fontColor", 
			  new ColorLiteral("#ffffff")    		//"backgroundColor"
			  };
	
	
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
			System.out.println(fields[i]);
			OperandLiteral instance = getLiteralClass(fields[i]);
			
			if(instance == null)
				throw new IllegalStateException("Unknown preference type");
			
			preferences.put(fields[i], (OperandLiteral) instance.createFromJson(jsonPreference, fields[i]));
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
		double[] points = new double[PREFERENCE_ATTRIBUTE.length + 8];
		int index = 0;
		for(int i = 0 ; i < PREFERENCE_ATTRIBUTE.length - 4; i++) {
			OperandLiteral operand = preferences.get(PREFERENCE_ATTRIBUTE[i]);
			if(operand == null) {
				points[index++] = 0.0;
			} else {
				//TO-DO get all the operand values
				points[index++] = operand.getPoint()[0];
			}
		}
		
		//Add color dimensions 
		for(int i = PREFERENCE_ATTRIBUTE.length - 4 ; i < PREFERENCE_ATTRIBUTE.length; i++) {
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
	
	private OperandLiteral getLiteralClass(String field) {
		for(int i = 0; i < PREFERENCE_ATTRIBUTE.length; i++) {
			if(field.equalsIgnoreCase(PREFERENCE_ATTRIBUTE[i])) {
				return PREFERENCE_CLASSES[i];
			}
		}
		return null;
	}
	
}
