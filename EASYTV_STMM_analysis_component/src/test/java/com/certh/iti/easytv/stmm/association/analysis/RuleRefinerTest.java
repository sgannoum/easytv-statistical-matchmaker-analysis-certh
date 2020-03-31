package com.certh.iti.easytv.stmm.association.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.certh.iti.easytv.stmm.association.analysis.rules.AssociationRuleWrapper;
import com.certh.iti.easytv.stmm.association.analysis.rules.RbmmRuleWrapper;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper;
import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

/**
 * Test the whole rules refinement process, from association analysis to generated rules
 * the unit test will actually test the following conditions:
 * 1-) generated rules
 * 2-) rules with contextual conditions in their head section will be filtered out.
 * 3-) rules merging case, where the most specific rules are kept 
 * @author salgan
 *
 */
public class RuleRefinerTest {

	private RuleRefiner ruleRefiner;
	private List<Profile> profiles = new ArrayList<Profile>();
	
	private JSONObject profile_1 = new JSONObject("{" + 
			"    \"user_id\": 1," + 
			"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
			"		\"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": true," + 
			"		\"http://registry.easytv.eu/common/volume\": 0" + 
			"		" + 
			"    }}}}," + 
			"    \"user_context\": {" + 
			"        \"http://registry.easytv.eu/context/device\": \"tablet\"" + 
			"    }" + 
			"}");
	
	private JSONObject profile_2 = new JSONObject("{" +  
			"    \"user_id\": 2," + 
			"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
			"		\"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": true," + 
			"		\"http://registry.easytv.eu/common/volume\": 0" + 
			"		" + 
			"    }}}}," +
			"    \"user_context\": {" + 
			"        \"http://registry.easytv.eu/context/device\": \"tablet\"" + 
			"    }" + 
			"}");
	
	
	private JSONObject profile_3 = new JSONObject("{" + 
			"    \"user_id\": 3," + 
			"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
			"		\"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": true," + 
			"		\"http://registry.easytv.eu/common/volume\": 0" + 
			"		" + 
			"    }}}}," + 
			"    \"user_context\": {" + 
			"        \"http://registry.easytv.eu/context/device\": \"tablet\"" + 
			"    }" + 
			"}");
	
	@BeforeTest
	public void beforeClass() throws IOException, UserProfileParsingException {
		
		profiles.add(new Profile(profile_1));
		profiles.add(new Profile(profile_2));
		profiles.add(new Profile(profile_3));

		ruleRefiner = new RuleRefiner(Profile.getBins());
	}

	/**
	 * Test that refineRules would return the proper collection of rules
	 */
	@Test
	public void test_refineRules_use_Case_1() {

		double minSupport = 0.5;
		double minConfidence = 0.5;

		// No Rbmm rules	
		Vector<RbmmRuleWrapper> rbmmRules = new Vector<RbmmRuleWrapper>();


		RuleWrapper rl1 = new RuleWrapper("http://registry.easytv.eu/context/device = \"tablet\" ^"
										+ "http://registry.easytv.eu/application/cs/accessibility/detection/sound = true"
										+ "->"
										+ "http://registry.easytv.eu/common/volume = 2");
		
		RuleWrapper rl2 = new RuleWrapper("http://registry.easytv.eu/context/device = \"tablet\" "
										+ "->"
										+ "http://registry.easytv.eu/application/cs/accessibility/detection/sound = true ^"
										+ "http://registry.easytv.eu/common/volume = 2");

		RuleWrapper rl3 = new RuleWrapper("http://registry.easytv.eu/context/device = \"tablet\" ^"
										+ "http://registry.easytv.eu/common/volume >= 0 ^"
										+ "http://registry.easytv.eu/common/volume <= 4 ^"
										+ "->"
										+ "http://registry.easytv.eu/application/cs/accessibility/detection/sound = true");
		
		Vector<RuleWrapper> expectedRules = new Vector<RuleWrapper>();
		expectedRules.add(rl1);
		expectedRules.add(rl2);
		expectedRules.add(rl3);

		
		// Get rules
		Vector<RuleWrapper> actualRules = ruleRefiner.refineRules(profiles, rbmmRules, minSupport, minConfidence);		
		Assert.assertEquals(expectedRules, actualRules);
	}
	
	/**
	 * Test that refineRules would return the proper collection of rules
	 */
	@Test
	public void test_refineRules_use_Case_2() {

		double minSupport = 0.5;
		double minConfidence = 0.5;

		// Rbmm rules	
		RbmmRuleWrapper rb1 = new RbmmRuleWrapper("http://registry.easytv.eu/application/cs/accessibility/magnification/scale = false ^"
												+ "http://registry.easytv.eu/application/cs/ui/text/size = 2.3"
												+ "->"
												+ "http://registry.easytv.eu/application/cs/audio/eq = false ");

		RbmmRuleWrapper rb2 = new RbmmRuleWrapper("http://registry.easytv.eu/application/cs/accessibility/magnification/scale = 3.5 ^"
												+ "http://registry.easytv.eu/application/cs/audio/eq = true"
												+ "->"
												+ "http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size = 1.5");

		Vector<RbmmRuleWrapper> rbmmRules = new Vector<RbmmRuleWrapper>();
		rbmmRules.add(rb1);
		rbmmRules.add(rb2);


		RuleWrapper rl1 = new RuleWrapper("http://registry.easytv.eu/context/device = \"tablet\" ^"
										+ "http://registry.easytv.eu/application/cs/accessibility/detection/sound = true"
										+ "->"
										+ "http://registry.easytv.eu/common/volume = 2");
		
		RuleWrapper rl2 = new RuleWrapper("http://registry.easytv.eu/context/device = \"tablet\" "
										+ "->"
										+ "http://registry.easytv.eu/application/cs/accessibility/detection/sound = true ^"
										+ "http://registry.easytv.eu/common/volume = 2");

		RuleWrapper rl3 = new RuleWrapper("http://registry.easytv.eu/context/device = \"tablet\" ^"
										+ "http://registry.easytv.eu/common/volume >= 0 ^"
										+ "http://registry.easytv.eu/common/volume <= 4 ^"
										+ "->"
										+ "http://registry.easytv.eu/application/cs/accessibility/detection/sound = true");
		
		Vector<RuleWrapper> expectedRules = new Vector<RuleWrapper>();
		expectedRules.add(rl1);
		expectedRules.add(rl2);
		expectedRules.add(rl3);

		
		// Get rules
		Vector<RuleWrapper> actualRules = ruleRefiner.refineRules(profiles, rbmmRules, minSupport, minConfidence);		
		Assert.assertEquals(expectedRules, actualRules);
	}


	/**
	 * A rule based rule is updated by an association rule when their bodies are equals, then 
	 * the rule based rule head is substituted by the rules head unions
	 */
	@Test
	public void test_refineRules_update_case_1() {

		Vector<AssociationRuleWrapper> assRules = new Vector<AssociationRuleWrapper>();
		assRules.add(new AssociationRuleWrapper("b1 = 1 ^ b2 = 2.0 ^ b3 = \"str\" ^ b4 = true -> h1 = 2.5 ^ h2 = true"));

		Vector<RbmmRuleWrapper> rbmmRules = new Vector<RbmmRuleWrapper>();
		rbmmRules.add(new RbmmRuleWrapper("b1 = 1 ^ b2 = 2.0 ^ b3 = \"str\" ^ b4 = true -> h1 = 2.5 ^ h3 = 1.5"));

		Vector<RuleWrapper> expected = new Vector<RuleWrapper>();		
		expected.add(new RbmmRuleWrapper("b1 = 1 ^ b2 = 2.0 ^ b3 = \"str\" ^ b4 = true -> h1 = 2.5 ^ h2 = true ^ h3 = 1.5"));

		Vector<RuleWrapper> actual = RuleRefiner.refineRules(assRules, rbmmRules);
		Assert.assertEquals(assRules.get(0).hashCode(), rbmmRules.get(0).hashCode());
		Assert.assertEquals(actual, expected);
	}
	
	@Test
	public void test_refineRules_update_case_2() {

		Vector<AssociationRuleWrapper> assRules = new Vector<AssociationRuleWrapper>();
		assRules.add(new AssociationRuleWrapper("b1 = 1 ^ b2 = 2.0 ^ b3 = \"str\" ^ b4 = true -> h1 = 1.0 ^ h2 = true"));

		Vector<RbmmRuleWrapper> rbmmRules = new Vector<RbmmRuleWrapper>();
		rbmmRules.add(new RbmmRuleWrapper("b1 = 1 ^ b2 = 2.0 ^ b3 = \"str\" ^ b4 = true -> h1 = 2.5 ^ h3 = 1.5"));

		Vector<RuleWrapper> expected = new Vector<RuleWrapper>();		
		expected.add(new RbmmRuleWrapper("b1 = 1 ^ b2 = 2.0 ^ b3 = \"str\" ^ b4 = true -> h1 = 1.0 ^ h2 = true ^ h3 = 1.5"));

		Vector<RuleWrapper> actual = RuleRefiner.refineRules(assRules, rbmmRules);
		Assert.assertEquals(actual, expected);
	}
	
	@Test
	public void test_refineRules_update_case_3() {

		Vector<AssociationRuleWrapper> assRules = new Vector<AssociationRuleWrapper>();
		assRules.add(new AssociationRuleWrapper("b1 = 1 ^ b2 = 2.0 ^ b3 = \"str\" ^ b4 = true -> h1 = 2.5 ^ h2 = true"));
		assRules.add(new AssociationRuleWrapper("b1 = 1 ^ b2 = 2.0 ^ b3 = \"str\" ^ b4 = true -> h1 = 1.0 ^ h2 = true"));

		Vector<RbmmRuleWrapper> rbmmRules = new Vector<RbmmRuleWrapper>();
		rbmmRules.add(new RbmmRuleWrapper("b1 = 1 ^ b2 = 2.0 ^ b3 = \"str\" ^ b4 = true -> h1 = 2.5 ^ h3 = 1.5"));

		Vector<RuleWrapper> expected = new Vector<RuleWrapper>();		
		expected.add(new RbmmRuleWrapper("b1 = 1 ^ b2 = 2.0 ^ b3 = \"str\" ^ b4 = true -> h1 = 2.5 ^ h2 = true ^ h3 = 1.5"));

		Vector<RuleWrapper> actual = RuleRefiner.refineRules(assRules, rbmmRules);
		Assert.assertEquals(actual, expected);
	}

	/**
	 * A rule based rule is remove when there is no matched association rule
	 */
	@Test
	public void test_refineRules_remove_case() {
	
		Vector<AssociationRuleWrapper> assRules = new Vector<AssociationRuleWrapper>();
		assRules.add(new AssociationRuleWrapper("b1 = 1 ^ b2 = 2.0 ^ b3 = \"str\" ^ b4 = true -> h1 = 2.5 ^ h2 = true"));

		Vector<RbmmRuleWrapper> rbmmRules = new Vector<RbmmRuleWrapper>();
		rbmmRules.add(new RbmmRuleWrapper("b1 = 1 ^ b2 = 2.0 -> h1 = 2.5 ^ h2 = true"));

		Vector<RuleWrapper> expected = new Vector<RuleWrapper>();
		expected.add(new AssociationRuleWrapper("b1 = 1 ^ b2 = 2.0 ^ b3 = \"str\" ^ b4 = true -> h1 = 2.5 ^ h2 = true"));

		Vector<RuleWrapper> actual = RuleRefiner.refineRules(assRules, rbmmRules);
		Assert.assertEquals(actual, expected);
	}
	
	/**
	 * An association rule is added, when there is no mactched rule based rule.
	 */
	@Test
	public void test_refineRules_add_case() {

		Vector<AssociationRuleWrapper> assRules = new Vector<AssociationRuleWrapper>();
		assRules.add(new AssociationRuleWrapper("b1 = 1 ^ b2 = 2.0 ^ b3 = \"str\" ^ b4 = true -> h1 = 2.5 ^ h2 = true"));
		
		Vector<RuleWrapper> expected = new Vector<RuleWrapper>();
		expected.add(new AssociationRuleWrapper("b1 = 1 ^ b2 = 2.0 ^ b3 = \"str\" ^ b4 = true -> h1 = 2.5 ^ h2 = true"));
		
		Vector<RuleWrapper> actual = RuleRefiner.refineRules(assRules, new Vector<RbmmRuleWrapper>());
		Assert.assertEquals(expected, actual);
	}
	
	/**
	 * An association rule is added, when there is no matched rule based rule.
	 */
	@Test
	public void test_refineRules_cases() {

		Vector<AssociationRuleWrapper> assRules = new Vector<AssociationRuleWrapper>();
		assRules.add(new AssociationRuleWrapper("b1 = 1 ^ b2 = 2.0 ^ b3 = \"str\" ^ b4 = true -> h1 = 2.5 ^ h2 = true"));
		assRules.add(new AssociationRuleWrapper("b1 = 1 ^ b3 = \"str\" ^ b4 = true -> h1 = 2.5 ^ h2 = true"));		//to be added

		Vector<RbmmRuleWrapper> rbmmRules = new Vector<RbmmRuleWrapper>();
		rbmmRules.add(new RbmmRuleWrapper("b1 = 1 ^ b2 = 2.0 ^ b3 = \"str\" ^ b4 = true -> h1 = 2.5 ^ h3 = 1.5"));	//to be updated
		rbmmRules.add(new RbmmRuleWrapper("b1 = 1 -> h1 = 2.5 ^ h2 = true"));										//to be deleted

		
		Vector<RuleWrapper> expected = new Vector<RuleWrapper>();		
		expected.add(new AssociationRuleWrapper("b1 = 1 ^ b2 = 2.0 ^ b3 = \"str\" ^ b4 = true -> h1 = 2.5 ^ h2 = true ^ h3 = 1.5"));
		expected.add(new RbmmRuleWrapper("b1 = 1 ^ b3 = \"str\" ^ b4 = true -> h1 = 2.5 ^ h2 = true"));


		Vector<RuleWrapper> actual = RuleRefiner.refineRules(assRules, rbmmRules);
		Assert.assertEquals(actual, expected);
	}
	

	@Test(expectedExceptions = IllegalStateException.class)
	public void test_refineRules_exception() {

		Vector<AssociationRuleWrapper> asRules = new Vector<AssociationRuleWrapper>();
		asRules.add(new AssociationRuleWrapper("b1 = 2.0 -> h1 = 1.5"));
		asRules.add(new AssociationRuleWrapper("b1 = 1.5 -> h1 = 1.5"));

		Vector<RbmmRuleWrapper> rbmmRules = new Vector<RbmmRuleWrapper>();
		rbmmRules.add(new RbmmRuleWrapper("b1 = 2.0 -> h1 = 1.5"));
		rbmmRules.add(new RbmmRuleWrapper("b1 = 1.5 -> h1 = 1.5"));
		rbmmRules.add(new RbmmRuleWrapper("b1 = 2.0 -> h1 = 1.5"));

		RuleRefiner.refineRules(asRules, rbmmRules);
	}

}
