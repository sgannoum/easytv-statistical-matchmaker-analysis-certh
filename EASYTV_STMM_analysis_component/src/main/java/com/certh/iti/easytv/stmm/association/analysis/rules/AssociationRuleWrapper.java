package com.certh.iti.easytv.stmm.association.analysis.rules;

import org.json.JSONObject;

public class AssociationRuleWrapper extends RuleWrapper {

	public AssociationRuleWrapper(BodyRuleConditions body, HeadRuleConditions head) {
		super(body, head);
	}
	
	public AssociationRuleWrapper(JSONObject json) {
		super(json);
	}
	

}
