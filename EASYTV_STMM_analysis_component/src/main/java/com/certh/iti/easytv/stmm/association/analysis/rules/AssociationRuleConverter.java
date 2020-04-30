package com.certh.iti.easytv.stmm.association.analysis.rules;

import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Logger;

import com.certh.iti.easytv.stmm.association.analysis.fpgrowth.Itemset;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.BodyRuleConditions;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.HeadRuleConditions;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.RuleCondition;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.RuleCondition.Argument;
import com.certh.iti.easytv.user.preference.attributes.AttributesAggregator.Association;
import com.certh.iti.easytv.user.preference.attributes.discretization.Discrete;
import com.certh.iti.easytv.user.preference.attributes.AttributesAggregator;

/**
 * A converter class that converts assciationRule into an associationRuleWrapper instance
 *
 */
public class AssociationRuleConverter {
	
	private final static Logger logger = Logger.getLogger(AssociationRuleConverter.class.getName());

	
	private AttributesAggregator aggregator;
	private Vector<AssociationRuleWrapper> rules = new  Vector<AssociationRuleWrapper>();
	
	public AssociationRuleConverter(AttributesAggregator aggregator) {
		this.aggregator = aggregator;
	}
	
	/**
	 * Convert Association rules into AssociationRuleWrapper after filtering rules that:
	 * 1-) have contextual or content information in their head part
	 * 2-) are considered contained in other rules
	 * 
	 * @param rules
	 * @return
	 */
	public Vector<AssociationRuleWrapper> convert(Vector<AssociationRule> rules) {
		
		//filter out rules
		Vector<AssociationRule> removed = AssociationRuleFilter.filter(rules);
		logger.info(String.format("%d rules remains after filtering generic association rules", rules.size()));

		
		for(AssociationRule associationRule : rules) {
			AssociationRuleWrapper rule = convert(associationRule);
			this.rules.add(rule);
		}
		
		return this.rules;
	}
	
	/**
	 * Convert an AssociationRule instance into AssociationRuleWrapper
	 * 
	 * @param associationRule an instance
	 * @return
	 */
	public AssociationRuleWrapper convert(AssociationRule associationRule) {

		//handle head
		Itemset head = associationRule.getHead();
		RuleCondition[] heads = new RuleCondition[head.size()];		
		for(int i = 0; i < head.size(); i++) {			
			Association<String, Discrete> association = aggregator.decode(head.get(i));
			heads[i] = new RuleCondition(association.getUri(), "EQ" , new Argument[] {new Argument(association.getValue().getXMLDataTypeURI(), association.getValue().getCenter())});
		}
		
		//handle body
		Itemset body = associationRule.getBody();
		RuleCondition[] bodies= new RuleCondition[body.size() * 2];
		int index = 0;
		for(int i = 0; i < body.size(); i++) {			
			Association<String, Discrete> association = aggregator.decode(body.get(i));
			Discrete discrete = association.getValue();
			
			if(discrete.getRange().length == 1)
				bodies[index++] = new RuleCondition(association.getUri(), "EQ" , new Argument[] {new Argument(discrete.getXMLDataTypeURI(), discrete.getRange()[0])});
			else {
				bodies[index++] = new RuleCondition(association.getUri(), "GE" , new Argument[] {new Argument(discrete.getXMLDataTypeURI(), discrete.getRange()[0])});
				bodies[index++] = new RuleCondition(association.getUri(), "LE" , new Argument[] {new Argument(discrete.getXMLDataTypeURI(), discrete.getRange()[1])});
			}
		}
		
		//resize
		bodies = Arrays.copyOf(bodies, index);
		BodyRuleConditions bodyRuleConditions = new BodyRuleConditions(bodies, body.getWeight(), body.getSupport());
		HeadRuleConditions headRuleConditions = new HeadRuleConditions(heads, head.getWeight(), head.getSupport());

		return new AssociationRuleWrapper(bodyRuleConditions, headRuleConditions);
	}

}
