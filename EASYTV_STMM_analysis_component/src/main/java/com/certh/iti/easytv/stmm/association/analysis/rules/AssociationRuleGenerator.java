package com.certh.iti.easytv.stmm.association.analysis.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.certh.iti.easytv.stmm.association.analysis.AssociationAnalyzer;
import com.certh.iti.easytv.stmm.association.analysis.fpgrowth.Itemset;

public class AssociationRuleGenerator {
	
	private AssociationAnalyzer fpgrowth;
	private int maxItem = Integer.MAX_VALUE;
	
	public AssociationRuleGenerator(AssociationAnalyzer fpgrowth) {
		this.fpgrowth = fpgrowth;
	}
	
	public AssociationRuleGenerator(AssociationAnalyzer fpgrowth, int maxItem) {
		this.fpgrowth = fpgrowth;
		this.maxItem = maxItem;
	}
	
	public Vector<AssociationRule> findAssociationRules(Vector<Itemset> frequentItemset, double minConfidence) {
		Vector<AssociationRule> allRules = new Vector<AssociationRule>();
		
		for(Itemset itemset : frequentItemset) {
				
			Vector<Itemset> subsets = AssociationRuleGenerator.generateAllSubSet(itemset, maxItem);

			//generate all association rules
			Vector<AssociationRule> rules =  generateAllRules(subsets, itemset, minConfidence);
					
			//add
			allRules.addAll(rules);
		}
		
		return allRules;
	}

	/**
	 * Generate all non empty subsets, filtering out all elements that are bigger than maxItem
	 * 
	 * @param itemset
	 * @return
	 */
	protected static Vector<Itemset> generateAllSubSet(Itemset itemset, int maxItem){
		
		List<Vector<Itemset>> intermediatSubSets = new ArrayList<Vector<Itemset>>();
		Vector<Itemset> generatedHeads = new Vector<Itemset>();
		Vector<Itemset> itemSets = new Vector<Itemset>();
		
		//Create one elements subSets of itemsets
		for(int i = 0; i < itemset.size() && itemset.get(i) < maxItem; i++) {						
			Itemset subSet1 = new Itemset(1);
			subSet1.add(itemset.get(i));
			itemSets.add(subSet1);
			generatedHeads.add(subSet1);
		}
		
		//add
		intermediatSubSets.add(itemSets);
		
		//Combines subSets together
		while(!intermediatSubSets.isEmpty()) {
			itemSets = intermediatSubSets.remove(0);
			
			for(int i = 0; i < itemSets.size(); i++) {
				Itemset subSet1 = itemSets.get(i);
				Vector<Itemset> newItemSets = new Vector<Itemset>();

				//Exclude all items itemsets
				if(subSet1.size() == itemset.size() - 1) 
					continue;

				for(int j = i + 1;  j < itemSets.size(); j++) {
					Itemset subSet2 = itemSets.get(j);
					Itemset combinedSubSet = subSet1.combineWith(subSet2);
					
					generatedHeads.add(combinedSubSet);
					newItemSets.add(combinedSubSet);
				}
				
				//add
				intermediatSubSets.add(newItemSets);
			}
		}
				
		return generatedHeads;
	}
	
	/**
	 * Generate all non empty subsets
	 * 
	 * @param itemset
	 * @return
	 */
	protected static Vector<Itemset> generateAllSubSet(Itemset itemset){
		return generateAllSubSet(itemset,  Integer.MAX_VALUE);
	}
	
	/**
	 * Generate all association rules
	 * 
	 * @param headItemsets
	 * @param itemset
	 */
	protected Vector<AssociationRule> generateAllRules(Vector<Itemset> headItemsets, Itemset itemset, double minConfidence) {
		Vector<AssociationRule> rules = new Vector<AssociationRule>(headItemsets.size());
				
		for(int i = 0; i < headItemsets.size(); i++) {
			Itemset head = headItemsets.get(i);
			Itemset body = Itemset.subtraction(itemset, head);
			Itemset union = Itemset.union(itemset, head);
			
			if(body.size() == 0)
				continue;
			
			//update weight and support
			fpgrowth.updateWeightAndSupport(head);
			fpgrowth.updateWeightAndSupport(body);
			fpgrowth.updateWeightAndSupport(union);
						
			//calculate rule confidence
			double confidence = (union.getWeight() * 1.0) / body.getWeight();
			if(confidence >= minConfidence) 
				rules.add(new AssociationRule(body, head, union, confidence));
		}
		
		return rules;
	}

}
