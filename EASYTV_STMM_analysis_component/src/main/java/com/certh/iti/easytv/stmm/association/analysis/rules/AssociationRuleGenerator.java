package com.certh.iti.easytv.stmm.association.analysis.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.certh.iti.easytv.stmm.association.analysis.fpgrowth.Itemset;

public class AssociationRuleGenerator {
	
	private Vector<Itemset> itemsets;
	
	public AssociationRuleGenerator(Vector<Itemset> itemsets) {
		this.itemsets = itemsets;
	}
	
	public Vector<AssociationRule> findAssociationRules(Vector<Itemset> frequentItemset, double minConfidence) {
		Vector<AssociationRule> allRules = new Vector<AssociationRule>();
		
		for(Itemset itemset : frequentItemset) {
			//generate all non-empty sub sets
			Vector<Itemset> subsets = AssociationRuleGenerator.generateAllSubSet(itemset);
			
			//generate all association rules
			Vector<AssociationRule> rules =  AssociationRuleGenerator.generateAllRules(this.itemsets, subsets, itemset, minConfidence);
			
			//add
			allRules.addAll(rules);
		}
		return allRules;
	}
	
	/**
	 * Generate all non empty subsets
	 * 
	 * @param itemset
	 * @return
	 */
	public static Vector<Itemset> generateAllSubSet(Itemset itemset){
		//TODO mine association trees for frequent itemsets support
		
		List<Vector<Itemset>> intermediatSubSets = new ArrayList<Vector<Itemset>>();
		Vector<Itemset> subsets = new Vector<Itemset>();
		Vector<Itemset> itemSets = new Vector<Itemset>();
		
		//Create one elements subSets of itemsets
		for(int i = 0; i < itemset.size(); i++) {
			Itemset subSet1 = new Itemset(1);
			subSet1.add(itemset.get(i));
			itemSets.add(subSet1);
			subsets.add(subSet1);
		}
		
		//add
		intermediatSubSets.add(itemSets);

		//Combines subSets together
		while(!intermediatSubSets.isEmpty()) {
			itemSets = intermediatSubSets.remove(0);
			
			for(int i = 0; i < itemSets.size(); i++) {
				Itemset subSet1 = itemSets.get(i);
				Vector<Itemset> newItemSets = new Vector<Itemset>();
				
				for(int j = i + 1;  j < itemSets.size(); j++) {
					Itemset subSet2 = itemSets.get(j);
					Itemset combinedSubSet = subSet1.combineWith(subSet2);
					
					subsets.add(combinedSubSet);
					newItemSets.add(combinedSubSet);
				}
				//add
				intermediatSubSets.add(newItemSets);
			}
		}
		return subsets;
	}
	
	/**
	 * Generate all association rules
	 * 
	 * @param subsetItemsets
	 * @param itemset
	 */
	public static Vector<AssociationRule> generateAllRules(Vector<Itemset> itemsets, Vector<Itemset> subsetItemsets, Itemset itemset, double minConfidence) {
		Vector<AssociationRule> rules = new Vector<AssociationRule>(subsetItemsets.size());
				
		for(int i = 0; i < subsetItemsets.size(); i++) {
			Itemset head = subsetItemsets.get(i);
			Itemset body = Itemset.subtraction(itemset, head);
			Itemset union = Itemset.union(itemset, head);
			
			long union_counts = 0;
			long head_counts = 0;
			long body_counts = 0;
			double confidence = 0;
			
			//TODO find a more efficient way to store and compare itemsets
			//measure counts
			for(Itemset item : itemsets) {
				if(union.isIncludedIn(item)) union_counts++;
				if(head.isIncludedIn(item)) head_counts++;
				if(body.isIncludedIn(item)) body_counts++;
			}
			
			//calculate rule confidence
			confidence = (union_counts * 1.0) / head_counts;
			union.setWeight(union_counts);
			head.setWeight(head_counts);
			body.setWeight(body_counts);
			if(body.size() != 0 && confidence >= minConfidence) 
				rules.add(new AssociationRule(body, head, union, confidence));
		}
		
		return rules;
	}

}
