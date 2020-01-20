package com.certh.iti.easytv.stmm.association.analysis;

import java.util.List;
import java.util.Vector;

import com.certh.iti.easytv.stmm.association.analysis.fpgrowth.Itemset;
import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.preference.attributes.Attribute.Bin;

public abstract class AssociationAnalyzer {
	
	protected List<Profile> input;
	protected Vector<Bin> bins;
	protected Vector<Itemset> frequent1 = null;
	protected Vector<Itemset> itemsets = null;
	protected double minSupport = 0.0;
	
	public AssociationAnalyzer(List<Profile> input, Vector<Bin> bins) {
		this.input = input;
		this.bins = bins;
	}

	/**
	 * Get the frequent itemset that has minSupport
	 * 
	 * @param minSupport
	 */
	public abstract Vector<Itemset> getFrequentItemsets(double minSupport);
	
	/**
	 * Get the associated itemsets
	 * @return
	 */
	public abstract Vector<Itemset> getItemsets();
	
}
