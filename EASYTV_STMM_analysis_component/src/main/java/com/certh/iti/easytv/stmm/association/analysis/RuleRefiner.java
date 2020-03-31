package com.certh.iti.easytv.stmm.association.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.logging.Logger;

import com.certh.iti.easytv.stmm.association.analysis.fpgrowth.FPGrowthWrapper;
import com.certh.iti.easytv.stmm.association.analysis.fpgrowth.Itemset;
import com.certh.iti.easytv.stmm.association.analysis.rules.AssociationRule;
import com.certh.iti.easytv.stmm.association.analysis.rules.AssociationRuleConverter;
import com.certh.iti.easytv.stmm.association.analysis.rules.AssociationRuleGenerator;
import com.certh.iti.easytv.stmm.association.analysis.rules.AssociationRuleWrapper;
import com.certh.iti.easytv.stmm.association.analysis.rules.RbmmRuleWrapper;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper;
import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.preference.attributes.Attribute.Bin;

public class RuleRefiner {
	
	private final static Logger logger = Logger.getLogger(RuleRefiner.class.getName());
	
	private Vector<Bin> bins;
	private Vector<Itemset> frequentItemset;
	private Vector<AssociationRule> associationRules;
	private Vector<RuleWrapper> associationRulesWrapper;

	private double minSupport = 0, minConfidence = 0;

	
	public RuleRefiner(Vector<Bin> bins) {
		this.bins = bins;
	}
	
	public double getMinSupport() {
		return minSupport;
	}
	
	public double getMinConfidence() {
		return minConfidence;
	}

	public Vector<Itemset> getFrequentItemsete() {
		return frequentItemset;
	}
	
	public Vector<AssociationRule> getAssociationRules() {
		return associationRules;
	}
	
	/**
	 * Given the set of user profiles and the 
	 * @param profiles a collection of user profiles 
	 * @param rbmmRules rbmms set of rules
	 * @param minSupport minimum support for itemset
	 * @param minConfidence minimum confidence for rules
	 * @return a collection of refined rules
	 */
	public Vector<RuleWrapper> refineRules(List<Profile> profiles, Vector<RbmmRuleWrapper> rbmmRules, double minSupport, double minConfidence) {
		
		this.minSupport = minSupport;
		this.minConfidence = minConfidence;
		
		//Create fp-growth instance and get profiles itemsets
		logger.info("Start association analysis");
		AssociationAnalyzer fpgrowth = new FPGrowthWrapper(profiles, bins);
		
		//Association rules generator
		logger.info("Start association rules generating process");
		AssociationRuleGenerator ruleGenerator = new AssociationRuleGenerator(fpgrowth.getItemsets());
		
		//Assocation rule converter
		AssociationRuleConverter rulesConverter = new AssociationRuleConverter(bins);
		
		logger.info(String.format("Generate rules with  Minimume support: %.1f, Minimume confidence: %.1f", minSupport, minConfidence));
		frequentItemset = fpgrowth.getFrequentItemsets(minSupport);
		
		logger.info(String.format("Found %d frequent itemsets with minSupport: %f and minConfidence: %f", frequentItemset.size(), minSupport, minConfidence));
		associationRules = ruleGenerator.findAssociationRules(frequentItemset, minConfidence);
		
		logger.info(String.format("Found %d association rules", associationRules.size()));
		Vector<AssociationRuleWrapper> asRules = rulesConverter.convert(associationRules);
		
		logger.info(String.format("Refine converted %d association rules with RBMM %d rules...", asRules.size(), rbmmRules.size()));
		associationRulesWrapper = RuleRefiner.refineRules(asRules, rbmmRules);
		
		//Refine rules
		return associationRulesWrapper;
	}
	
	/**
	 * Given two lists of rules find:
	 * 	- RBMM rules is to updated
	 * 	- RBMM rules to be removed
	 * 	- Association rules to be added
	 * 
	 * @param asRules
	 * @param rbmmRules
	 * @return
	 */
	public static  Vector<RuleWrapper> refineRules(Vector<AssociationRuleWrapper> asRules, Vector<RbmmRuleWrapper> rbmmRules){
		//Classify rules cases into the results of set actions between the two sets
		ArrayList<RuleWrapper> list;
		HashMap<Integer, ArrayList<RuleWrapper>> maps = new HashMap<Integer, ArrayList<RuleWrapper>>();
		
		//Add association rules first
		for(AssociationRuleWrapper rule : asRules) {
			int hash = rule.hashCode();
			if((list = maps.get(hash)) != null) {				
				list.get(0).merge(rule.getHead());
				logger.info(String.format("Association rule to be merged %s", rule.toString()));
			} else {
				list = new ArrayList<RuleWrapper>();
				list.add(rule);
				maps.put(hash, list);
			}
		}
		
		//Then add rbmm rules
		for(RbmmRuleWrapper rule : rbmmRules) {
			int hash = rule.hashCode();
			if((list = maps.get(hash)) != null) {
				list.add(rule);
			} else {
				list = new ArrayList<RuleWrapper>();
				list.add(rule);
				maps.put(hash, list);
			}
		}
		
		//Handle rules
		RuleWrapper key;
		AssociationRuleWrapper as;
		RbmmRuleWrapper rb;
		Vector<RuleWrapper> resutls = new Vector<RuleWrapper>();
		for(Entry<Integer, ArrayList<RuleWrapper>> entry : maps.entrySet()) {
			list = entry.getValue();
			key = list.get(0);
			
			if(AssociationRuleWrapper.class.isInstance(key)) {
				
				//Check for two cases: 
				//	Case A: adding a rule when only one association rule instance exists
				//	Case B: updating a rule when two instances of different classes exists.
				
				if(list.size() == 1) {
					as = (AssociationRuleWrapper) list.get(0);
					resutls.add(as);
					logger.info(String.format("Association rule to be added %s", as.toString()));

				} else if(list.size() == 2) {
					as = (AssociationRuleWrapper) list.get(0);
					rb = (RbmmRuleWrapper) list.get(1);
					as.merge(rb.getHead());
					resutls.add(as);
					logger.info(String.format("RBMM rule to be updated %s", rb.toString()));

				} else if(list.size() > 2) 
					throw new IllegalStateException("More than two rules are matched..."+ list.toString());
				
				
			} else if (RbmmRuleWrapper.class.isInstance(key)) {
				 	//Rule to be deleted from rbmm
					logger.info(String.format("RBMM rule to be deleted %s", key.toString()));
			} else 
				throw new IllegalStateException("Unkown class type " + key.getClass().getName());	
		}
		
		return resutls;
	}
	
}