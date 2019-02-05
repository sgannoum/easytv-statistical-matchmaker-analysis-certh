package com.certh.iti.easytv.stmm.generator.preference.operand;

import java.util.Random;

import com.certh.iti.easytv.stmm.user.profile.preference.condition.operand.TimeLiteral;

public class RandomTimeLiteral extends TimeLiteral {

	public RandomTimeLiteral(Random rand) {
		super(String.format("%02d", rand.nextInt(24))+":"+Integer.toString(rand.nextInt(60)));
	}

}
