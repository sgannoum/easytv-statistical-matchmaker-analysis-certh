package com.certh.iti.easytv.stmm.association.analysis.fpgrowth;

import java.util.Vector;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import junit.framework.Assert;


public class FPGrowthTest {
	
	private FPGrowth fpg;
	private Vector<Itemset> itemsets;
	private int[] counts;
	
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
	}
	
	
	/**
	 * test the generated frequent itemsets
	 */
	@Test
	public void test_frequentItemSet() {

		Vector<Itemset> expected = new Vector<Itemset>();
		expected.add(new Itemset("4")); expected.add(new Itemset("2,4"));  
		expected.add(new Itemset("5")); expected.add(new Itemset("2,5")); expected.add(new Itemset("1,2,5")); expected.add(new Itemset("1,5"));
		expected.add(new Itemset("1")); expected.add(new Itemset("1,2")); expected.add(new Itemset("1,2,3")); expected.add(new Itemset("1,3"));
		expected.add(new Itemset("3")); expected.add(new Itemset("2,3"));
		expected.add(new Itemset("2"));
	     
	    Assert.assertEquals(expected, fpg.getFrequentItemsets());
	}
	
	/**
	 	Itemset		support
	 	-------------------
	 	{I2, I5}		2
	 	{I1, I5}		2
	 	{I1, I2, I5}	2
	 	{I2, I4}		2
	  	{I2, I3}		4
	  	{I1, I3}		4
	  	{I1, I2, I3}	2
	 	{I1, I2}		4
	 */
	@Test
	public void test_weight() {
		long[] expected = new long[] {2, 2,
									  2, 2, 2, 2,
									  6, 4, 2, 4,
									  6, 4,
									  7};

	     Vector<Itemset> actual = fpg.getFrequentItemsets();
	     	     
	     for(int i = 0; i < actual.size(); i++) {
	    	 Itemset itemset = actual.get(i);
	      // System.out.println(String.format("%d %d %d",i , expected[i], itemset.getWeight()));
	    	 Assert.assertEquals(expected[i], itemset.getWeight());
	     }
	}

}
