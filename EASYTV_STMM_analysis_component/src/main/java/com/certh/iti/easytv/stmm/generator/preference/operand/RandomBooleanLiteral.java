package com.certh.iti.easytv.stmm.generator.preference.operand;

import java.util.Random;

import com.certh.iti.easytv.stmm.user.profile.preference.condition.operand.BooleanLiteral;

public class RandomBooleanLiteral extends BooleanLiteral {

	public RandomBooleanLiteral(Random rand) {
		super(rand.nextBoolean());
	}
}
