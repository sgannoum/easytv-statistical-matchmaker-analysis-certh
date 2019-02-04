package com.certh.iti.easytv.stmm.user.profile.preference.condition.operand;

import org.json.JSONObject;

public class BooleanLiteral extends OperandLiteral {
	private boolean booleanLiteral;
	
	public BooleanLiteral(Object literal) {
		super(literal);
		booleanLiteral = (Boolean) literal;
	}

	@Override
	public JSONObject toJSON() {
		return null;
	}
	
	@Override
	public String toString() {
		return String.valueOf(literal);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!BooleanLiteral.class.isInstance(obj)) return false;
		
		return literal.equals(((BooleanLiteral)obj).getValue());
	}

	@Override
	public double distanceTo(OperandLiteral op2) {
		BooleanLiteral other = (BooleanLiteral) op2;
		return booleanLiteral == other.booleanLiteral ? 0 : 1.0;
	}
}