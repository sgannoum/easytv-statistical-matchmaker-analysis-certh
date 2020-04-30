package com.certh.iti.easytv.stmm.association.analysis.rules;

import java.util.Vector;

import com.certh.iti.easytv.stmm.association.analysis.fpgrowth.Itemset;

public class AssociationRuleFilter {
	
	/**
	 * Filter out useless rules that include items that are greater than item 
	 * 
	 * @param rules
	 */
	public static Vector<AssociationRule> filter(Vector<AssociationRule> rules, int maxItem){
		
		int i = 0;
		AssociationRule rule1 = null;
		Vector<AssociationRule> removed = new Vector<AssociationRule>();
		 while(i < rules.size()) {
			 rule1 = rules.get(i);
			 Itemset head =  rule1.getHead();
			 
			 //check rule head
			 for(int j = 0 ; j < head.size(); j++) 
				 if(head.get(j) >= maxItem) {
					 removed.add(rules.remove(i));
					 i--;
					 break;
				 }
			 i++;
		 }
		 
		 return removed;
	}
	
	/**
	 * Filter out generic rules
	 * 
	 * @param rules
	 */
	public static Vector<AssociationRule> filter(Vector<AssociationRule> rules){
		
		AssociationRule rule1 = null, rule2 = null;
		Vector<AssociationRule> removed = new Vector<AssociationRule>();
		int i = 0, j = 1;
		 while(i < rules.size()) {
			 rule1 = rules.get(i);
			 for(j = i + 1; j < rules.size(); j++) {
				 rule2 = rules.get(j);
				 if(rule1.canSubstituted(rule2)) {
					 removed.add(rules.remove(j));
					 j--;
				 } else if(rule2.canSubstituted(rule1)){
					 removed.add(rules.remove(i));
					 i--;
					 break;
				 }
			 }
			 i++;
		 }
		 
		 return removed;
	}

}
