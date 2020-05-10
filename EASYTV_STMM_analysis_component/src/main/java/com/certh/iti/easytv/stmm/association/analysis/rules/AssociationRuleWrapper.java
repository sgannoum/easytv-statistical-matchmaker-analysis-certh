package com.certh.iti.easytv.stmm.association.analysis.rules;

import org.json.JSONObject;

public class AssociationRuleWrapper extends RuleWrapper {

	public AssociationRuleWrapper(BodyRuleConditions body, HeadRuleConditions head, double confidence) {
		super(body, head, confidence);
	}
	
	public AssociationRuleWrapper(JSONObject json) {
		super(json);
	}
	
	public AssociationRuleWrapper(String rule) {
		super(rule, 0.0);
	}
	
	public AssociationRuleWrapper(String rule, double confidence) {
		super(rule, confidence);
	}

}
