package com.certh.iti.easytv.stmm.association.analysis.rules;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.certh.iti.easytv.stmm.association.analysis.fpgrowth.Itemset;

public class AssociationRuleTest {
	
	@Test
	public void test_equals_equals() {
		AssociationRule rule1 = new AssociationRule(new Itemset("1,2,3"), new Itemset("4,5"));
		AssociationRule rule2 = new AssociationRule(new Itemset("1,2,3"), new Itemset("4,5"));
		
		Assert.assertEquals(rule1.canSubstituted(rule2), true);
	}
	
	@Test
	public void test_equals_less() {
		AssociationRule rule1 = new AssociationRule(new Itemset("1,2,3"), new Itemset("4"));
		AssociationRule rule2 = new AssociationRule(new Itemset("1,2,3"), new Itemset("4,5"));
		
		Assert.assertEquals(rule1.canSubstituted(rule2), false);
	}
	
	@Test
	public void test_equals_more() {
		AssociationRule rule1 = new AssociationRule(new Itemset("1,2,3"), new Itemset("4,5,6"));
		AssociationRule rule2 = new AssociationRule(new Itemset("1,2,3"), new Itemset("4,5"));
		
		Assert.assertEquals(rule1.canSubstituted(rule2), true);
	}
	
	@Test
	public void test_equals_no() {
		AssociationRule rule1 = new AssociationRule(new Itemset("1,2,3"), new Itemset("7,8,9"));
		AssociationRule rule2 = new AssociationRule(new Itemset("1,2"), new Itemset("4,5"));
		
		Assert.assertEquals(rule1.canSubstituted(rule2), false);
	}
	
	//******************************************************************************************//
	
	
	@Test
	public void test_less_equals() {
		AssociationRule rule1 = new AssociationRule(new Itemset("1,2"), new Itemset("4,5"));
		AssociationRule rule2 = new AssociationRule(new Itemset("1,2,3"), new Itemset("4,5"));
		
		Assert.assertEquals(rule1.canSubstituted(rule2), false);
	}
	
	
	@Test
	public void test_less_less() {
		AssociationRule rule1 = new AssociationRule(new Itemset("1,2"), new Itemset("4"));
		AssociationRule rule2 = new AssociationRule(new Itemset("1,2,3"), new Itemset("4,5"));
		
		Assert.assertEquals(rule1.canSubstituted(rule2), false);
	}
	
	@Test
	public void test_less_more() {
		AssociationRule rule1 = new AssociationRule(new Itemset("1,2"), new Itemset("4,5,6"));
		AssociationRule rule2 = new AssociationRule(new Itemset("1,2,3"), new Itemset("4,5"));
		
		Assert.assertEquals(rule1.canSubstituted(rule2), false);
	}
	
	@Test
	public void test_less_no() {
		AssociationRule rule1 = new AssociationRule(new Itemset("1,2,3"), new Itemset("7,8,9"));
		AssociationRule rule2 = new AssociationRule(new Itemset("1,2"), new Itemset("4,5"));
		
		Assert.assertEquals(rule1.canSubstituted(rule2), false);
	}
	
	//******************************************************************************************//

	
	
	@Test
	public void test_more_equals() {
		AssociationRule rule1 = new AssociationRule(new Itemset("1,2,3"), new Itemset("4,5"));
		AssociationRule rule2 = new AssociationRule(new Itemset("1,2"), new Itemset("4,5"));
		
		Assert.assertEquals(rule1.canSubstituted(rule2), true);
	}
	
	@Test
	public void test_more_less() {
		AssociationRule rule1 = new AssociationRule(new Itemset("1,2,3"), new Itemset("4"));
		AssociationRule rule2 = new AssociationRule(new Itemset("1,2"), new Itemset("4,5"));
		
		Assert.assertEquals(rule1.canSubstituted(rule2), false);
	}
	
	@Test
	public void test_more_more() {
		AssociationRule rule1 = new AssociationRule(new Itemset("1,2,3"), new Itemset("4,5,6"));
		AssociationRule rule2 = new AssociationRule(new Itemset("1,2"), new Itemset("4,5"));
		
		Assert.assertEquals(rule1.canSubstituted(rule2), false);
	}
	
	
	@Test
	public void test_more_no() {
		AssociationRule rule1 = new AssociationRule(new Itemset("1,2,3"), new Itemset("7,8,9"));
		AssociationRule rule2 = new AssociationRule(new Itemset("1,2"), new Itemset("4,5"));
		
		Assert.assertEquals(rule1.canSubstituted(rule2), false);
	}
	
	//******************************************************************************************//

	
	
	@Test
	public void test_no_equals() {
		AssociationRule rule1 = new AssociationRule(new Itemset("7,8,9"), new Itemset("4,5"));
		AssociationRule rule2 = new AssociationRule(new Itemset("1,2"), new Itemset("4,5"));
		
		Assert.assertEquals(rule1.canSubstituted(rule2), false);
	}
	
	@Test
	public void test_no_less() {
		AssociationRule rule1 = new AssociationRule(new Itemset("7,8,9"), new Itemset("4"));
		AssociationRule rule2 = new AssociationRule(new Itemset("1,2"), new Itemset("4,5"));
		
		Assert.assertEquals(rule1.canSubstituted(rule2), false);
	}
	
	@Test
	public void test_no_more() {
		AssociationRule rule1 = new AssociationRule(new Itemset("7,8,9"), new Itemset("4,5,6"));
		AssociationRule rule2 = new AssociationRule(new Itemset("1,2"), new Itemset("4,5"));
		
		Assert.assertEquals(rule1.canSubstituted(rule2), false);
	}
	
	@Test
	public void test_no_no() {
		AssociationRule rule1 = new AssociationRule(new Itemset("7,8"), new Itemset("9"));
		AssociationRule rule2 = new AssociationRule(new Itemset("1,2"), new Itemset("4,5"));
		
		Assert.assertEquals(rule1.canSubstituted(rule2), false);
	}
	
	//******************************************************************************************//

	@Test
	public void test_equals() {
		AssociationRule rule1 = new AssociationRule(new Itemset("1,2,3"), new Itemset("4,5"));
		AssociationRule rule2 = new AssociationRule(new Itemset("1,2,3"), new Itemset("4,5"));
		
		Assert.assertTrue(rule1.equals(rule2));
	}
	
	@Test
	public void test_not_equals() {
		AssociationRule rule1 = new AssociationRule(new Itemset("1,2,3,4"), new Itemset("4,5"));
		AssociationRule rule2 = new AssociationRule(new Itemset("1,2,3"), new Itemset("4,5"));
		AssociationRule rule3 = new AssociationRule(new Itemset("1,2,3"), new Itemset("4,5,6"));
		
		Assert.assertFalse(rule1.equals(rule2));
		Assert.assertFalse(rule2.equals(rule1));
				
		Assert.assertFalse(rule3.equals(rule2));
		Assert.assertFalse(rule2.equals(rule3));
		
		Assert.assertFalse(rule1.equals(rule3));
		Assert.assertFalse(rule3.equals(rule1));
	}

}
