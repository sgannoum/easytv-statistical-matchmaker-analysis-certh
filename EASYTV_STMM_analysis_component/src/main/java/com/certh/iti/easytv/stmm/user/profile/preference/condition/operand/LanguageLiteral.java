package com.certh.iti.easytv.stmm.user.profile.preference.condition.operand;

import org.json.JSONObject;

public class LanguageLiteral extends OperandLiteral{
	
	private int language; 
	private static final String[] languagesStr = {"ENGLISH", "SPANISH", "CATALAN", "GREEK", "ITALIAN"};

	public LanguageLiteral(Object literal) {
		super(literal);
		language = indexOf((String) literal);
		
		if(language == -1)
			throw new IllegalStateException("Unknown language " + literal);
	}

	@Override
	public JSONObject toJSON() {
		return null;
	}
	
	@Override
	public String toString() {
		return languagesStr[language];
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!LanguageLiteral.class.isInstance(obj)) return false;
		LanguageLiteral other = (LanguageLiteral) obj;
		
		return language == other.language;
	}

	@Override
	public double distanceTo(OperandLiteral op2) {
		LanguageLiteral other = (LanguageLiteral) op2;
		return Math.pow(language - other.language, 2);
	}
	
	
	private int indexOf(String language) {
		for(int i = 0; i < languagesStr.length; i++) 
			if(language.equalsIgnoreCase(languagesStr[i])) 
				return i;
			
		return -1;
	}
	
	public double[] getPoint() {
		return new double[] {language};
	}
}
