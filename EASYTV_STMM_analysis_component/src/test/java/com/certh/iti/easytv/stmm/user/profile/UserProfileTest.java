package com.certh.iti.easytv.stmm.user.profile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UserProfileTest {
	
	private final String path = "C:\\Users\\salgan\\git\\EASYTV_STMM_analysis_component\\EASYTV_STMM_analysis_component\\target\\test-classes\\testing_profiles\\userModel.json";
	private JSONObject json;
	
	@BeforeClass
	public void beforClass() throws IOException {
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		StringBuffer buff = new StringBuffer();
		
		while((line = reader.readLine()) != null) {
			buff.append(line);
		}
		
		json = new JSONObject(buff.toString());		
		reader.close();
	}
	
	@Test
	public void test_constructor() throws IOException {
		UserProfile userProfile1 = new UserProfile(json);
		UserProfile userProfile2 = new UserProfile(userProfile1.getGeneral(), userProfile1.getVisualCapabilities(), userProfile1.getAuditoryCapabilities(), userProfile1.getUserPreferences(), false);
	
		Assert.assertTrue(userProfile2.getJSONObject().similar(json));
	}
	
	@Test
	public void test_identifcal_profiles() throws IOException {
		
		JSONObject jsonProfile1 = new JSONObject("{\r\n" + 
				"  \"general\": {\r\n" + 
				"    \"age\": 40,\r\n" + 
				"    \"gender\": \"male\"\r\n" + 
				"  },\r\n" + 
				"  \"visual\": {\r\n" + 
				"    \"visual_acuity\": 8,\r\n" + 
				"    \"contrast_sensitivity\": 24,\r\n" + 
				"    \"color_blindness\": \"normal\"\r\n" + 
				"  },\r\n" + 
				"  \"auditory\": {\r\n" + 
				"    \"quarterK\": 81,\r\n" + 
				"    \"halfK\": 35,\r\n" + 
				"    \"oneK\": 98,\r\n" + 
				"    \"twoK\": 18,\r\n" + 
				"    \"fourK\": 57,\r\n" + 
				"    \"eightK\": 27\r\n" + 
				"  },\r\n" + 
				"  \"user_preferences\": {\r\n" + 
				"    \"default\": {\r\n" + 
				"      \"preferences\": {\r\n" + 
				"        \"contrast\": 100,\r\n" + 
				"        \"font_size\": 20,\r\n" + 
				"        \"color_temperature\": 0.008,\r\n" + 
				"        \"language_subtitles\": \"spanish\",\r\n" + 
				"        \"language_sign\": \"spanish\",\r\n" + 
				"        \"language_audio\": \"spanish\",\r\n" + 
				"        \"backgroundColor\": \"#000000\"\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}");
		
		JSONObject jsonProfile2 = new JSONObject("{\r\n" + 
				"  \"general\": {\r\n" + 
				"    \"age\": 40,\r\n" + 
				"    \"gender\": \"male\"\r\n" + 
				"  },\r\n" + 
				"  \"visual\": {\r\n" + 
				"    \"visual_acuity\": 8,\r\n" + 
				"    \"contrast_sensitivity\": 24,\r\n" + 
				"    \"color_blindness\": \"normal\"\r\n" + 
				"  },\r\n" + 
				"  \"auditory\": {\r\n" + 
				"    \"quarterK\": 81,\r\n" + 
				"    \"halfK\": 35,\r\n" + 
				"    \"oneK\": 98,\r\n" + 
				"    \"twoK\": 18,\r\n" + 
				"    \"fourK\": 57,\r\n" + 
				"    \"eightK\": 27\r\n" + 
				"  },\r\n" + 
				"  \"user_preferences\": {\r\n" + 
				"    \"default\": {\r\n" + 
				"      \"preferences\": {\r\n" + 
				"        \"contrast\": 100,\r\n" + 
				"        \"font_size\": 20,\r\n" + 
				"        \"color_temperature\": 0.008,\r\n" + 
				"        \"language_subtitles\": \"spanish\",\r\n" + 
				"        \"language_sign\": \"spanish\",\r\n" + 
				"        \"language_audio\": \"spanish\",\r\n" + 
				"        \"backgroundColor\": \"#000000\"\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}");
		
		UserProfile userProfile1 = new UserProfile(jsonProfile1);
		UserProfile userProfile2 = new UserProfile(jsonProfile2);
	
		Assert.assertEquals(userProfile1.distanceTo(userProfile2), 0.0);
	}
	
	@Test
	public void test_profiles_similarety_color_difference() throws IOException {
		
		JSONObject jsonProfile1 = new JSONObject("{\r\n" + 
				"  \"general\": {\r\n" + 
				"    \"age\": 40,\r\n" + 
				"    \"gender\": \"male\"\r\n" + 
				"  },\r\n" + 
				"  \"visual\": {\r\n" + 
				"    \"visual_acuity\": 8,\r\n" + 
				"    \"contrast_sensitivity\": 24,\r\n" + 
				"    \"color_blindness\": \"normal\"\r\n" + 
				"  },\r\n" + 
				"  \"auditory\": {\r\n" + 
				"    \"quarterK\": 81,\r\n" + 
				"    \"halfK\": 35,\r\n" + 
				"    \"oneK\": 98,\r\n" + 
				"    \"twoK\": 18,\r\n" + 
				"    \"fourK\": 57,\r\n" + 
				"    \"eightK\": 27\r\n" + 
				"  },\r\n" + 
				"  \"user_preferences\": {\r\n" + 
				"    \"default\": {\r\n" + 
				"      \"preferences\": {\r\n" + 
				"        \"contrast\": 100,\r\n" + 
				"        \"font_size\": 20,\r\n" + 
				"        \"color_temperature\": 0.008,\r\n" + 
				"        \"language_subtitles\": \"spanish\",\r\n" + 
				"        \"language_sign\": \"spanish\",\r\n" + 
				"        \"language_audio\": \"spanish\",\r\n" + 
				"        \"backgroundColor\": \"#000000\"\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}");
		
		JSONObject jsonProfile2 = new JSONObject("{\r\n" + 
				"  \"general\": {\r\n" + 
				"    \"age\": 40,\r\n" + 
				"    \"gender\": \"male\"\r\n" + 
				"  },\r\n" + 
				"  \"visual\": {\r\n" + 
				"    \"visual_acuity\": 8,\r\n" + 
				"    \"contrast_sensitivity\": 24,\r\n" + 
				"    \"color_blindness\": \"normal\"\r\n" + 
				"  },\r\n" + 
				"  \"auditory\": {\r\n" + 
				"    \"quarterK\": 81,\r\n" + 
				"    \"halfK\": 35,\r\n" + 
				"    \"oneK\": 98,\r\n" + 
				"    \"twoK\": 18,\r\n" + 
				"    \"fourK\": 57,\r\n" + 
				"    \"eightK\": 27\r\n" + 
				"  },\r\n" + 
				"  \"user_preferences\": {\r\n" + 
				"    \"default\": {\r\n" + 
				"      \"preferences\": {\r\n" + 
				"        \"contrast\": 100,\r\n" + 
				"        \"font_size\": 20,\r\n" + 
				"        \"color_temperature\": 0.008,\r\n" + 
				"        \"language_subtitles\": \"spanish\",\r\n" + 
				"        \"language_sign\": \"spanish\",\r\n" + 
				"        \"language_audio\": \"spanish\",\r\n" + 
				"        \"backgroundColor\": \"#080000\"\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}");
		
		UserProfile userProfile1 = new UserProfile(jsonProfile1);
		UserProfile userProfile2 = new UserProfile(jsonProfile2);
	
		Assert.assertEquals(userProfile1.distanceTo(userProfile2), 8.0);
	}
	
	@Test
	public void test_profiles_similarety_language_difference() throws IOException {
		
		JSONObject jsonProfile1 = new JSONObject("{\r\n" + 
				"  \"general\": {\r\n" + 
				"    \"age\": 40,\r\n" + 
				"    \"gender\": \"male\"\r\n" + 
				"  },\r\n" + 
				"  \"visual\": {\r\n" + 
				"    \"visual_acuity\": 8,\r\n" + 
				"    \"contrast_sensitivity\": 24,\r\n" + 
				"    \"color_blindness\": \"normal\"\r\n" + 
				"  },\r\n" + 
				"  \"auditory\": {\r\n" + 
				"    \"quarterK\": 81,\r\n" + 
				"    \"halfK\": 35,\r\n" + 
				"    \"oneK\": 98,\r\n" + 
				"    \"twoK\": 18,\r\n" + 
				"    \"fourK\": 57,\r\n" + 
				"    \"eightK\": 27\r\n" + 
				"  },\r\n" + 
				"  \"user_preferences\": {\r\n" + 
				"    \"default\": {\r\n" + 
				"      \"preferences\": {\r\n" + 
				"        \"contrast\": 100,\r\n" + 
				"        \"font_size\": 20,\r\n" + 
				"        \"color_temperature\": 0.008,\r\n" + 
				"        \"language_subtitles\": \"spanish\",\r\n" + 
				"        \"language_sign\": \"spanish\",\r\n" + 
				"        \"language_audio\": \"spanish\",\r\n" + 
				"        \"backgroundColor\": \"#000000\"\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}");
		
		JSONObject jsonProfile2 = new JSONObject("{\r\n" + 
				"  \"general\": {\r\n" + 
				"    \"age\": 40,\r\n" + 
				"    \"gender\": \"male\"\r\n" + 
				"  },\r\n" + 
				"  \"visual\": {\r\n" + 
				"    \"visual_acuity\": 8,\r\n" + 
				"    \"contrast_sensitivity\": 24,\r\n" + 
				"    \"color_blindness\": \"normal\"\r\n" + 
				"  },\r\n" + 
				"  \"auditory\": {\r\n" + 
				"    \"quarterK\": 81,\r\n" + 
				"    \"halfK\": 35,\r\n" + 
				"    \"oneK\": 98,\r\n" + 
				"    \"twoK\": 18,\r\n" + 
				"    \"fourK\": 57,\r\n" + 
				"    \"eightK\": 27\r\n" + 
				"  },\r\n" + 
				"  \"user_preferences\": {\r\n" + 
				"    \"default\": {\r\n" + 
				"      \"preferences\": {\r\n" + 
				"        \"contrast\": 100,\r\n" + 
				"        \"font_size\": 20,\r\n" + 
				"        \"color_temperature\": 0.008,\r\n" + 
				"        \"language_subtitles\": \"spanish\",\r\n" + 
				"        \"language_sign\": \"spanish\",\r\n" + 
				"        \"language_audio\": \"greek\",\r\n" + 
				"        \"backgroundColor\": \"#000000\"\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}");
		
		UserProfile userProfile1 = new UserProfile(jsonProfile1);
		UserProfile userProfile2 = new UserProfile(jsonProfile2);
	
		Assert.assertEquals(userProfile1.distanceTo(userProfile2), 4.0);
	}
	
	
	@Test
	public void test_getPoints() throws IOException {
		
		JSONObject jsonProfile1 = new JSONObject("{\r\n" + 
				"  \"general\": {\r\n" + 
				"    \"age\": 40,\r\n" + 
				"    \"gender\": \"male\"\r\n" + 
				"  },\r\n" + 
				"  \"visual\": {\r\n" + 
				"    \"visual_acuity\": 8,\r\n" + 
				"    \"contrast_sensitivity\": 24,\r\n" + 
				"    \"color_blindness\": \"normal\"\r\n" + 
				"  },\r\n" + 
				"  \"auditory\": {\r\n" + 
				"    \"quarterK\": 81,\r\n" + 
				"    \"halfK\": 35,\r\n" + 
				"    \"oneK\": 98,\r\n" + 
				"    \"twoK\": 18,\r\n" + 
				"    \"fourK\": 57,\r\n" + 
				"    \"eightK\": 27\r\n" + 
				"  },\r\n" + 
				"  \"user_preferences\": {\r\n" + 
				"    \"default\": {\r\n" + 
				"      \"preferences\": {\r\n" + 
				"        \"contrast\": 100,\r\n" + 
				"        \"font_size\": 20,\r\n" + 
				"        \"color_temperature\": 0.008,\r\n" + 
				"        \"language_subtitles\": \"spanish\",\r\n" + 
				"        \"language_sign\": \"spanish\",\r\n" + 
				"        \"language_audio\": \"spanish\",\r\n" +
				"        \"fontColor\": \"#030201\",\r\n" + 
				"        \"backgroundColor\": \"#060504\"\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}");
		
		UserProfile userProfile1 = new UserProfile(jsonProfile1);
		double[] expectedPoints = {40, 0, 8, 24, 0, 81, 35, 98, 18, 57, 27, 
									0, 1, 100, 20, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,1,2 ,6,4,5 };
		
		double[] actualPoints = userProfile1.getPoint();
		
		Assert.assertEquals(actualPoints.length, expectedPoints.length);
		
		for(int i = 0 ; i < actualPoints.length; i++)
			Assert.assertEquals(actualPoints[i], expectedPoints[i]); 

	}

}
