package com.certh.iti.easytv.stmm.generator.preference.operand;

import java.util.Random;

import com.certh.iti.easytv.stmm.user.profile.preference.condition.operand.ColorLiteral;


public class RandomColorLiteral extends ColorLiteral {

	public RandomColorLiteral(Random rand) {
		super("#"+Integer.toHexString(rand.nextInt(255))+Integer.toHexString(rand.nextInt(255))+Integer.toHexString(rand.nextInt(255)));
	}

}
