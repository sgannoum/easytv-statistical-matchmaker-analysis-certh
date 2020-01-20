package com.certh.iti.easytv.stmm.association.analysis.fpgrowth;

import java.util.List;
import java.util.Vector;

import com.certh.iti.easytv.stmm.association.analysis.AssociationAnalyzer;
import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.preference.attributes.Attribute.Bin;

public class FPGrowthWrapper extends AssociationAnalyzer {

	private Vector<Itemset> itemsets;
	private FPGrowth fpg;
	
	public FPGrowthWrapper(List<Profile> profiles, Vector<Bin> bins) {
		super(profiles, bins);
 
        //counts
		int index = 0;
        int[] counts = new int[bins.size() + 1];
        for(Bin bin : bins)
        	counts[index++] = bin.counts;
        
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
	
	@Override
	public Vector<Itemset> getFrequentItemsets(double minSupport) {
		this.minSupport = minSupport;
		fpg.findFrequentItemsets(minSupport);
   	    return fpg.getFrequentItemsets();
	}
	
	@Override
	public Vector<Itemset> getItemsets() {
		return itemsets;
	}

	@Override
	public String toString() {
		String statistics = "";
		
		System.out.println("Size: " +bins.size());
		
		 for(int i = 0; i < bins.size(); i++) {
			 Bin bin = bins.get(i);
			 if(bin.counts != 0)
				 statistics += String.format("|%-5d|%-100s|%-10d|\n", i, bin.label, bin.counts);
		 }
		 
		 String line = String.format("%-118s", " ").replaceAll(" ", "+");

		 return  String.format("%s\n" +
				 			   "|%-5s|%-100s|%-10s|\n"+
				 			    "%s\n" +
				 			    "%s" +
				 			    "%s" 
				 			   	, 
				 			   	
				 			   	line,
				 			   " Id", "Lable" , "Counts",
				 			   	line,
				 			    statistics,
				 				line);
	}

}
