package com.certh.iti.easytv.stmm.association.analysis.rules;

import java.util.Vector;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.certh.iti.easytv.stmm.association.analysis.fpgrowth.FPGrowth;
import com.certh.iti.easytv.stmm.association.analysis.fpgrowth.Itemset;

import junit.framework.Assert;

public class RulesGeneratorTests {
	
	private FPGrowth fpg;
	private Vector<Itemset> itemsets;
	private Vector<Itemset> frequentItemsets;
	private Vector<Itemset> actualSubSets;
	private Vector<AssociationRule> rules;
	private Vector<AssociationRule> allRules;
	private int[] counts;
	private AssociationRuleGenerator ruleGenerator;
	
	/**
	 * 	Table of transactions
		TID 	List of item IDs
		------------------------
		T100 	I1, I2, I5
		T200 	I2, I4
		T300 	I2, I3
		T400 	I1, I2, I4
		T500 	I1, I3
		T600 	I2, I3
		T700 	I1, I3
		T800 	I1, I2, I3, I5
		T900 	I1, I2, I3
	 */
	@BeforeClass
	public void beforTest() {
		itemsets = new Vector<Itemset>();
		itemsets.add(new Itemset("1,2,5"));
		itemsets.add(new Itemset("2,4"));
		itemsets.add(new Itemset("2,3"));
		itemsets.add(new Itemset("1,2,4"));
		itemsets.add(new Itemset("1,3"));
		itemsets.add(new Itemset("2,3"));
		itemsets.add(new Itemset("1,3"));
		itemsets.add(new Itemset("1,2,3,5"));
		itemsets.add(new Itemset("1,2,3"));
			
		//count of distinct items
		counts = new int[] {0, 6, 7, 6, 2, 2};
		
	    fpg = new FPGrowth(itemsets, counts);
	    fpg.findFrequentItemsets(0.222);
	    frequentItemsets = fpg.getFrequentItemsets();
	    
	    //Generate assoication rules of first itemset
	    Itemset itemset = itemsets.get(0);
	    
	    //all subset of itemset "1,2,5"
		actualSubSets = AssociationRuleGenerator.generateAllSubSet(itemset);
		
		//corresponding association rules 
		rules = AssociationRuleGenerator.generateAllRules(itemsets, actualSubSets, itemset, 0.1);
		
		//create rule generator
		ruleGenerator = new AssociationRuleGenerator(itemsets);
		
		allRules = ruleGenerator.findAssociationRules(frequentItemsets, 0.1);
	}
	
	/**
	 * test the generated frequent itemsets
	 */
	@Test
	public void test_frequent_itemset() {
		
		Vector<Itemset> expected = new Vector<Itemset>();
		expected.add(new Itemset("4")); expected.add(new Itemset("2,4"));  
		expected.add(new Itemset("5")); expected.add(new Itemset("2,5")); expected.add(new Itemset("1,2,5")); expected.add(new Itemset("1,5"));
		expected.add(new Itemset("1")); expected.add(new Itemset("1,2")); expected.add(new Itemset("1,2,3")); expected.add(new Itemset("1,3"));
		expected.add(new Itemset("3")); expected.add(new Itemset("2,3"));
		expected.add(new Itemset("2"));
	
		Assert.assertEquals(expected, frequentItemsets);
	}
	
	/**
	 * test the generated subsets of Itemset("1,2,5")
	 */
	@Test
	public void test_subset() {
		
		Vector<Itemset> expectedSubSets = new Vector<Itemset>();
		expectedSubSets.add(new Itemset("1"));
		expectedSubSets.add(new Itemset("2"));
		expectedSubSets.add(new Itemset("5"));
		expectedSubSets.add(new Itemset("1,2"));
		expectedSubSets.add(new Itemset("1,5"));
		expectedSubSets.add(new Itemset("2,5"));
		expectedSubSets.add(new Itemset("1,2,5"));
		
		Assert.assertEquals(expectedSubSets, actualSubSets);
	}
	
	/**
	 * test the generated rules of Itemset("1,2,5")
	 */
	@Test
	public void test_generateRules() {
		
		Vector<AssociationRule> expected = new Vector<AssociationRule>();
		expected.add(new AssociationRule(new Itemset("1"), new Itemset("2,5")));
		expected.add(new AssociationRule(new Itemset("2"), new Itemset("1,5")));
		expected.add(new AssociationRule(new Itemset("5"), new Itemset("1,2")));
		expected.add(new AssociationRule(new Itemset("1,2"), new Itemset("5")));
		expected.add(new AssociationRule(new Itemset("1,5"), new Itemset("2")));
		expected.add(new AssociationRule(new Itemset("2,5"), new Itemset("1")));

		Assert.assertEquals(expected, rules);	
	}
	
	/**
	 * test the generated rules of confidence
	 */
	@Test
	public void test_generateRulesConfidence() {

		/**	
		 	 Association rule	confidence
		 	 -----------------------------
		     I1 => {I2, I5}		2/6 D 33%
			 I2 => {I1, I5}		2/7 D 29%
			 I5 => {I1, I2}		2/2 D 100%
			 {I1, I2} => I5		2/4 D 50%
	 		 {I1, I5} => I2		2/2 D 100%
			 {I2, I5} => I1		2/2 D 100%
		 * 
		 */
		
		double[] support = new double[] { 2.0/6.0, 2.0/7.0, 2.0/2.0, 2.0/4.0, 2.0/2.0, 2.0/2.0 };

		int index = 0;
		for(AssociationRule rule : rules) {
			System.out.println(rule);
			Assert.assertEquals(support[index++], rule.getConfidence());
		}
	}
	
	/**
	 * test the generated rules of Itemset("1,2,5")
	 */
	@Test
	public void test_findAssociationRules() {
			
		/**
		    Rules		confidence
		    ----------------------
		    2 => 4 			0.29
			4 => 2 			1.00
			2 => 5 			0.29
			5 => 2 			1.00
			1 => 2,5 		0.33
			2 => 1,5 		0.29
			5 => 1,2 		1.00
			1,2 => 5 		0.50
			1,5 => 2 		1.00
			2,5 => 1 		1.00
			1 => 5 			0.33
			5 => 1			1.00
			1 => 2			0.67
			2 => 1 			0.57
			1 => 2,3 		0.33
			2 => 1,3 		0.29
			3 => 1,2 		0.33
			1,2 => 3 		0.50
			1,3 => 2 		0.50
			2,3 => 1 		0.50
			1 => 3 			0.67
			3 => 1 			0.67
			2 => 3 			0.57
			3 => 2 			0.67
		 */
		
		Vector<AssociationRule> expected = new Vector<AssociationRule>();
		expected.add(new AssociationRule(new Itemset("2"), new Itemset("4")));
		expected.add(new AssociationRule(new Itemset("4"), new Itemset("2")));
		expected.add(new AssociationRule(new Itemset("2"), new Itemset("5")));
		expected.add(new AssociationRule(new Itemset("5"), new Itemset("2")));
		expected.add(new AssociationRule(new Itemset("1"), new Itemset("2,5")));
		expected.add(new AssociationRule(new Itemset("2"), new Itemset("1,5")));
		expected.add(new AssociationRule(new Itemset("5"), new Itemset("1,2")));
		
		expected.add(new AssociationRule(new Itemset("1,2"), new Itemset("5")));
		expected.add(new AssociationRule(new Itemset("1,5"), new Itemset("2")));
		expected.add(new AssociationRule(new Itemset("2,5"), new Itemset("1")));

		expected.add(new AssociationRule(new Itemset("1"), new Itemset("5")));
		expected.add(new AssociationRule(new Itemset("5"), new Itemset("1")));
		expected.add(new AssociationRule(new Itemset("1"), new Itemset("2")));
		expected.add(new AssociationRule(new Itemset("2"), new Itemset("1")));

		expected.add(new AssociationRule(new Itemset("1"), new Itemset("2,3")));
		expected.add(new AssociationRule(new Itemset("2"), new Itemset("1,3")));
		expected.add(new AssociationRule(new Itemset("3"), new Itemset("1,2")));

		expected.add(new AssociationRule(new Itemset("1,2"), new Itemset("3")));
		expected.add(new AssociationRule(new Itemset("1,3"), new Itemset("2")));
		expected.add(new AssociationRule(new Itemset("2,3"), new Itemset("1")));
		
		expected.add(new AssociationRule(new Itemset("1"), new Itemset("3")));
		expected.add(new AssociationRule(new Itemset("3"), new Itemset("1")));
		expected.add(new AssociationRule(new Itemset("2"), new Itemset("3")));
		expected.add(new AssociationRule(new Itemset("3"), new Itemset("2")));

		Assert.assertEquals(expected, allRules);	
	}

}
