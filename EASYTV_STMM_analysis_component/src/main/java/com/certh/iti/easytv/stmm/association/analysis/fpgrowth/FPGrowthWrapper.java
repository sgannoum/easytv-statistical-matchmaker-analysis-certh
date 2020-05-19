package com.certh.iti.easytv.stmm.association.analysis.fpgrowth;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.certh.iti.easytv.stmm.association.analysis.AssociationAnalyzer;
import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.preference.attributes.AttributesAggregator;
import com.certh.iti.easytv.user.preference.attributes.discretization.Discrete;

public class FPGrowthWrapper extends AssociationAnalyzer {

	private Vector<Itemset> itemsets;
	private FPGrowth fpg;
	
	public FPGrowthWrapper(List<Profile> profiles, AttributesAggregator aggregator) {
		super(profiles);
 
        //counts
		int index = 0;
        int[] counts = new int[aggregator.getBinNumber() + 1];

        for(Iterator<Discrete> iterator = aggregator.discreteIterator(); iterator.hasNext(); )
        	counts[index++] = iterator.next().getCounts();
        
        //vector
        itemsets = new Vector<Itemset>(profiles.size());
        for(Profile profile : profiles) {
        	int[] profileInt = profile.getAsItemSet();
        	Itemset itemset = new Itemset(profileInt);
        	itemsets.add(itemset);
        }
        
        //create FP-growth
        this.fpg = new FPGrowth(itemsets, counts);
	}
	
	
	public FPGrowthWrapper(Vector<Itemset> input, int[] counts) {
		super(null);
        
        //create FP-growth
        this.fpg = new FPGrowth(input, counts);
	}
	
	@Override
	public Vector<Itemset> getFrequentItemsets(double minSupport) {
		fpg.findFrequentItemsets(minSupport);
   	    return fpg.getFrequentItemsets();
	}
	
	@Override
	public Itemset updateWeightAndSupport(Itemset itemset) {
		return fpg.updateWeightAndSupport(itemset);
	}
	
	@Override
	public Vector<Itemset> getItemsets() {
		return itemsets;
	}


	@Override
	public Vector<Itemset> getFrequentItemsets(int minWeight) {
		fpg.findFrequentItemsets(minWeight);
   	    return fpg.getFrequentItemsets();
	}

}
