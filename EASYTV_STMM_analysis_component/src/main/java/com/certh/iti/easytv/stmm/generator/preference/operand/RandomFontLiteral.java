package com.certh.iti.easytv.stmm.generator.preference.operand;

import java.util.Random;

import com.certh.iti.easytv.stmm.user.profile.preference.condition.operand.FontLiteral;

public class RandomFontLiteral extends FontLiteral {

	public RandomFontLiteral(Random rand) {
		super(FontLiteral.fontOf(rand.nextInt(5)));
	}

}
