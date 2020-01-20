package com.certh.iti.easytv.stmm.association.analysis.fpgrowth;

import java.util.Vector;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import junit.framework.Assert;


public class FPTreeTest {
	
	private FPGrowth fpg;
	private FPTree fpt;
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
	    fpg.findFrequentItemsets(0.7);
	    
	    fpt = fpg.getFPTree();
	}

}
