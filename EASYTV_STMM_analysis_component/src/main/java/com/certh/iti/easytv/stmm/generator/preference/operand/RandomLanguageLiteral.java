package com.certh.iti.easytv.stmm.generator.preference.operand;

import java.util.Random;

import com.certh.iti.easytv.stmm.user.profile.preference.condition.operand.LanguageLiteral;

public class RandomLanguageLiteral extends LanguageLiteral {

	public RandomLanguageLiteral(Random rand) {
		super(LanguageLiteral.languageOf(rand.nextInt(5)));
	}

}
