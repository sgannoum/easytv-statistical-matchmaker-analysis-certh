package com.certh.iti.easytv.stmm.association.analysis.rules;

import java.io.File;
import java.io.IOException;

import org.json.JSONObject;

public class RbmmRuleWrapper extends RuleWrapper {

	public RbmmRuleWrapper(BodyRuleConditions body, HeadRuleConditions head) {
		super(body, head);
	}
	
	public RbmmRuleWrapper(JSONObject json) {
		super(json);
	}
	
	public RbmmRuleWrapper(File file) throws IOException {
		super(file);
	}
	
	public RbmmRuleWrapper(String rule) {
		super(rule, 0,0,0,0);
	}
	

}
