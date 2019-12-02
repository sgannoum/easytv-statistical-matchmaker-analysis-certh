package com.certh.iti.easytv.stmm.similarity.dimension;

public class Time extends Dimension{

	public Time() {
		super(-1.0);
	}
	
	public Time(double missingValue) {
		super(missingValue);
	}
	
	public double[] dissimilarity(double a, double b) {
		if(a == missingValue && a == b) 
			return new double[] {0.0, 0.0};
		
		return new double[] {1.0, Math.abs(a - b)};
	}

}
