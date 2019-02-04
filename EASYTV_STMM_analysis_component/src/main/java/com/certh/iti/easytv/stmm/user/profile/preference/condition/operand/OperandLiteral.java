package com.certh.iti.easytv.stmm.user.profile.preference.condition.operand;

import org.json.JSONObject;


/**
 * Each operand shall be either a URI (in accordance with IETF RFC 3986) – in which case it shall be interpreted as the value of the concept with the URI as key, a condition object (i.e. a nested condition), or a literal.
 * @author salgan
 *
 */
public abstract class OperandLiteral {
	
	protected Object literal;
	
	public OperandLiteral(Object literal) {
		this.literal = literal;
	}
		
	public Object getValue() {
		return this.literal;
	}
	
	public abstract JSONObject toJSON();
	public abstract double distanceTo(OperandLiteral op2);
}