package com.certh.iti.easytv.stmm.association.analysis.rules;

import org.json.JSONObject;

public class RbmmRuleWrapper extends RuleWrapper {

	public RbmmRuleWrapper(BodyRuleConditions body, HeadRuleConditions head) {
		super(body, head);
	}
	
	public RbmmRuleWrapper(JSONObject json) {
		super(json);
	}
	

}
