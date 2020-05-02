package com.certh.iti.easytv.stmm.association.analysis;

import java.util.List;
import java.util.Vector;

import com.certh.iti.easytv.stmm.association.analysis.fpgrowth.Itemset;
import com.certh.iti.easytv.user.Profile;

public abstract class AssociationAnalyzer {
	
	protected List<Profile> input;
	protected Vector<Itemset> frequent1 = null;
	protected Vector<Itemset> itemsets = null;
	protected double minSupport = 0.0;
	
	public AssociationAnalyzer(List<Profile> input) {
		this.input = input;
	}

	/**
	 * Get the frequent itemset that has minSupport
	 * 
	 * @param minSupport
	 */
	public abstract Vector<Itemset> getFrequentItemsets(double minSupport);
	
	/**
	 * Update the itemset weight and support
	 * @param itemset
	 * @return the same itemset
	 */
	public abstract Itemset updateWeightAndSupport(Itemset itemset);
	
	/**
	 * Get the associated itemsets
	 * @return
	 */
	public abstract Vector<Itemset> getItemsets();
	
}
