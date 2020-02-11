package com.certh.iti.easytv.stmm.association.analysis.rules;

import java.lang.reflect.MalformedParametersException;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Vector;

import com.certh.iti.easytv.stmm.association.analysis.fpgrowth.Itemset;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.BodyRuleConditions;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.HeadRuleConditions;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.RuleCondition;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper.RuleCondition.Argument;
import com.certh.iti.easytv.user.preference.attributes.Attribute.Bin;

/**
 * A converter class that converts assciationRule into an associationRuleWrapper instance
 *
 */
public class AssociationRuleConverter {
	
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
			Bin bin = bins.get(head.get(i));
			
			StringTokenizer stok = new StringTokenizer(bin.label, " -,", false);
			if(stok.countTokens() == 0 || stok.countTokens() > 3)
				throw new MalformedParametersException(bin.label);
			
			heads[i] = new RuleCondition(stok.nextToken(), "EQ" , new Argument[] {new Argument(bin.type.getXMLDataTypeURI(), 
																							   bin.center)});
		}
		
		
		//handle body
		Itemset body = associationRule.getBody();
		RuleCondition[] bodies= new RuleCondition[body.size() * 2];
		int index = 0;
		for(int i = 0; i < body.size(); i++) {
			Bin bin = bins.get(body.get(i));
			
			StringTokenizer stok = new StringTokenizer(bin.label, " -,", false);
			if(stok.countTokens() == 0 || stok.countTokens() > 3)
				throw new MalformedParametersException(bin.label);
			
			String uri = stok.nextToken();
			
			if(bin.range.length == 1)
				bodies[index++] = new RuleCondition(uri, "EQ" , new Argument[] {new Argument(bin.type.getXMLDataTypeURI(), 
																							 bin.range[0])});
			else if(bin.range.length == 2) {
				System.out.println(bin.range[0]+" "+bin.range[1]);
				bodies[index++] = new RuleCondition(uri, "GE" , new Argument[] {new Argument(bin.type.getXMLDataTypeURI(), 
																							 bin.range[0])});
				bodies[index++] = new RuleCondition(uri, "LE" , new Argument[] {new Argument(bin.type.getXMLDataTypeURI(), 
																							 bin.range[1])});
			}
		}
		
		//resize
		bodies = Arrays.copyOf(bodies, index);
		BodyRuleConditions bodyRuleConditions = new BodyRuleConditions(bodies, body.getWeight(), body.getSupport());
		HeadRuleConditions headRuleConditions = new HeadRuleConditions(heads, head.getWeight(), head.getSupport());

		return new AssociationRuleWrapper(bodyRuleConditions, headRuleConditions);
	}
}
