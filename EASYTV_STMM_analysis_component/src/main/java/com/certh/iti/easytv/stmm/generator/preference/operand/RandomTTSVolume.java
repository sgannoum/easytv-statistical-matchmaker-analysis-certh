package com.certh.iti.easytv.stmm.generator.preference.operand;

import java.util.Random;

import com.certh.iti.easytv.stmm.user.profile.preference.condition.operand.NumericLiteral;

public class RandomTTSVolume extends NumericLiteral {

	public RandomTTSVolume(Random literal) {
		super(literal.nextInt(100));
	}

}
