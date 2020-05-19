package com.certh.iti.easytv.stmm.association.analysis;

import java.util.HashMap;
import java.util.List;
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
import com.certh.iti.easytv.user.preference.attributes.AttributesAggregator;

public class RuleRefiner {
	
	private final static Logger logger = Logger.getLogger(RuleRefiner.class.getName());
	
	private AttributesAggregator aggregator;
	private List<Profile> profiles;
	private Vector<Itemset> frequentItemset;
	private Vector<AssociationRule> associationRules;
	private Vector<AssociationRuleWrapper> asRules;
	private AssociationRuleConverter rulesConverter;

	private double minSupport = 0, minConfidence = 0;

	
	public RuleRefiner(AttributesAggregator aggregator, List<Profile> profiles, double minSupport, double minConfidence) {
		this.aggregator = aggregator;
		this.minSupport = minSupport;
		this.minConfidence = minConfidence;
		this.profiles = profiles;
		this.rulesConverter = new AssociationRuleConverter(this.aggregator);
		
		//Create fp-growth instance and get profiles itemsets
		logger.info("Start association analysis...");
		AssociationAnalyzer fpgrowth = new FPGrowthWrapper(this.profiles, this.aggregator);
		frequentItemset = fpgrowth.getFrequentItemsets(minSupport);
		logger.info(String.format("Found %d frequent itemsets with minSupport: %.1f", frequentItemset.size(), minSupport));
		
		//filter out contextual information from rules head
		int maxItem = this.aggregator.getGroupBase("contextual attributes");

		//Association rules generator
		logger.info("Start association rules, filtering out itemset biggern than "+maxItem+"...");
		AssociationRuleGenerator ruleGenerator = new AssociationRuleGenerator(fpgrowth, maxItem);
		associationRules = ruleGenerator.findAssociationRules(frequentItemset, minConfidence);
		logger.info(String.format("Found %d association rules minimum confidence: %.1f", associationRules.size(), minConfidence));
		
		logger.info("Start converting rules...");
		asRules = rulesConverter.convert(associationRules);
		logger.info("Finished converting rules...");
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
	
	public Vector<AssociationRuleWrapper> getAssociationRulesWrapper() {
		return asRules;
	}
	
	/**
	 * Given the set of user profiles and the 
	 * @param profiles a collection of user profiles 
	 * @param rbmmRules rbmms set of rules
	 * @param minSupport minimum support for itemset
	 * @param minConfidence minimum confidence for rules
	 * @return a collection of refined rules
	 */
	public Vector<RuleWrapper> refineRules(Vector<RbmmRuleWrapper> rbmmRules) {
		
		logger.info(String.format("Refine converted %d association rules with RBMM %d rules...", asRules.size(), rbmmRules.size()));
		return RuleRefiner.refineRules(asRules, rbmmRules);
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
	public static Vector<RuleWrapper> refineRules(Vector<AssociationRuleWrapper> asRules, Vector<RbmmRuleWrapper> rbmmRules){
		//Classify rules cases into the results of set actions between the two sets
		AssociationRuleWrapper assValue;
		Vector<RuleWrapper> resutls = new Vector<RuleWrapper>();
		HashMap<Integer, AssociationRuleWrapper> maps = new HashMap<Integer, AssociationRuleWrapper>();
		
		//Classity association rules first
		for(AssociationRuleWrapper assRule : asRules) {
			int key = assRule.hashCode();
			if((assValue = maps.get(key)) != null) {				
				assValue.merge(assRule.getHead());
				logger.info(String.format("Association rule to be merged %s", assRule.toString()));
			} else 
				maps.put(key, assRule);
		}
		
		//Check for two cases: 
		//	Case A: updating an RBMM rule
		//	Case B: Deleting an RBMM rule when no association rule exists
		//	Case C: Add a new 
		for(RbmmRuleWrapper rbmmRule : rbmmRules) {
			int key = rbmmRule.hashCode();
			if((assValue = maps.remove(key)) != null) {
				assValue.merge(rbmmRule.getHead());
				resutls.add(assValue);
				logger.info(String.format("RBMM rule to be updated %s", rbmmRule.toString()));
			} else {
			 	//Rule to be deleted from rbmm
				logger.info(String.format("RBMM rule to be deleted %s", rbmmRule.toString()));
			}
		}
		
		//Check for the case: 
		//	Case C: Add a new association rule
		for(AssociationRuleWrapper entry : maps.values()) {
			resutls.add(entry);
			logger.info(String.format("Association rule to be added %s", entry.toString()));	
		} 
		
		return resutls;
	}
	
}