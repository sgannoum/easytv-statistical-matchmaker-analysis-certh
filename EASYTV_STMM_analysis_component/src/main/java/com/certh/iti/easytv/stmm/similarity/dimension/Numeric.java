package com.certh.iti.easytv.stmm.similarity.dimension;

public class Numeric extends Dimension {
	
	private double max;
	private double min;
	
	public Numeric(double max, double min) {
		super();
		this.max = max;
		this.min = min;
	}
	
	public Numeric(double missingValue, double max, double min) {
		super(missingValue);
		this.max = max;
		this.min = min;
	}

	@Override
	public double[] dissimilarity(double a, double b) {	
		if(a == missingValue && a == b) 
			return new double[] {0.0, 0.0};
		

		return new double[] {1.0, Math.abs(a - b)/ (max - min)};
	}


}
