package com.certh.iti.easytv.stmm.association.analysis.rules;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.BodyRuleConditions;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.HeadRuleConditions;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.RuleCondition;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.RuleCondition.Argument;

public class RuleWrapperTest {

	@Test
	public void test_creation_from_json() {
		
		JSONObject rule = new JSONObject("{" + 
				"    \"head\": [{" + 
				"        \"args\": [{" + 
				"            \"xml-type\": \"http://www.w3.org/2001/XMLSchema#double\"," + 
				"            \"value\": 1.5" + 
				"        }]," + 
				"        \"preference\": \"http://4\"," + 
				"        \"builtin\": \"EQ\"" + 
				"    }]," + 
				"    \"body\": [{" + 
				"        \"args\": [{" + 
				"            \"xml-type\": \"http://www.w3.org/2001/XMLSchema#double\"," + 
				"            \"value\": 1.6" + 
				"        }]," + 
				"        \"preference\": \"http://1\"," + 
				"        \"builtin\": \"EQ\"" + 
				"    }]" + 
				"}");

		RuleWrapper actual = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://1", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.6) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));
		
		RuleWrapper expected = new RuleWrapper(rule);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void test_toJson_conversion() {
		
		JSONObject expected = new JSONObject("{" + 
				"    \"head\": [{" + 
				"        \"args\": [{" + 
				"            \"xml-type\": \"http://www.w3.org/2001/XMLSchema#double\"," + 
				"            \"value\": 1.5" + 
				"        }]," + 
				"        \"preference\": \"http://4\"," + 
				"        \"builtin\": \"EQ\"" + 
				"    }]," + 
				"    \"body\": [{" + 
				"        \"args\": [{" + 
				"            \"xml-type\": \"http://www.w3.org/2001/XMLSchema#double\"," + 
				"            \"value\": 1.6" + 
				"        }]," + 
				"        \"preference\": \"http://1\"," + 
				"        \"builtin\": \"EQ\"" + 
				"    }]" + 
				"}");

		RuleWrapper as1 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://1", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.6) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));
		
		JSONObject actual = as1.getJSONObject();
		Assert.assertTrue(actual.similar(expected));
	}
	
	@Test
	public void test_conversion() {
		
		JSONObject expected = new JSONObject("{" + 
				"    \"head\": [{" + 
				"        \"args\": [{" + 
				"            \"xml-type\": \"http://www.w3.org/2001/XMLSchema#double\"," + 
				"            \"value\": 1.5" + 
				"        }]," + 
				"        \"preference\": \"http://4\"," + 
				"        \"builtin\": \"EQ\"" + 
				"    }]," + 
				"    \"body\": [{" + 
				"        \"args\": [{" + 
				"            \"xml-type\": \"http://www.w3.org/2001/XMLSchema#double\"," + 
				"            \"value\": 1.6" + 
				"        }]," + 
				"        \"preference\": \"http://1\"," + 
				"        \"builtin\": \"EQ\"" + 
				"    }]" + 
				"}");

		RuleWrapper ruleWrapper = new RuleWrapper(expected);
		JSONObject actual = ruleWrapper.getJSONObject(); 
		Assert.assertTrue(expected.similar(actual));
	}
	
	/**
	 * test that two rules with the same body sections have the same hascode value
	 */
	@Test
	public void test_hasCode_equals_args() {

		RuleWrapper as1 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://1", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.6) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		RuleWrapper as2 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://1", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.6) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		Assert.assertTrue(as1.hashCode() == as2.hashCode());
	}

	/**
	 * test that two rules with the different body sections dose not have the same hascode value
	 */
	@Test
	public void test_hasCode_not_equals_args() {

		RuleWrapper as1 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://1", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.4) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		RuleWrapper as2 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://1", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.3) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		Assert.assertFalse(as1.hashCode() == as2.hashCode());
	}

	/**
	 * test that two rules with the different body sections dose not have the same hascode value
	 */
	@Test
	public void test_hasCode_not_equals_uri() {

		RuleWrapper as1 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://11", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		RuleWrapper as2 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://1", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		Assert.assertFalse(as1.hashCode() == as2.hashCode());
	}

	/**
	 * test that two rules with the different body sections dose not have the same hascode value
	 */
	@Test
	public void test_hasCode_not_equals_operand_type() {

		RuleWrapper as1 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://11", "NE",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		RuleWrapper as2 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://1", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		Assert.assertFalse(as1.hashCode() == as2.hashCode());
	}

	/**
	 * test that two rules with the different body sections dose not have the same hascode value
	 */
	@Test
	public void test_hasCode_not_equals_xml_type() {

		RuleWrapper as1 = new RuleWrapper(
				new BodyRuleConditions(new RuleCondition[] { new RuleCondition("http://11", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#int", 1.5) }) }, 0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		RuleWrapper as2 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://1", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		Assert.assertFalse(as1.hashCode() == as2.hashCode());
	}

	/**
	 * test that two rules with the same body sections have the same hascode value
	 */
	@Test
	public void test_hasCode_equals1() {

		RuleCondition[] bodyConditions = new RuleCondition[] {
				new RuleCondition("http://1", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://2", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 10) }),
				new RuleCondition("http://3", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleCondition[] headConditions = new RuleCondition[] {
				new RuleCondition("http://4", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://5", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 10) }),
				new RuleCondition("http://6", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleWrapper as1 = new RuleWrapper(new BodyRuleConditions(bodyConditions, 0, 0),
				new HeadRuleConditions(headConditions, 0, 0));

		RuleCondition[] bodyConditions1 = new RuleCondition[] {
				new RuleCondition("http://1", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://2", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 10) }),
				new RuleCondition("http://3", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleCondition[] headConditions1 = new RuleCondition[] {
				new RuleCondition("http://4", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://5", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 10) }),
				new RuleCondition("http://6", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleWrapper as2 = new RuleWrapper(new BodyRuleConditions(bodyConditions1, 0, 0),
				new HeadRuleConditions(headConditions1, 0, 0));

		Assert.assertTrue(as1.hashCode() == as2.hashCode());
	}

	/**
	 * test that two rules with the different body sections dose not have the same hascode value
	 */
	@Test
	public void test_hasCode_not_equals1() {

		RuleCondition[] bodyConditions = new RuleCondition[] {
				new RuleCondition("http://1", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://2", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 10) }),
				new RuleCondition("http://3", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleCondition[] headConditions = new RuleCondition[] {
				new RuleCondition("http://4", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://5", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 10) }),
				new RuleCondition("http://6", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleWrapper as1 = new RuleWrapper(new BodyRuleConditions(bodyConditions, 0, 0),
				new HeadRuleConditions(headConditions, 0, 0));

		RuleCondition[] bodyConditions1 = new RuleCondition[] {
				new RuleCondition("http://1", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://2", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 10) }),
				new RuleCondition("http://3", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "heello world") }) };

		RuleCondition[] headConditions1 = new RuleCondition[] {
				new RuleCondition("http://4", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://5", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 10) }),
				new RuleCondition("http://6", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleWrapper as2 = new RuleWrapper(new BodyRuleConditions(bodyConditions1, 0, 0),
				new HeadRuleConditions(headConditions1, 0, 0));

		Assert.assertFalse(as1.hashCode() == as2.hashCode());
	}

	/**
	 * test that two rules with the same body and head section are equals
	 */
	@Test
	public void test_equals() {

		RuleCondition[] bodyConditions = new RuleCondition[] {
				new RuleCondition("http://1", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://2", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 10) }),
				new RuleCondition("http://3", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleCondition[] headConditions = new RuleCondition[] {
				new RuleCondition("http://4", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://5", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 10) }),
				new RuleCondition("http://6", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleWrapper as1 = new RuleWrapper(new BodyRuleConditions(bodyConditions, 0, 0),
				new HeadRuleConditions(headConditions, 0, 0));

		RuleCondition[] bodyConditions1 = new RuleCondition[] {
				new RuleCondition("http://1", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://2", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 10) }),
				new RuleCondition("http://3", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleCondition[] headConditions1 = new RuleCondition[] {
				new RuleCondition("http://4", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://5", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 10) }),
				new RuleCondition("http://6", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleWrapper as2 = new RuleWrapper(new BodyRuleConditions(bodyConditions1, 0, 0),
				new HeadRuleConditions(headConditions1, 0, 0));

		Assert.assertEquals(as1, as2);
	}

	/**
	 * test that two rules that differ in body sections are not equals
	 */
	@Test
	public void test_not_equals_args() {

		RuleWrapper as1 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://1", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.4) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		RuleWrapper as2 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://1", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.3) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		Assert.assertNotEquals(as1, as2);
	}

	@Test
	public void test_not_equals_uri() {

		RuleWrapper as1 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://11", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		RuleWrapper as2 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://1", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		Assert.assertNotEquals(as1, as2);
	}

	@Test
	public void test_not_equals_operand_type() {

		RuleWrapper as1 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://11", "NE",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		RuleWrapper as2 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://1", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		Assert.assertNotEquals(as1, as2);
	}

	@Test
	public void test_not_equals_xml_type() {

		RuleWrapper as1 = new RuleWrapper(
				new BodyRuleConditions(new RuleCondition[] { new RuleCondition("http://11", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#int", 1.5) }) }, 0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		RuleWrapper as2 = new RuleWrapper(
				new BodyRuleConditions(
						new RuleCondition[] { new RuleCondition("http://1", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0),
				new HeadRuleConditions(
						new RuleCondition[] { new RuleCondition("http://4", "EQ",
								new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }) },
						0, 0));

		Assert.assertNotEquals(as1, as2);
	}

	@Test
	public void test_not_equals1() {

		RuleCondition[] bodyConditions = new RuleCondition[] {
				new RuleCondition("http://1", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://2", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 10) }),
				new RuleCondition("http://3", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleCondition[] headConditions = new RuleCondition[] {
				new RuleCondition("http://4", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://5", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 10) }),
				new RuleCondition("http://6", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleWrapper as1 = new RuleWrapper(new BodyRuleConditions(bodyConditions, 0, 0),
				new HeadRuleConditions(headConditions, 0, 0));

		RuleCondition[] bodyConditions1 = new RuleCondition[] {
				new RuleCondition("http://1", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://2", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 10) }),
				new RuleCondition("http://3", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "heello world") }) };

		RuleCondition[] headConditions1 = new RuleCondition[] {
				new RuleCondition("http://4", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://5", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 10) }),
				new RuleCondition("http://6", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleWrapper as2 = new RuleWrapper(new BodyRuleConditions(bodyConditions1, 0, 0),
				new HeadRuleConditions(headConditions1, 0, 0));

		Assert.assertNotEquals(as1, as2);
	}

	@Test
	public void test_not_equals2() {

		RuleCondition[] bodyConditions = new RuleCondition[] {
				new RuleCondition("http://1", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://2", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 11) }),
				new RuleCondition("http://3", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleCondition[] headConditions = new RuleCondition[] {
				new RuleCondition("http://4", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://5", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 11) }),
				new RuleCondition("http://6", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleWrapper as1 = new RuleWrapper(new BodyRuleConditions(bodyConditions, 0, 0),
				new HeadRuleConditions(headConditions, 0, 0));

		RuleCondition[] bodyConditions1 = new RuleCondition[] {
				new RuleCondition("http://1", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://2", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 10) }),
				new RuleCondition("http://3", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleCondition[] headConditions1 = new RuleCondition[] {
				new RuleCondition("http://4", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://5", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 10) }),
				new RuleCondition("http://6", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleWrapper as2 = new RuleWrapper(new BodyRuleConditions(bodyConditions1, 0, 0),
				new HeadRuleConditions(headConditions1, 0, 0));

		Assert.assertNotEquals(as1, as2);
	}

	@Test
	public void test_not_equals3() {

		RuleCondition[] bodyConditions = new RuleCondition[] {
				new RuleCondition("http://1", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 2.0) }),
				new RuleCondition("http://2", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 10) }),
				new RuleCondition("http://3", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleCondition[] headConditions = new RuleCondition[] {
				new RuleCondition("http://4", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://5", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 11) }),
				new RuleCondition("http://6", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleWrapper as1 = new RuleWrapper(new BodyRuleConditions(bodyConditions, 0, 0),
				new HeadRuleConditions(headConditions, 0, 0));

		RuleCondition[] bodyConditions1 = new RuleCondition[] {
				new RuleCondition("http://1", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://2", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 10) }),
				new RuleCondition("http://3", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleCondition[] headConditions1 = new RuleCondition[] {
				new RuleCondition("http://4", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://5", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 10) }),
				new RuleCondition("http://6", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleWrapper as2 = new RuleWrapper(new BodyRuleConditions(bodyConditions1, 0, 0),
				new HeadRuleConditions(headConditions1, 0, 0));

		Assert.assertNotEquals(as1, as2);
	}

	/**
	 * Test that hashing returns right values
	 */
	@Test
	public void test_rule_hashing() {

		RuleCondition[] bodyConditions = new RuleCondition[] {
				new RuleCondition("http://1", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 2.0) }),
				new RuleCondition("http://2", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 10) }),
				new RuleCondition("http://3", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleCondition[] headConditions = new RuleCondition[] {
				new RuleCondition("http://4", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://5", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 11) }),
				new RuleCondition("http://6", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleWrapper as1 = new RuleWrapper(new BodyRuleConditions(bodyConditions, 0, 0),
				new HeadRuleConditions(headConditions, 0, 0));

		RuleCondition[] bodyConditions1 = new RuleCondition[] {
				new RuleCondition("http://1", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://2", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 10) }),
				new RuleCondition("http://3", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleCondition[] headConditions1 = new RuleCondition[] {
				new RuleCondition("http://4", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://5", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 11) }),
				new RuleCondition("http://6", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleWrapper as2 = new RuleWrapper(new BodyRuleConditions(bodyConditions1, 0, 0),
				new HeadRuleConditions(headConditions1, 0, 0));

		RuleCondition[] bodyConditions2 = new RuleCondition[] {
				new RuleCondition("http://1", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://2", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 11) }),
				new RuleCondition("http://3", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleCondition[] headConditions2 = new RuleCondition[] {
				new RuleCondition("http://4", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://5", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 11) }),
				new RuleCondition("http://6", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleWrapper as3 = new RuleWrapper(new BodyRuleConditions(bodyConditions2, 0, 0),
				new HeadRuleConditions(headConditions2, 0, 0));

		RuleCondition[] bodyConditions3 = new RuleCondition[] {
				new RuleCondition("http://1", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://2", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 11) }),
				new RuleCondition("http://3", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleCondition[] headConditions3 = new RuleCondition[] {
				new RuleCondition("http://4", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#double", 1.5) }),
				new RuleCondition("http://5", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#integer", 11) }),
				new RuleCondition("http://6", "EQ",
						new Argument[] { new Argument("http://www.w3.org/2001/XMLSchema#string", "hello world") }) };

		RuleWrapper as4 = new RuleWrapper(new BodyRuleConditions(bodyConditions3, 0, 0),
				new HeadRuleConditions(headConditions3, 0, 0));

		Set<RuleWrapper> rules = new HashSet<RuleWrapper>();
		rules.add(as1);
		rules.add(as2);
		rules.add(as3);
		rules.add(as4);

		Assert.assertEquals(3, rules.size());
	}

}
