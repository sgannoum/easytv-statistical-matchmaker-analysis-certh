package com.certh.iti.easytv.stmm.generator.preference.operand;

import java.util.Random;

import com.certh.iti.easytv.stmm.user.profile.preference.condition.operand.NumericLiteral;

public class RandomTTSSpeed extends NumericLiteral {

	public RandomTTSSpeed(Random literal) {
		super(literal.nextInt(20) - 10);
	}

}
