package com.certh.iti.easytv.stmm.association.analysis.rules;

import java.util.Vector;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.certh.iti.easytv.stmm.association.analysis.fpgrowth.Itemset;


public class AssociationRulesFilterTests {
	
	@Test
	public void test_filter_rules_identical() {
		
		Vector<AssociationRule> actual = new Vector<AssociationRule>();
		actual.add(new AssociationRule(new Itemset("1"), new Itemset("2,5")));
		actual.add(new AssociationRule(new Itemset("2"), new Itemset("1,5")));
		
		Vector<AssociationRule> expected = new Vector<AssociationRule>();
		expected.add(new AssociationRule(new Itemset("1"), new Itemset("2,5")));
		expected.add(new AssociationRule(new Itemset("2"), new Itemset("1,5")));

		AssociationRuleFilter.filter(actual);
		Assert.assertEquals(actual, expected);	
	}

	@Test
	public void test_filter_rules_2() {
		Vector<AssociationRule> actual = new Vector<AssociationRule>();
		actual.add(new AssociationRule(new Itemset("1,2,3"), new Itemset("4,5")));
		actual.add(new AssociationRule(new Itemset("1,2,3"), new Itemset("4,5")));
		
		Vector<AssociationRule> expected = new Vector<AssociationRule>();
		expected.add(new AssociationRule(new Itemset("1,2,3"), new Itemset("4,5")));
		
		AssociationRuleFilter.filter(actual);
		Assert.assertEquals(actual, expected);		
	}
	
	@Test
	public void test_filter_rules_3() {
		Vector<AssociationRule> actual = new Vector<AssociationRule>();
		actual.add(new AssociationRule(new Itemset("1,2,3,4"), new Itemset("5,6,7")));
		actual.add(new AssociationRule(new Itemset("1,2,3"), new Itemset("5,6")));
		
		Vector<AssociationRule> expected = new Vector<AssociationRule>();
		expected.add(new AssociationRule(new Itemset("1,2,3,4"), new Itemset("5,6,7")));
		
		AssociationRuleFilter.filter(actual);
		Assert.assertEquals(actual, expected);		
	}

	@Test
	public void test_filter_rules_4() {
		
		Vector<AssociationRule> actual = new Vector<AssociationRule>();
		actual.add(new AssociationRule(new Itemset("1,2,3"), new Itemset("4,5")));
		actual.add(new AssociationRule(new Itemset("1,2,3,4"), new Itemset("5,6,7")));
		
		Vector<AssociationRule> expected = new Vector<AssociationRule>();
		expected.add(new AssociationRule(new Itemset("1,2,3"), new Itemset("4,5")));
		expected.add(new AssociationRule(new Itemset("1,2,3,4"), new Itemset("5,6,7")));

		AssociationRuleFilter.filter(actual);	
		Assert.assertEquals(actual, expected);	
	}
	
	@Test
	public void test_filter_rules_5() {
		
		Vector<AssociationRule> actual = new Vector<AssociationRule>();
		actual.add(new AssociationRule(new Itemset("1,2,3"), new Itemset("4,5")));
		actual.add(new AssociationRule(new Itemset("1,2,3"), new Itemset("4,5")));
		actual.add(new AssociationRule(new Itemset("1,2,3,4"), new Itemset("5,6,7")));
		actual.add(new AssociationRule(new Itemset("1,2,3"), new Itemset("5,6")));
		
		Vector<AssociationRule> expected = new Vector<AssociationRule>();
		expected.add(new AssociationRule(new Itemset("1,2,3"), new Itemset("4,5")));
		expected.add(new AssociationRule(new Itemset("1,2,3,4"), new Itemset("5,6,7")));

		AssociationRuleFilter.filter(actual);	
		Assert.assertEquals(actual, expected);	
	}
	
	@Test
	public void test_filter_rules_6() {
		
		Vector<AssociationRule> actual = new Vector<AssociationRule>();
		actual.add(new AssociationRule(new Itemset("1,2,3"), new Itemset("14,15")));
		actual.add(new AssociationRule(new Itemset("1,2,3"), new Itemset("14,15")));
		actual.add(new AssociationRule(new Itemset("1,2,3,4"), new Itemset("14,15")));
		actual.add(new AssociationRule(new Itemset("1,2"), new Itemset("14,15")));
		
		Vector<AssociationRule> expected = new Vector<AssociationRule>();
		expected.add(new AssociationRule(new Itemset("1,2,3,4"), new Itemset("14,15")));

		AssociationRuleFilter.filter(actual);	
		Assert.assertEquals(actual, expected);	
	}
	
	@Test
	public void test_filter_rules_7() {
		
		Vector<AssociationRule> actual = new Vector<AssociationRule>();
		actual.add(new AssociationRule(new Itemset("1,2,3"), new Itemset("14,15")));
		actual.add(new AssociationRule(new Itemset("1,2,3"), new Itemset("14,15")));
		actual.add(new AssociationRule(new Itemset("1,2,3"), new Itemset("14,15,16")));
		actual.add(new AssociationRule(new Itemset("1,2,3"), new Itemset("14")));
		
		Vector<AssociationRule> expected = new Vector<AssociationRule>();
		expected.add(new AssociationRule(new Itemset("1,2,3"), new Itemset("14,15,16")));

		AssociationRuleFilter.filter(actual);	
		Assert.assertEquals(actual, expected);	
	}
	
	@Test
	public void test_filter_rules_8() {
		
		Vector<AssociationRule> actual = new Vector<AssociationRule>();
		actual.add(new AssociationRule(new Itemset("1,2,3,4"), new Itemset("14,15")));
		actual.add(new AssociationRule(new Itemset("1,2,3,5"), new Itemset("14,15")));
		actual.add(new AssociationRule(new Itemset("1,2,3"), new Itemset("14,15,16")));
		actual.add(new AssociationRule(new Itemset("1,2,3"), new Itemset("14,15,17")));
		
		Vector<AssociationRule> expected = new Vector<AssociationRule>();
		expected.add(new AssociationRule(new Itemset("1,2,3,4"), new Itemset("14,15")));
		expected.add(new AssociationRule(new Itemset("1,2,3,5"), new Itemset("14,15")));
		expected.add(new AssociationRule(new Itemset("1,2,3"), new Itemset("14,15,16")));
		expected.add(new AssociationRule(new Itemset("1,2,3"), new Itemset("14,15,17")));
		
		AssociationRuleFilter.filter(actual);	
		Assert.assertEquals(actual, expected);	
	}
	
	@Test
	public void test_filter_unessary_rules_1() {
		
		Vector<AssociationRule> actual = new Vector<AssociationRule>();
		actual.add(new AssociationRule(new Itemset("1,2,3,4"), new Itemset("14,15,16,17,18")));
		actual.add(new AssociationRule(new Itemset("1,2,17"), new Itemset("14,15,16,17,18")));
		actual.add(new AssociationRule(new Itemset("1,2,17"), new Itemset("14,15,16")));
		actual.add(new AssociationRule(new Itemset("1,2,3,4"), new Itemset("14,15,18")));
		actual.add(new AssociationRule(new Itemset("1,2,3,4"), new Itemset("14,15,16")));

		
		Vector<AssociationRule> expected = new Vector<AssociationRule>();
		expected.add(new AssociationRule(new Itemset("1,2,17"), new Itemset("14,15,16")));
		expected.add(new AssociationRule(new Itemset("1,2,3,4"), new Itemset("14,15,16")));

		//Filter out rules with head items greater or equals to 17
		AssociationRuleFilter.filter(actual, 17);	
		Assert.assertEquals(actual, expected);	
	}


}
