package com.certh.iti.easytv.stmm.user.profile.preference;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PreferenceTests {
	
	//private final String path = "C:\\Users\\salgan\\git\\EASYTV_STMM_analysis_component\\EASYTV_STMM_analysis_component\\target\\test-classes\\testing_profiles\\userModel.json";
	private final String path = "C:\\Users\\salgan\\git\\EASYTV_STMM_analysis_component\\EASYTV_STMM_analysis_component\\target\\test-classes\\testing_profiles\\userModel_url.json";

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
	public void test_preference() {
		JSONObject jsonExpected = json.getJSONObject("user_preferences").getJSONObject("default");

		//convert JSON preference to map
		Preference preference1 = new Preference("default", jsonExpected);
		
		Assert.assertTrue(preference1.getPreferences().size() > 0);
		
		//use it to create a new preference object
		Preference preference2 = new Preference("default", preference1.getPreferences());
		JSONObject jsonFound = preference2.toJSON();
		
		Assert.assertTrue(jsonFound.similar(jsonExpected), "\nExpected: "+jsonExpected.toString(4)+"\n Found: "+jsonFound.toString(4)+"\n");
	}
	
	

}
