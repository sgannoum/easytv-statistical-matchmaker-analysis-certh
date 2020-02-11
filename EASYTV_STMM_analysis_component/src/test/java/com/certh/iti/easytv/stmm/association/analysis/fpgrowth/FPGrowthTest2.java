package com.certh.iti.easytv.stmm.association.analysis.fpgrowth;

import java.util.Vector;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import junit.framework.Assert;


public class FPGrowthTest2 {
	
	private FPGrowth fpg;
	private Vector<Itemset> itemsets;
	private int[] counts;
	
	/**
	 * 	Table of transactions
		TID 	List of item IDs
		------------------------
		T100 	P_1=true,P_3 = 0, P_2=false, P_4="small", P_5=1.5
		T200 	P_1=false,P_3 = 25, P_2=true,〖 P〗_4="large",〖 P〗_5=3.5
		T300 	P_1=true,P_3 = 1, P_2=true,〖 P〗_4="large",P_5=3.5
		T400 	P_1=true,P_3 = 0, P_2=false,〖 P〗_4="small",P_5=2.5
		T500 	P_1=false,P_3 = 20, P_2=true,〖 P〗_4="medium",P_5=3.5
		T600 	P_1=true,P_3 = 0, P_2=false,〖 P〗_4="small",〖 P〗_5=1.5
		T700 	P_1=true,P_3 = 0, P_2=false,〖 P〗_4="small",P_5=1.5
		T800 	P_1=false,P_3 = 20, P_2=true,〖 P〗_4="large", P_5=3.5
		T900 	P_1=false,P_3 = 10, P_2=true,〖 P〗_4="large", P_5=3.5
	 */
	
	
	/**
	 * 	Table of transactions
		TID 	List of item IDs
		------------------------
		T100 	2,13, P_2=false, P_4="small", P_5=1.5
		T200 	1,15, P_2=true,〖 P〗_4="large",〖 P〗_5=3.5
		T300 	2,13, P_2=true,〖 P〗_4="large",P_5=3.5
		T400 	2,13, P_2=false,〖 P〗_4="small",P_5=2.5
		T500 	1,15, P_2=true,〖 P〗_4="medium",P_5=3.5
		T600 	2,13, P_2=false,〖 P〗_4="small",〖 P〗_5=1.5
		T700 	2,13, P_2=false,〖 P〗_4="small",P_5=1.5
		T800 	1,15, P_2=true,〖 P〗_4="large", P_5=3.5
		T900 	1,10, P_2=true,〖 P〗_4="large", P_5=3.5
	 */
	
	@BeforeClass
	public void beforTest() {
		itemsets = new Vector<Itemset>();
		
		//widh zero value
		itemsets.add(new Itemset("1,3,4,31,36"));
		itemsets.add(new Itemset("0,3,9,31,36"));
		itemsets.add(new Itemset("1,3,4,31,36"));
		itemsets.add(new Itemset("1,2,4,29,34"));
		itemsets.add(new Itemset("0,3,8,30,36"));
		itemsets.add(new Itemset("1,2,4,29,32"));
		itemsets.add(new Itemset("1,2,4,29,32"));
		itemsets.add(new Itemset("0,3,8,31,36"));
		itemsets.add(new Itemset("0,3,6,31,36"));
			
		//count of distinct items
		counts = new int[37];
		counts[0] = 4;
		counts[1] = 5;
		counts[2] = 3;
		counts[3] = 6;
		counts[4] = 5;
		counts[6] = 1;
		counts[8] = 2;
		counts[9] = 1;
		counts[29] = 3;
		counts[30] = 1;
		counts[31] = 5;
		counts[32] = 2;
		counts[36] = 6;
		
	    fpg = new FPGrowth(itemsets, counts);
	    fpg.findFrequentItemsets(0.5);
	}
	
	
	/**
	 * test the generated frequent itemsets
	 */
	@Test
	public void test_frequentItemSet() {

		Vector<Itemset> expected = new Vector<Itemset>();
		//1, 1,4, 4, 31, 31,36, 3,31,36, 3,31, 3, 3,36, 36
		expected.add(new Itemset("1")); expected.add(new Itemset("1,4"));
		expected.add(new Itemset("4"));
		expected.add(new Itemset("31")); expected.add(new Itemset("31,36"));
		expected.add(new Itemset("3,31,36")); expected.add(new Itemset("3,31")); expected.add(new Itemset("3")); expected.add(new Itemset("3,36")); 
		expected.add(new Itemset("36"));
	     
	    Assert.assertEquals(expected, fpg.getFrequentItemsets());
	}
	
	/**
	 	Itemset		support
	 	-------------------
		{       1}     5
		{     1,4}     5
		{       4}     5
		{      31}     5
		{   31,36}     5
		{ 3,31,36}     5
		{    3,31}     5
		{       3}     6
		{    3,36}     6
		{      36}     6
	 	
	 */
	@Test
	public void test_weight() {
		long[] expected = new long[] {5, 5,
									  5, 5, 5, 5,
									  5, 6, 6, 6};

	     Vector<Itemset> actual = fpg.getFrequentItemsets();
	     	     
	     for(int i = 0; i < actual.size(); i++) {
	    	 Itemset itemset = actual.get(i);
	    	 //System.out.println(String.format("{%8s} %5d",itemset , itemset.getWeight()));
	    	 Assert.assertEquals(expected[i], itemset.getWeight());
	     }
	}

}
