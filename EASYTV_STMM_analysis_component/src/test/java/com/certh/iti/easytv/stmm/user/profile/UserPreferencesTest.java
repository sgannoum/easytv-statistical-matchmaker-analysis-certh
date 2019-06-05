package com.certh.iti.easytv.stmm.user.profile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.certh.iti.easytv.config.Config;

public class UserPreferencesTest {

	private JSONObject json;
	
	@BeforeClass
	public void beforClass() throws IOException {
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(Config.path)));
		StringBuffer buff = new StringBuffer();
		
		while((line = reader.readLine()) != null) {
			buff.append(line);
		}
		
		json = new JSONObject(buff.toString());		
		reader.close();
	}
	
	@Test
	public void test_constructor() {
		UserPreferences userPreferences = new UserPreferences(json.getJSONObject("user_preferences"));
		UserPreferences userPreferences2 = new UserPreferences(userPreferences.getDefaultPreference(), userPreferences.getConditionalPreferences());
			
		Assert.assertTrue(userPreferences.getJSONObject().similar(json.getJSONObject("user_preferences")));
	}
	
	
}
