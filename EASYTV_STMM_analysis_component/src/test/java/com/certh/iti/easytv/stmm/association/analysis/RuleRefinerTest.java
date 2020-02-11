package com.certh.iti.easytv.stmm.association.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.certh.iti.easytv.config.Config;
import com.certh.iti.easytv.stmm.association.analysis.rules.AssociationRuleWrapper;
import com.certh.iti.easytv.stmm.association.analysis.rules.RbmmRuleWrapper;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.BodyRuleConditions;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.HeadRuleConditions;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.RuleCondition;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.RuleCondition.Argument;
import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

import junit.framework.Assert;

public class RuleRefinerTest {

	private List<Profile> profiles = new ArrayList<Profile>();
	private RuleRefiner ruleRefiner;

	@BeforeTest
	public void beforeTest() throws IOException, UserProfileParsingException {
		// load profiles
		for (int i = 0; i < 9; i++) {
			System.out.println("load profile: " + "userProfile_" + i + ".json");
			JSONObject json = Config.getProfile("userProfile_" + i + ".json");
			profiles.add(new Profile(json));
		}

		ruleRefiner = new RuleRefiner(Profile.getBins());
	}

	/**
	 * Test that refineRules would return the proper collection of rules
	 */
	@Test
	public void test_refineRules_use_Case() {

		double minSupport = 0.5;
		double minConfidence = 0.5;

		// Rbmm rules
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
				"            \"xml-type\": \"http://www.w3.org/2001/XMLSchema#boolean\"," + 
				"            \"value\": false" + 
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

	/**
	 * A set of unit tests to tests the refineRules difference cases
	 */
	@Test
	public void test_refineRules_matched() {

		AssociationRuleWrapper as1 = new AssociationRuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://1", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 2.0) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "LE",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 2.5) }) },
						0, 0));

		RbmmRuleWrapper rb1 = new RbmmRuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://1", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 2.0) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		Vector<RuleWrapper> expected = new Vector<RuleWrapper>();
		expected.add(
				new RbmmRuleWrapper(
						new BodyRuleConditions(
								new RuleCondition[] { new RuleCondition("http://1", "EQ",
										new Argument[] {
												new Argument("http://www.w3.org/2001/XMLSchema#double", 2.0) }) },
								0, 0),
						as1.getHead()));

		Vector<AssociationRuleWrapper> asRules = new Vector<AssociationRuleWrapper>();
		asRules.add(as1);

		Vector<RbmmRuleWrapper> rbmmRules = new Vector<RbmmRuleWrapper>();
		rbmmRules.add(rb1);

		Vector<RuleWrapper> actual = RuleRefiner.refineRules(asRules, rbmmRules);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_refineRules_non_matched() {

		AssociationRuleWrapper as1 = new AssociationRuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://1", "LE",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 2.0) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 2.5) }) },
						0, 0));

		RbmmRuleWrapper rb1 = new RbmmRuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://1", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 2.0) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		Vector<RuleWrapper> expected = new Vector<RuleWrapper>();
		expected.add(as1);

		Vector<AssociationRuleWrapper> asRules = new Vector<AssociationRuleWrapper>();
		asRules.add(as1);

		Vector<RbmmRuleWrapper> rbmmRules = new Vector<RbmmRuleWrapper>();
		rbmmRules.add(rb1);

		Vector<RuleWrapper> actual = RuleRefiner.refineRules(asRules, rbmmRules);

		Assert.assertEquals(expected, actual);
	}

	@Test(expectedExceptions = IllegalStateException.class)
	public void test_refineRules_exception() {

		AssociationRuleWrapper as1 = new AssociationRuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://1", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 2.0) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		AssociationRuleWrapper as2 = new AssociationRuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://1", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		RbmmRuleWrapper rb1 = new RbmmRuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://1", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 2.0) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		RbmmRuleWrapper rb2 = new RbmmRuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://1", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		RbmmRuleWrapper rb3 = new RbmmRuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://1", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 2.0) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		Vector<AssociationRuleWrapper> asRules = new Vector<AssociationRuleWrapper>();
		asRules.add(as1);
		asRules.add(as2);

		Vector<RbmmRuleWrapper> rbmmRules = new Vector<RbmmRuleWrapper>();
		rbmmRules.add(rb1);
		rbmmRules.add(rb2);
		rbmmRules.add(rb3);

		RuleRefiner.refineRules(asRules, rbmmRules);
	}

}
