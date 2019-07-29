package com.certh.iti.easytv.stmm.similarity.dimension;

public class Nominal extends Dimension{

	public Nominal(double missingValue) {
		super(missingValue);
	}
	
	public Nominal() {
		super();
	}


	@Override
	public double[] dissimilarity(double a, double b) {	
		if(a != b) {
			return new double[] {1.0, 1.0};
		}
		else if(a != missingValue) {
			return new double[] {1.0, 0.0};
		}
		
		return new double[] {0.0, 0.0};
	}

}
