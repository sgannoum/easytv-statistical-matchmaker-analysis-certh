package com.certh.iti.easytv.stmm.user.profile.preference.condition.operand;

import org.json.JSONObject;


public class NumericLiteral  extends OperandLiteral {
	private double numericLiteral;
	
	public NumericLiteral(Object literal) {
		super(literal);
		
		if(Double.class.isInstance(literal)) {
			numericLiteral = Double.class.cast(literal);
		} else {
			numericLiteral = Integer.class.cast(literal);
		}
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
		if(!NumericLiteral.class.isInstance(obj)) return false;
		NumericLiteral other = (NumericLiteral) obj;
		
		return numericLiteral == other.numericLiteral;
	}

	@Override
	public double distanceTo(OperandLiteral op2) {
		NumericLiteral other = (NumericLiteral) op2;
		return Math.pow(numericLiteral - other.numericLiteral, 2);
	}

}