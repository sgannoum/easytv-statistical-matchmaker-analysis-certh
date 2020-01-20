package com.certh.iti.easytv.stmm.association.analysis.rules;

import org.json.JSONArray;
import org.json.JSONObject;

public class AssociationRuleWrapper implements Comparable<AssociationRuleWrapper> {
	
	public static abstract class RuleCondition {
		
		protected String uri;
		protected JSONObject json = null;

		public RuleCondition(String uri) {
			this.setUri(uri);
		}
		
		public String getUri() {
			return uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
		}
	}
	
	public static class HeadRuleCondition extends RuleCondition {
		
		protected Object[] range;
		
		public HeadRuleCondition(String uri, Object[] range) {
			super(uri);
			this.setRange(range);
		}
		
		public Object[] getRange() {
			return range;
		}

		public void setRange(Object[] range) {
			this.range = range;
		}
		
		public JSONObject getJSONObject() {
			if(json == null) {
				json = new JSONObject().put("preference", uri);
				if(range.length == 2) 
					json.put("in-range", new JSONArray().put(range[0]).put(range[1]));
				else if(range.length == 1)
					json.put("equals", new JSONArray().put(range[0]));
			}
			return json;
		}
		
		@Override
		public String toString() {
			return getJSONObject().toString(4);
		}
		
		@Override
		public boolean equals(Object obj) {
			if(this == obj) return true;
			if(!HeadRuleCondition.class.isInstance(obj)) return false;
			HeadRuleCondition other = (HeadRuleCondition) obj;
			return uri.equals(other.uri) && 
					range.length == other.range.length && 
					 range[0].equals(other.range[0]) && 
					 	range[1].equals(other.range[1]);
		}
	}
	
	public static class BodyRuleCondition extends RuleCondition {

		protected Object center;
		
		public BodyRuleCondition(String uri,  Object center) {
			super(uri);
			this.setCenter(center);
		}
		
		public Object getCenter() {
			return center;
		}

		public void setCenter(Object center) {
			this.center = center;
		}
		
		public JSONObject getJSONObject() {
			if(json == null) 
				json = new JSONObject().put("preference", uri).put("equals", center);

			return json;
		}
		
		@Override
		public String toString() {
			return getJSONObject().toString(4);
		}
		
		@Override
		public boolean equals(Object obj) {
			if(this == obj) return true;
			if(!BodyRuleCondition.class.isInstance(obj)) return false;
			BodyRuleCondition other = (BodyRuleCondition) obj;	
			return uri.equals(other.uri) && center.equals(other.center);
		}
	}
	
	
	private AssociationRule rule;
	private HeadRuleCondition[] head;
	private BodyRuleCondition[] body;
	private JSONObject json = null;
	
	public AssociationRuleWrapper(AssociationRule rule, HeadRuleCondition[] head, BodyRuleCondition[] body) {
		this.rule = rule;
		this.head = head;
		this.body = body;
	}
	
	public JSONObject getJSONObject() {
		if(json == null) {
			json = new JSONObject();
			JSONArray headConditions = new JSONArray();
			for(HeadRuleCondition headRuleCondition : head) 
				headConditions.put(headRuleCondition.getJSONObject());
			
			JSONArray bodyConditions = new JSONArray();
			for(BodyRuleCondition bodyRuleCondition : body) 
				bodyConditions.put(bodyRuleCondition.getJSONObject());
			
			json.put("head", headConditions);
			json.put("body", bodyConditions);
		}
		return json;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		
		if(!AssociationRuleWrapper.class.isInstance(obj)) return false;
		AssociationRuleWrapper other = (AssociationRuleWrapper) obj;	
		
		return getJSONObject().similar(other.getJSONObject());
	}
	
	@Override
	public String toString() {
		return rule.toString();
	}

	@Override
	public int compareTo(AssociationRuleWrapper o) {
		return rule.compareTo(o.rule);
	}
	
	public boolean canSubstituted(AssociationRuleWrapper o) {
		return rule.canSubstituted(o.rule);
	}

}
