package com.certh.iti.easytv.stmm.association.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.certh.iti.easytv.stmm.association.analysis.rules.AssociationRuleWrapper;
import com.certh.iti.easytv.stmm.association.analysis.rules.RbmmRuleWrapper;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper;
import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

import junit.framework.Assert;
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
	 * A set of unit tests to tests the refineRules difference cases
	 */
	@Test
	public void test_rule_update_case() {

		AssociationRuleWrapper as1 = new AssociationRuleWrapper("B1 = 2.0 -> H1 = 2.5");
		RbmmRuleWrapper rb1 = new RbmmRuleWrapper("B1 = 2.0 -> H1 = 1.5");

		Vector<AssociationRuleWrapper> asRules = new Vector<AssociationRuleWrapper>();
		asRules.add(as1);

		Vector<RbmmRuleWrapper> rbmmRules = new Vector<RbmmRuleWrapper>();
		rbmmRules.add(rb1);

		Vector<RuleWrapper> actual = RuleRefiner.refineRules(asRules, rbmmRules);

		Vector<RuleWrapper> expected = new Vector<RuleWrapper>();		
		expected.add(new RbmmRuleWrapper("B1 = 2.0 -> H1 = 2.5"));

		Assert.assertEquals(actual, expected);
	}

	@Test
	public void test_rule_remove_case() {
	
		AssociationRuleWrapper as1 = new AssociationRuleWrapper("B1 = 2.0 -> H1 = 2.5");	
		RbmmRuleWrapper rb1 = new RbmmRuleWrapper("B1 = 2.0 -> H1 = 1.5");

		Vector<AssociationRuleWrapper> asRules = new Vector<AssociationRuleWrapper>();
		asRules.add(as1);

		Vector<RbmmRuleWrapper> rbmmRules = new Vector<RbmmRuleWrapper>();
		rbmmRules.add(rb1);

		Vector<RuleWrapper> actual = RuleRefiner.refineRules(asRules, rbmmRules);

		Vector<RuleWrapper> expected = new Vector<RuleWrapper>();
		expected.add(as1);
		
		Assert.assertEquals(actual, expected);
	}
	
	@Test
	public void test_rule_add_case() {

		AssociationRuleWrapper as1 = new AssociationRuleWrapper("B1 = 2.0 -> H1 = 2.5");	

		
		Vector<RuleWrapper> expected = new Vector<RuleWrapper>();
		expected.add(as1);

		Vector<AssociationRuleWrapper> asRules = new Vector<AssociationRuleWrapper>();
		asRules.add(as1);

		Vector<RbmmRuleWrapper> rbmmRules = new Vector<RbmmRuleWrapper>();
		Vector<RuleWrapper> actual = RuleRefiner.refineRules(asRules, rbmmRules);

		Assert.assertEquals(expected, actual);
	}

	@Test(expectedExceptions = IllegalStateException.class)
	public void test_refineRules_exception() {

		AssociationRuleWrapper as1 = new AssociationRuleWrapper("B1 = 2.0 -> H1 = 1.5");	
		AssociationRuleWrapper as2 = new AssociationRuleWrapper("B1 = 1.5 -> H1 = 1.5");	
		RbmmRuleWrapper rb1 = new RbmmRuleWrapper("B1 = 2.0 -> H1 = 1.5");	
		RbmmRuleWrapper rb2 = new RbmmRuleWrapper("B1 = 1.5 -> H1 = 1.5");	
		RbmmRuleWrapper rb3 = new RbmmRuleWrapper("B1 = 2.0 -> H1 = 1.5");	


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
