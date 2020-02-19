package com.certh.iti.easytv.stmm.association.analysis.rules;

import java.util.Vector;

import com.certh.iti.easytv.stmm.association.analysis.fpgrowth.Itemset;

public class AssociationRuleFilter {
	
	/**
	 * Filter out useless rules that include items that are greater than item 
	 * 
	 * @param rules
	 */
	public static void filter(Vector<AssociationRule> rules, int maxItem){
		
		int i = 0;
		AssociationRule rule1 = null;
		 while(i < rules.size()) {
			 rule1 = rules.get(i);
			 Itemset head =  rule1.getHead();
			 
			 //check rule head
			 for(int j = 0 ; j < head.size(); j++) 
				 if(head.get(j) >= maxItem) {
					 rules.remove(i);
					 i--;
					 break;
				 }
			 i++;
		 }
	}
	
	/**
	 * Filter out generic rules
	 * 
	 * @param rules
	 */
	public static void filter(Vector<AssociationRule> rules){
		
		AssociationRule rule1 = null, rule2 = null;
		int i = 0, j = 1;
		 while(i < rules.size()) {
			 rule1 = rules.get(i);
			 for(j = i + 1; j < rules.size(); j++) {
				 rule2 = rules.get(j);
				 if(rule1.canSubstituted(rule2)) {
					 rules.remove(j);
					 j--;
				 } else if(rule2.canSubstituted(rule1)){
					 rules.remove(i);
					 i--;
					 break;
				 }
			 }
			 i++;
		 }
	}

}
