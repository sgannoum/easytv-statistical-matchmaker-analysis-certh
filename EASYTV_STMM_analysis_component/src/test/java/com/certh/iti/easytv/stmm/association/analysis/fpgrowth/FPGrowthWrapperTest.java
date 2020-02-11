package com.certh.iti.easytv.stmm.association.analysis.fpgrowth;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import org.json.JSONObject;
import org.testng.annotations.Test;

import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;
import com.certh.iti.easytv.user.preference.Preference;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.Attribute.Bin;

import junit.framework.Assert;

import com.certh.iti.easytv.user.preference.attributes.DoubleAttribute;
import com.certh.iti.easytv.user.preference.attributes.IntegerAttribute;
import com.certh.iti.easytv.user.preference.attributes.NominalAttribute;
import com.certh.iti.easytv.user.preference.attributes.OrdinalAttribute;
import com.certh.iti.easytv.user.preference.attributes.SymmetricBinaryAttribute;

public class FPGrowthWrapperTest {
		
	private JSONObject profile_0 = new JSONObject("{" + 
			"    \"user_content\": {" + 
			"        \"http://registry.easytv.eu/application/cs/audio/track\": [\"ca\",\"es\"]," + 
			"        \"http://registry.easytv.eu/application/cs/accessibility/detection/text\": true," + 
			"        \"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": true," + 
			"        \"http://registry.easytv.eu/application/cs/cc/subtitles/language\": [\"ca\",\"gr\",\"it\"]," + 
			"        \"http://registry.easytv.eu/application/cs/accessibility/detection/face\": true," + 
			"        \"http://registry.easytv.eu/application/cs/accessibility/detection/character\": true" + 
			"    }," + 
			"    \"user_id\": 0," + 
			"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
			"		\"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": false," + 
			"		\"http://registry.easytv.eu/common/volume\": 0," + 
			"		\"http://registry.easytv.eu/application/tts/audio/voice\": \"female\"," + 
			"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"23\"," + 
			"		\"http://registry.easytv.eu/application/cs/accessibility/magnification/scale\": 3.5" + 
			"		" + 
			"    }}}}," + 
			"    \"user_context\": {" + 
			"        \"http://registry.easytv.eu/context/device\": \"tablet\"," + 
			"        \"http://registry.easytv.eu/context/light\": 30," + 
			"        \"http://registry.easytv.eu/context/location\": \"es\"," + 
			"        \"http://registry.easytv.eu/context/proximity\": 98," + 
			"        \"http://registry.easytv.eu/context/time\": \"10:16:52\"" + 
			"    }" + 
			"}");

	/**
	 * Check that with no bining preference information remains 
	 * @throws IOException
	 * @throws UserProfileParsingException
	 */
	@Test
	public void test_profiles_itemset_no_bining() throws IOException, UserProfileParsingException {
		
		LinkedHashMap<String, Attribute> preferencesAttributes  =  new LinkedHashMap<String, Attribute>() {
			private static final long serialVersionUID = 1L;
		{
			put("http://registry.easytv.eu/application/cs/accessibility/detection/sound",  new SymmetricBinaryAttribute());
		    put("http://registry.easytv.eu/application/tts/audio/voice", new NominalAttribute(new String[] {"male", "female"}));
			put("http://registry.easytv.eu/common/volume", new IntegerAttribute(new double[] {0.0, 100.0}, 1.0, 0));
		    put("http://registry.easytv.eu/application/cs/ui/text/size", new OrdinalAttribute(new String[] {"15", "20", "23"}));					
		    put("http://registry.easytv.eu/application/cs/accessibility/magnification/scale", new DoubleAttribute(new double[] {1.5, 3.5}, 0.5, 0));
	    }};
	    
		//set new preferences attributes
	    Preference.setAttributes(preferencesAttributes);
	    Vector<Bin> bins = Profile.getBins();
		
		
		JSONObject expected = new JSONObject("{" + 
				"		\"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": false," + 
				"		\"http://registry.easytv.eu/common/volume\": 0," + 
				"		\"http://registry.easytv.eu/application/tts/audio/voice\": \"female\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"23\"," + 
				"		\"http://registry.easytv.eu/application/cs/accessibility/magnification/scale\": 3.5" + 
				"}");
		
		Profile profile = new Profile(profile_0);
		for(int i : profile.getAsItemSet()) {
			Bin bin = bins.get(i);
			StringTokenizer preference = new StringTokenizer(bin.label, " -");
			String uri = preference.nextToken();
			Assert.assertEquals(uri, expected.get(uri), bin.center);
		}
		
	}
	
	@Test
	public void test_profiles_itemset_bining() throws IOException, UserProfileParsingException {
		
		LinkedHashMap<String, Attribute> preferencesAttributes  =  new LinkedHashMap<String, Attribute>() {
			private static final long serialVersionUID = 1L;
		{
			put("http://registry.easytv.eu/application/cs/accessibility/detection/sound",  new SymmetricBinaryAttribute());
		    put("http://registry.easytv.eu/application/tts/audio/voice", new NominalAttribute(new String[] {"male", "female"}));
			put("http://registry.easytv.eu/common/volume", new IntegerAttribute(new double[] {0.0, 100.0}, 1.0, 25, 0));
		    put("http://registry.easytv.eu/application/cs/ui/text/size", new OrdinalAttribute(new String[] {"15", "20", "23"}));					
		    put("http://registry.easytv.eu/application/cs/accessibility/magnification/scale", new DoubleAttribute(new double[] {1.5, 3.5}, 0.5, 0));
	    }};
	    
		//set new preferences attributes
	    Preference.setAttributes(preferencesAttributes);
	    Vector<Bin> bins = Profile.getBins();
		
		
		JSONObject expected = new JSONObject("{" + 
				"		\"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": false," + 
				"		\"http://registry.easytv.eu/common/volume\": 2," + 
				"		\"http://registry.easytv.eu/application/tts/audio/voice\": \"female\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"23\"," + 
				"		\"http://registry.easytv.eu/application/cs/accessibility/magnification/scale\": 3.5" + 
				"}");
		
		Profile profile = new Profile(profile_0);
		for(int i : profile.getAsItemSet()) {
			Bin bin = bins.get(i);
			StringTokenizer preference = new StringTokenizer(bin.label, " -");
			String uri = preference.nextToken();
			Assert.assertEquals(uri, expected.get(uri), bin.center);
		}
		
	}
	
	/**
	 * Check that with no bining preference information remains 
	 * @throws IOException
	 * @throws UserProfileParsingException
	 */
	@Test
	public void test_profiles_itemset_no_bining_extract_attribute() throws IOException, UserProfileParsingException {
		
		LinkedHashMap<String, Attribute> preferencesAttributes  =  new LinkedHashMap<String, Attribute>() {
			private static final long serialVersionUID = 1L;
		{
			put("http://registry.easytv.eu/application/cs/accessibility/detection/sound",  new SymmetricBinaryAttribute());
		    put("http://registry.easytv.eu/application/tts/audio/voice", new NominalAttribute(new String[] {"male", "female"}));
			put("http://registry.easytv.eu/common/contrast", new IntegerAttribute(new double[] {0.0, 100.0}, 1.0, 25, 0));
			put("http://registry.easytv.eu/common/volume", new IntegerAttribute(new double[] {0.0, 100.0}, 1.0, 0));
		    put("http://registry.easytv.eu/application/cs/ui/text/size", new OrdinalAttribute(new String[] {"15", "20", "23"}));					
		    put("http://registry.easytv.eu/application/cs/accessibility/magnification/scale", new DoubleAttribute(new double[] {1.5, 3.5}, 0.5, 0));
	    }};
	    
		//set new preferences attributes
	    Preference.setAttributes(preferencesAttributes);
	    Vector<Bin> bins = Profile.getBins();
		
		
		JSONObject expected = new JSONObject("{" + 
				"		\"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": false," + 
				"		\"http://registry.easytv.eu/common/volume\": 0," + 
				"		\"http://registry.easytv.eu/application/tts/audio/voice\": \"female\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"23\"," + 
				"		\"http://registry.easytv.eu/application/cs/accessibility/magnification/scale\": 3.5" + 
				"}");
		
		Profile profile = new Profile(profile_0);
		for(int i : profile.getAsItemSet()) {
			Bin bin = bins.get(i);
			StringTokenizer preference = new StringTokenizer(bin.label, " -");
			String uri = preference.nextToken();
			Assert.assertEquals(uri, expected.get(uri), bin.center);
		}
		
	}

}
