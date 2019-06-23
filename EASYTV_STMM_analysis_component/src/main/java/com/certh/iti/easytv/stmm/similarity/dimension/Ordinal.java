package com.certh.iti.easytv.stmm.similarity.dimension;

public class Ordinal extends Numeric{

	public Ordinal(double max, double min) {
		super(max, min);
	}
	
	public Ordinal(double missingValue, double max, double min) {
		super(missingValue, max, min);
	}

}
