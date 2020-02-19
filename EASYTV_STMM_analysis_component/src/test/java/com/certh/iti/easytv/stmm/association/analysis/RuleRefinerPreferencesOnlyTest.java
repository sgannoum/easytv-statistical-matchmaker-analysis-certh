package com.certh.iti.easytv.stmm.association.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.json.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.certh.iti.easytv.config.Config;
import com.certh.iti.easytv.stmm.association.analysis.rules.RbmmRuleWrapper;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.BodyRuleConditions;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.HeadRuleConditions;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.RuleCondition;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.RuleCondition.Argument;
import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.UserContent;
import com.certh.iti.easytv.user.UserContext;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;
import com.certh.iti.easytv.user.preference.Preference;
import com.certh.iti.easytv.user.preference.attributes.Attribute;

import junit.framework.Assert;

public class RuleRefinerPreferencesOnlyTest {

	private RuleRefiner ruleRefiner;
	private List<Profile> profiles = new ArrayList<Profile>();
	private Map<String, Attribute> initialAttributes, initialContextAttribute, initialContentAttribute;
	private Map<String, Attribute> contextAttributes  =  new LinkedHashMap<String, Attribute>();
	private Map<String, Attribute> contentAttributes  =  new LinkedHashMap<String, Attribute>();

	
	@BeforeClass
	public void beforeClass() throws IOException, UserProfileParsingException {
		initialAttributes = Preference.getAttributes();
		initialContextAttribute = UserContext.getAttributes();
		initialContentAttribute = UserContent.getAttributes();
		
		//change profiles attributes to use only context
		Profile.setAttributes(initialAttributes, contextAttributes, contentAttributes);
		
		// load profiles
		for (int i = 0; i < 9; i++) {
			System.out.println("load profile: " + "userProfile_" + i + ".json");
			JSONObject json = Config.getProfile("userProfile_" + i + ".json");
			
			json.remove("user_context");
			json.remove("user_content");

			profiles.add(new Profile(json));
		}

		ruleRefiner = new RuleRefiner(Profile.getBins());
	}
	 
	@AfterClass
	public void afterClass() {		
		Profile.setAttributes(initialAttributes, initialContextAttribute, initialContentAttribute);
	}


	/**
	 * Test that refineRules would return the proper collection of rules
	 */
	@Test
	public void test_refineRules_use_Case() {

		double minSupport = 0.5;
		double minConfidence = 0.5;

		// Rbmm rules
		// http://registry.easytv.eu/application/cs/accessibility/magnification/scale 1.0 
		// http://registry.easytv.eu/application/cs/ui/text/size 2.3 
		// ->
		// http://registry.easytv.eu/application/cs/audio/eq false
		RbmmRuleWrapper rb1 = new RbmmRuleWrapper(new JSONObject("{" + 
				"    \"head\": [{" + 
				"        \"args\": [{" + 
				"            \"xml-type\": \"http://www.w3.org/2001/XMLSchema#boolean\"," + 
				"            \"value\": false" + 
				"        }]," + 
				"        \"preference\": \"http://registry.easytv.eu/application/cs/audio/eq\"," + 
				"        \"builtin\": \"EQ\"" + 
				"    }]," + 
				"    \"body\": ["+
				"	 {" + 
				"        \"args\": [{" + 
				"            \"xml-type\": \"http://www.w3.org/2001/XMLSchema#double\"," + 
				"            \"value\": 1.0" + 
				"        }]," + 
				"        \"preference\": \"http://registry.easytv.eu/application/cs/accessibility/magnification/scale\"," + 
				"        \"builtin\": \"EQ\"" + 
				"    }," + 
				"	 {" + 
				"        \"args\": [{" + 
				"            \"xml-type\": \"http://www.w3.org/2001/XMLSchema#double\"," + 
				"            \"value\": 2.3" + 
				"        }]," + 
				"        \"preference\": \"http://registry.easytv.eu/application/cs/ui/text/size\"," + 
				"        \"builtin\": \"EQ\"" + 
				"    }]" + 
				"}"));

		// http://registry.easytv.eu/application/cs/accessibility/magnification/scale 3.5 
		// http://registry.easytv.eu/application/cs/audio/eq true
		// ->
		// http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size 1.3 
		RbmmRuleWrapper rb2 = new RbmmRuleWrapper(new JSONObject("{" + 
				"    \"head\": [{" + 
				"        \"args\": [{" + 
				"            \"xml-type\": \"http://www.w3.org/2001/XMLSchema#double\"," + 
				"            \"value\": 1.5" + 
				"        }]," + 
				"        \"preference\": \"http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size\"," + 
				"        \"builtin\": \"EQ\"" + 
				"    }]," + 
				"    \"body\": ["+
				"	 {" + 
				"        \"args\": [{" + 
				"            \"xml-type\": \"http://www.w3.org/2001/XMLSchema#boolean\"," + 
				"            \"value\": true" + 
				"        }]," + 
				"        \"preference\": \"http://registry.easytv.eu/application/cs/audio/eq\"," + 
				"        \"builtin\": \"EQ\"" + 
				"    }," + 
				"	 {" + 
				"        \"args\": [{" + 
				"            \"xml-type\": \"http://www.w3.org/2001/XMLSchema#double\"," + 
				"            \"value\": 3.5" + 
				"        }]," + 
				"        \"preference\": \"http://registry.easytv.eu/application/cs/accessibility/magnification/scale\"," + 
				"        \"builtin\": \"EQ\"" + 
				"    }]" + 
				"}"));

		Vector<RbmmRuleWrapper> rbmmRules = new Vector<RbmmRuleWrapper>();
		rbmmRules.add(rb1);
		rbmmRules.add(rb2);

		// http://registry.easytv.eu/common/volume (0,4) 
		// ->
		// http://registry.easytv.eu/application/cs/accessibility/detection/sound(true)
		RuleWrapper rl1 = new RuleWrapper(
				new BodyRuleConditions(new RuleCondition[] {
						new RuleCondition("http://registry.easytv.eu/common/volume", "GE",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 0) }),
						new RuleCondition("http://registry.easytv.eu/common/volume", "LE",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 4) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition(
								"http://registry.easytv.eu/application/cs/accessibility/detection/sound", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#boolean", true) }) },
						0, 0));

		// http://registry.easytv.eu/application/cs/ui/text/size(23) 
		// ->
		// http://registry.easytv.eu/application/cs/accessibility/magnification/scale(3.5)
		// http://registry.easytv.eu/application/cs/audio/eq(true)
		RuleWrapper rl2 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://registry.easytv.eu/application/cs/ui/text/size",
								"EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "23") }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] {
								new RuleCondition("http://registry.easytv.eu/application/cs/audio/eq", "EQ",
										new Argument[] {
												new Argument("http://www.w3.org/2001/XMLSchema#boolean", true) }),
								new RuleCondition(
										"http://registry.easytv.eu/application/cs/accessibility/magnification/scale",
										"EQ",
										new Argument[] {
												new Argument("http://www.w3.org/2001/XMLSchema#double", 3.5) }) },
						0, 0));

		// http://registry.easytv.eu/application/cs/accessibility/magnification/scale(3.5)
		// http://registry.easytv.eu/application/cs/audio/eq(true) 
		// ->
		// http://registry.easytv.eu/application/cs/ui/text/size(23)
		RuleWrapper rl3 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] {
								new RuleCondition("http://registry.easytv.eu/application/cs/audio/eq", "EQ",
										new Argument[] {
												new Argument("http://www.w3.org/2001/XMLSchema#boolean", true) }),
								new RuleCondition(
										"http://registry.easytv.eu/application/cs/accessibility/magnification/scale",
										"EQ",
										new Argument[] {
												new Argument("http://www.w3.org/2001/XMLSchema#double", 3.5) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://registry.easytv.eu/application/cs/ui/text/size",
								"EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "23") }) },
						0, 0));

		// http://registry.easytv.eu/application/cs/ui/text/size(23)
		// http://registry.easytv.eu/application/cs/audio/eq(true) 
		// ->
		// http://registry.easytv.eu/application/cs/accessibility/magnification/scale(3.5)
		RuleWrapper rl4 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] {
								new RuleCondition("http://registry.easytv.eu/application/cs/ui/text/size", "EQ",
										new Argument[] {
												new Argument("http://www.w3.org/2001/XMLSchema#string", "23") }),
								new RuleCondition("http://registry.easytv.eu/application/cs/audio/eq", "EQ",
										new Argument[] {
												new Argument("http://www.w3.org/2001/XMLSchema#boolean", true) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition(
								"http://registry.easytv.eu/application/cs/accessibility/magnification/scale", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 3.5) }) },
						0, 0));

		// http://registry.easytv.eu/application/cs/audio/eq(true) 
		// ->
		// http://registry.easytv.eu/application/cs/ui/text/size(23)
		// http://registry.easytv.eu/application/cs/accessibility/magnification/scale(3.5)
		RuleWrapper rl5 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://registry.easytv.eu/application/cs/audio/eq",
								"EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#boolean", true) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] {
								new RuleCondition(
										"http://registry.easytv.eu/application/cs/accessibility/magnification/scale",
										"EQ", new Argument[] {
												new Argument("http://www.w3.org/2001/XMLSchema#double", 3.5) }),
								new RuleCondition("http://registry.easytv.eu/application/cs/ui/text/size", "EQ",
										new Argument[] {
												new Argument("http://www.w3.org/2001/XMLSchema#string", "23") }) },
						0, 0));

		// http://registry.easytv.eu/application/cs/accessibility/magnification/scale(3.5)
		// http://registry.easytv.eu/application/cs/ui/text/size(23) 
		// ->
		// http://registry.easytv.eu/application/cs/audio/eq(true)
		RuleWrapper rl6 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition(
								"http://registry.easytv.eu/application/cs/accessibility/magnification/scale", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 3.5) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] {
								new RuleCondition("http://registry.easytv.eu/application/cs/audio/eq", "EQ",
										new Argument[] {
												new Argument("http://www.w3.org/2001/XMLSchema#boolean", true) }),
								new RuleCondition("http://registry.easytv.eu/application/cs/ui/text/size", "EQ",
										new Argument[] {
												new Argument("http://www.w3.org/2001/XMLSchema#string", "23") }) },
						0, 0));

		// http://registry.easytv.eu/application/cs/accessibility/detection/sound(true)
		// -> 
		// http://registry.easytv.eu/common/volume (0,4)
		RuleWrapper rl7 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition(
								"http://registry.easytv.eu/application/cs/accessibility/detection/sound", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#boolean", true) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] {
								new RuleCondition("http://registry.easytv.eu/common/volume", "GE",
										new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 0) }),
								new RuleCondition("http://registry.easytv.eu/common/volume", "LE",
										new Argument[] {
												new Argument("http://www.w3.org/2001/XMLSchema#integer", 4) }) },
						0, 0));

		// http://registry.easytv.eu/application/cs/accessibility/magnification/scale(3.5)
		// -> 
		// http://registry.easytv.eu/application/cs/audio/eq(true)
		// http://registry.easytv.eu/application/cs/ui/text/size(23)
		RuleWrapper rl8 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition(
								"http://registry.easytv.eu/application/cs/accessibility/magnification/scale", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 3.5) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] {
								new RuleCondition("http://registry.easytv.eu/application/cs/audio/eq", "EQ",
										new Argument[] {
												new Argument("http://www.w3.org/2001/XMLSchema#boolean", true) }),
								new RuleCondition("http://registry.easytv.eu/application/cs/ui/text/size", "EQ",
										new Argument[] {
												new Argument("http://www.w3.org/2001/XMLSchema#string", "23") }) },
						0, 0));

		Vector<RuleWrapper> expectedRules = new Vector<RuleWrapper>();
		expectedRules.add(rl1);
		expectedRules.add(rl2);
		expectedRules.add(rl3);
		expectedRules.add(rl4);
		expectedRules.add(rl5);
		expectedRules.add(rl6);
		expectedRules.add(rl7);
		expectedRules.add(rl8);
		

		// Get rules
		Vector<RuleWrapper> actualRules = ruleRefiner.refineRules(profiles, rbmmRules, minSupport, minConfidence);
		Assert.assertEquals(expectedRules, actualRules);
	}
}
