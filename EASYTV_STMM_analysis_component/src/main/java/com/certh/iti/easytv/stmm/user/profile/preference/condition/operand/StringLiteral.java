package com.certh.iti.easytv.stmm.user.profile.preference.condition.operand;

import org.json.JSONObject;


public class StringLiteral extends OperandLiteral {
	private String str;

	public StringLiteral(Object literal) {
		super(literal);
		str = (String) literal;
	}
	
	public StringLiteral(String literal) {
		super(literal);
		str = (String) literal;
	}

	@Override
	public JSONObject toJSON() {
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!StringLiteral.class.isInstance(obj)) return false;
		
		return str.equals(((StringLiteral) obj).str);
	}
	
	@Override
	public String toString() {
		return str;
	}

	@Override
	public double distanceTo(OperandLiteral op2) {
		StringLiteral other = (StringLiteral) op2;
		return str.compareToIgnoreCase(other.str);
	}
}