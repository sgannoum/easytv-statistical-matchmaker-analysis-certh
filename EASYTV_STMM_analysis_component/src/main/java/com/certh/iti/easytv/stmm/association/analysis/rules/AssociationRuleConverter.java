package com.certh.iti.easytv.stmm.association.analysis.rules;

import java.lang.reflect.MalformedParametersException;
import java.util.StringTokenizer;
import java.util.Vector;

import com.certh.iti.easytv.stmm.association.analysis.fpgrowth.Itemset;
import com.certh.iti.easytv.stmm.association.analysis.rules.AssociationRuleWrapper.BodyRuleCondition;
import com.certh.iti.easytv.stmm.association.analysis.rules.AssociationRuleWrapper.HeadRuleCondition;
import com.certh.iti.easytv.user.preference.attributes.Attribute.Bin;

public class AssociationRuleConverter {
	
	private Vector<Bin> bins;
	private Vector<AssociationRuleWrapper> rules = new  Vector<AssociationRuleWrapper>();
	
	public AssociationRuleConverter(Vector<Bin> bins) {
		this.bins = bins;
	}
	
	public Vector<AssociationRuleWrapper> getRules(Vector<AssociationRule> rules) {
		
		for(AssociationRule associationRule : rules) {
			
			//handle head
			Itemset head = associationRule.getHead();
			HeadRuleCondition[] heads = new HeadRuleCondition[head.size()];
			for(int i = 0; i < head.size(); i++) {
				Bin bin = bins.get(head.get(i));
				
				StringTokenizer stok = new StringTokenizer(bin.label, " -,", false);
				if(stok.countTokens() == 0 || stok.countTokens() > 3)
					throw new MalformedParametersException(bin.label);
				
				heads[i] = new AssociationRuleWrapper.HeadRuleCondition(stok.nextToken(), bin.range);
			}
			
			
			//handle body
			Itemset body = associationRule.getBody();
			BodyRuleCondition[] bodies= new BodyRuleCondition[body.size()];
			for(int i = 0; i < body.size(); i++) {
				Bin bin = bins.get(body.get(i));
				
				StringTokenizer stok = new StringTokenizer(bin.label, " -,", false);
				if(stok.countTokens() == 0 || stok.countTokens() > 3)
					throw new MalformedParametersException(bin.label);
				
				bodies[i] = new AssociationRuleWrapper.BodyRuleCondition(stok.nextToken(), bin.center);
			}
			
			//add rule
			this.rules.add(new AssociationRuleWrapper(associationRule, heads, bodies));
		}
		
		return this.rules;
	}
/*		
	public JSONObject convert(Vector<AssociationRule> rules) {
		
		JSONArray jsonRules = new JSONArray();
		
		for(AssociationRule associationRule : rules) {
			JSONObject jsonRule = new JSONObject();
			
			Itemset head = associationRule.getHead();
			Itemset body = associationRule.getBody();
			
			JSONArray ruleHead = convertHead(head);
			JSONArray ruleBody = convertBody(body);
			
			jsonRule.put("head", ruleHead);
			jsonRule.put("body", ruleBody);
			
			//add to rules
			jsonRules.put(jsonRule);
		}
		
		JSONObject json = new JSONObject().put("rules", jsonRules);
		
		return json;
	}
	
	private JSONArray convertHead(Itemset itemset) {
		
		JSONArray conditions = new JSONArray();
		
		for(int i = 0; i < itemset.size(); i++) {
			int code = itemset.get(i);
			Bin bin = bins.get(code);
			String preference = bin.label;
		
			StringTokenizer stok = new StringTokenizer(preference, " -,", false);
			if(stok.countTokens() == 0 || stok.countTokens() > 3)
				throw new MalformedParametersException(preference);
			
			JSONObject condition = new JSONObject();
			condition.put("preference", stok.nextElement());
			
			if(stok.countTokens() == 2) 
				condition.put("in-range", new JSONArray().put(stok.nextElement()).put(stok.nextElement()));
			else if(stok.countTokens() == 1)
				condition.put("equals", new JSONArray().put(stok.nextElement()));

			//add
			conditions.put(condition);
		}
		return conditions;
	}
	
	private JSONArray convertBody(Itemset itemset) {
		
		JSONArray conditions = new JSONArray();
		
		for(int i = 0; i < itemset.size(); i++) {
			int code = itemset.get(i);
			Bin bin = bins.get(code);
			String lable = bin.label;
		
			StringTokenizer stok = new StringTokenizer(lable, " -,", false);
			if(stok.countTokens() == 0 || stok.countTokens() > 3)
				throw new MalformedParametersException(lable);
			
			JSONObject condition = new JSONObject();
			condition.put("preference", stok.nextElement());
			
			condition.put("equals", new JSONArray().put(bin.center));

			//add
			conditions.put(condition);
		}
		return conditions;
	}
*/
}
