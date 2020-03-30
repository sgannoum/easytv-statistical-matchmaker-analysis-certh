package com.certh.iti.easytv.stmm.association.analysis.rules;

import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Logger;

import org.apache.commons.math3.exception.OutOfRangeException;

import com.certh.iti.easytv.stmm.association.analysis.fpgrowth.Itemset;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.BodyRuleConditions;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.HeadRuleConditions;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.RuleCondition;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.RuleCondition.Argument;
import com.certh.iti.easytv.user.preference.Preference;
import com.certh.iti.easytv.user.preference.attributes.Attribute.Bin;

/**
 * A converter class that converts assciationRule into an associationRuleWrapper instance
 *
 */
public class AssociationRuleConverter {
	
	private final static Logger logger = Logger.getLogger(AssociationRuleConverter.class.getName());

	
	private Vector<Bin> bins;
	private Vector<AssociationRuleWrapper> rules = new  Vector<AssociationRuleWrapper>();
	
	public AssociationRuleConverter(Vector<Bin> bins) {
		this.bins = bins;
	}
	
	/**
	 * Convert Association rules into AssociationRuleWrapper
	 * @param rules
	 * @return
	 */
	public Vector<AssociationRuleWrapper> convert(Vector<AssociationRule> rules) {
		
		//get preference maximum item value
		int maxItem = Preference.getBinNumber();
		
		//keep only rules that have preferences item in their head section
		AssociationRuleFilter.filter(rules, maxItem);
		logger.info(String.format("%d rules remains after filtering rules with non preferences items in their head section", rules.size()));

		
		//filter out rules
		AssociationRuleFilter.filter(rules);
		logger.info(String.format("%d rules remains after filtering generic association rules", rules.size()));

		
		for(AssociationRule associationRule : rules) {
			AssociationRuleWrapper rule = convert(associationRule);
			this.rules.add(rule);
		}
		
		return this.rules;
	}
	
	/**
	 * Convert an AssociationRule instance into AssociationRuleWrapper
	 * @param associationRule an instance
	 * @return
	 */
	public AssociationRuleWrapper convert(AssociationRule associationRule) {

		//handle head
		Itemset head = associationRule.getHead();
		RuleCondition[] heads = new RuleCondition[head.size()];
		for(int i = 0; i < head.size(); i++) {
			int binId = head.get(i);
			
			if(binId < 0 || binId > bins.size() - 1)
				throw new OutOfRangeException(binId, 0, bins.size() - 1);
			
			Bin bin = bins.get(binId);
			heads[i] = new RuleCondition(bin.label, "EQ" , new Argument[] {new Argument(bin.type.getXMLDataTypeURI(), bin.center)});
		}
		
		//handle body
		Itemset body = associationRule.getBody();
		RuleCondition[] bodies= new RuleCondition[body.size() * 2];
		int index = 0;
		for(int i = 0; i < body.size(); i++) {
			int binId = body.get(i);
			
			if(binId < 0 || binId > bins.size() - 1)
				throw new OutOfRangeException(binId, 0, bins.size() - 1);
			
			Bin bin = bins.get(binId);
			if(bin.range.length == 1)
				bodies[index++] = new RuleCondition(bin.label, "EQ" , new Argument[] {new Argument(bin.type.getXMLDataTypeURI(),  bin.range[0])});
			else if(bin.range.length == 2) {
				bodies[index++] = new RuleCondition(bin.label, "GE" , new Argument[] {new Argument(bin.type.getXMLDataTypeURI(),  bin.range[0])});
				bodies[index++] = new RuleCondition(bin.label, "LE" , new Argument[] {new Argument(bin.type.getXMLDataTypeURI(),  bin.range[1])});
			}
		}
		
		//resize
		bodies = Arrays.copyOf(bodies, index);
		BodyRuleConditions bodyRuleConditions = new BodyRuleConditions(bodies, body.getWeight(), body.getSupport());
		HeadRuleConditions headRuleConditions = new HeadRuleConditions(heads, head.getWeight(), head.getSupport());

		return new AssociationRuleWrapper(bodyRuleConditions, headRuleConditions);
	}

}
