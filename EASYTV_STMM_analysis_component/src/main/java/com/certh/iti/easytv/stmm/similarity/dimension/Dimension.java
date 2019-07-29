package com.certh.iti.easytv.stmm.similarity.dimension;

public abstract class Dimension {
	
	protected double missingValue;
	
	public Dimension() {
		this.missingValue = -1.0;
	}
	
	public Dimension(double missingValue) {
		this.missingValue = missingValue;
	}
	
	public double getMissingValue() {
		return missingValue;
	}
	
	public double[] dissimilarity(double a, double b) {
		if(a == missingValue && a == b) {
			return new double[] {0.0, 0.0};
		}
		return new double[] {1.0, 0.0};
	}

}
