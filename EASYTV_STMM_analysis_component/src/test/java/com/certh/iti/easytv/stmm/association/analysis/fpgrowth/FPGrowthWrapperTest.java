package com.certh.iti.easytv.stmm.association.analysis.fpgrowth;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.json.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.AttributesAggregator;
import com.certh.iti.easytv.user.preference.attributes.AttributesAggregator.Association;
import com.certh.iti.easytv.user.preference.attributes.DoubleAttribute;
import com.certh.iti.easytv.user.preference.attributes.IntegerAttribute;
import com.certh.iti.easytv.user.preference.attributes.NominalAttribute;
import com.certh.iti.easytv.user.preference.attributes.OrdinalAttribute;
import com.certh.iti.easytv.user.preference.attributes.SymmetricBinaryAttribute;
import com.certh.iti.easytv.user.preference.attributes.discretization.Discrete;

import junit.framework.Assert;

public class FPGrowthWrapperTest {

	private AttributesAggregator aggregator;
	
	private JSONObject profile_0 = new JSONObject("{" + 
			"    \"user_id\": 0," + 
			"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
			"		\"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": false," + 
			"		\"http://registry.easytv.eu/common/volume\": 0," + 
			"		\"http://registry.easytv.eu/application/tts/audio/voice\": \"female\"," + 
			"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"23\"," + 
			"		\"http://registry.easytv.eu/application/cs/accessibility/magnification/scale\": 3.5" + 
			"		" + 
			"    }}}}" + 
			"}");
	

	@BeforeClass
	public void beforeclass() {
		aggregator = Profile.getAggregator();
	}
	
	@AfterClass
	public void afterclass() {
		Profile.setAggregator(aggregator);
	}
	
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
	    AttributesAggregator aggregator = new AttributesAggregator();
	    aggregator.add(preferencesAttributes);
	    Profile.setAggregator(aggregator);
		
		JSONObject expected = new JSONObject("{" + 
				"		\"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": false," + 
				"		\"http://registry.easytv.eu/common/volume\": 0," + 
				"		\"http://registry.easytv.eu/application/tts/audio/voice\": \"female\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"23\"," + 
				"		\"http://registry.easytv.eu/application/cs/accessibility/magnification/scale\": 3.5" + 
				"}");
		
		Profile profile = new Profile(profile_0);
		for(int i : profile.getAsItemSet()) {
			Association<String, Discrete> assotiation = aggregator.decode(i);
			String uri = assotiation.getUri();
			Assert.assertEquals(uri, expected.get(uri), assotiation.getValue().getCenter());
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
	    
	    AttributesAggregator aggregator = new AttributesAggregator();
	    aggregator.add(preferencesAttributes);
	    Profile.setAggregator(aggregator);
		
		JSONObject expected = new JSONObject("{" + 
				"		\"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": false," + 
				"		\"http://registry.easytv.eu/common/volume\": 2," + 
				"		\"http://registry.easytv.eu/application/tts/audio/voice\": \"female\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"23\"," + 
				"		\"http://registry.easytv.eu/application/cs/accessibility/magnification/scale\": 3.5" + 
				"}");
		
		Profile profile = new Profile(profile_0);
		for(int i : profile.getAsItemSet()) {
			Association<String, Discrete> assotiation = aggregator.decode(i);
			String uri = assotiation.getUri();
			Assert.assertEquals(uri, expected.get(uri), assotiation.getValue().getCenter());
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
	    
	    AttributesAggregator aggregator = new AttributesAggregator();
	    aggregator.add(preferencesAttributes);
	    Profile.setAggregator(aggregator);
	    
		JSONObject expected = new JSONObject("{" + 
				"		\"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": false," + 
				"		\"http://registry.easytv.eu/common/volume\": 0," + 
				"		\"http://registry.easytv.eu/application/tts/audio/voice\": \"female\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"23\"," + 
				"		\"http://registry.easytv.eu/application/cs/accessibility/magnification/scale\": 3.5" + 
				"}");
		
		Profile profile = new Profile(profile_0);
		for(int i : profile.getAsItemSet()) {
			Association<String, Discrete> assotiation = aggregator.decode(i);
			String uri = assotiation.getUri();
			Assert.assertEquals(uri, expected.get(uri), assotiation.getValue().getCenter());
		}
	}

}
