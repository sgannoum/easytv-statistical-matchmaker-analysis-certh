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
		Assert.assertEquals(actual, expected);
	}
	
	@Test
	public void test_creation_from_string() {
		
		JSONObject rule = new JSONObject("{" + 
				"    \"head\": [{" + 
				"        \"args\": [{" + 
				"            \"xml-type\": \"http://www.w3.org/2001/XMLSchema#double\"," + 
				"            \"value\": 1.5" + 
				"        }]," + 
				"        \"preference\": \"http://tenth\"," + 
				"        \"builtin\": \"EQ\"" + 
				"    }]," + 
				"    \"body\": ["
				+ "{" + 
				"        \"args\": [{" + 
				"            \"xml-type\": \"http://www.w3.org/2001/XMLSchema#double\"," + 
				"            \"value\": 1.6" + 
				"        }]," + 
				"        \"preference\": \"http://first\"," + 
				"        \"builtin\": \"EQ\"" + 
				"  },"
				+ "{" + 
				"        \"args\": [{" + 
				"            \"xml-type\": \"http://www.w3.org/2001/XMLSchema#integer\"," + 
				"            \"value\": 0" + 
				"        }]," + 
				"        \"preference\": \"http://second\"," + 
				"        \"builtin\": \"GE\"" + 
				"  },"
				+ "{" + 
				"        \"args\": [{" + 
				"            \"xml-type\": \"http://www.w3.org/2001/XMLSchema#integer\"," + 
				"            \"value\": 4" + 
				"        }]," + 
				"        \"preference\": \"http://second\"," + 
				"        \"builtin\": \"LE\"" + 
				"  },"
				+ "{" + 
				"        \"args\": [{" + 
				"            \"xml-type\": \"http://www.w3.org/2001/XMLSchema#integer\"," + 
				"            \"value\": 5" + 
				"        }]," + 
				"        \"preference\": \"http://third\"," + 
				"        \"builtin\": \"GT\"" + 
				"  },"
				+ "{" + 
				"        \"args\": [{" + 
				"            \"xml-type\": \"http://www.w3.org/2001/XMLSchema#integer\"," + 
				"            \"value\": 6" + 
				"        }]," + 
				"        \"preference\": \"http://third\"," + 
				"        \"builtin\": \"LT\"" + 
				"  },"
				+ "{" + 
				"        \"args\": [{" + 
				"            \"xml-type\": \"http://www.w3.org/2001/XMLSchema#boolean\"," + 
				"            \"value\": false" + 
				"        }]," + 
				"        \"preference\": \"http://fourth\"," + 
				"        \"builtin\": \"EQ\"" + 
				"  },"
				+ "{" + 
				"        \"args\": [{" + 
				"            \"xml-type\": \"http://www.w3.org/2001/XMLSchema#boolean\"," + 
				"            \"value\": true" + 
				"        }]," + 
				"        \"preference\": \"http://fifth\"," + 
				"        \"builtin\": \"EQ\"" + 
				"  },"
				+ "{" + 
				"        \"args\": [{" + 
				"            \"xml-type\": \"http://www.w3.org/2001/XMLSchema#string\"," + 
				"            \"value\": \"hello\"" + 
				"        }]," + 
				"        \"preference\": \"http://sixth\"," + 
				"        \"builtin\": \"EQ\"" + 
				"  }"
				+ "]" + 
				"}");
		RuleWrapper expected = new RuleWrapper(rule);
		
		RuleWrapper actual = new RuleWrapper("http://first = 1.6 ^" 
											 + "http://second >= 0 ^ http://second <= 4 ^" 
											 + "http://third > 5 ^ http://third < 6 ^"	
											 + "http://fourth = false ^"
											 + "http://fifth = true ^"
											 + "http://sixth = \"hello\""
											 + "->"
											 + "http://tenth = 1.5");
		
		Assert.assertEquals(actual, expected);
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
	
	@Test
	public void test_merge_rules() {
		RuleWrapper rl1 = new RuleWrapper("body_first = 1.6"
										+ "->"
										+ "head_first = 1.5 ^"
										+ "head_second = 1.5");
		
		RuleWrapper rl2 = new RuleWrapper("body_first = 1.6"
										+ "->"
										+ "head_first = 1.5 ^"
										+ "head_third = 1.5");
		
		RuleWrapper expected = new RuleWrapper("body_first = 1.6"
										+ "->"
										+ "head_first = 1.5 ^"
										+ "head_first = 1.5 ^"
										+ "head_third = 1.5");
		rl1.merge(rl2.getHead());
		
		Assert.assertEquals(rl1, expected);
	}

}
