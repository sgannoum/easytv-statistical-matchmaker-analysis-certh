package com.certh.iti.easytv.stmm.similarity.dimension;

public class MultiNominal extends Dimension {

	int length;
	
	public MultiNominal(int length, double missingValue) {
		super(missingValue);
		this.length = length;
	}
	
	public MultiNominal(int length) {
		super();
		this.length = length;
	}

	/**
	 * The similarity between two series of bits is calculated based on the common number 
	 * of zero and ones between the two series
	 */
	@Override
	public double[] dissimilarity(double a, double b) {	
		
		//handle missing value situation
		if(a == missingValue && a == b) 
			return new double[] {0.0, 0.0};
		
		long la = (long) a, lb = (long) b;
		long or = la | lb;
		long common = la & lb;
		int cindex = 0, dindex = 0;
		
		//no commons
		if(common == 0)
			return new double[] {1.0, 1.0};
		
		for(int i =0; i < length && common != 0; common /=2, i++) {
			if((common & 1L) == 1) 
				cindex++;
		}
		
		for(int i = 0; i < length && or != 0; or /=2, i++) {
			if((or & 1L) == 1) 
				dindex++;
		}

		double value = (double) cindex/ (double) dindex;
		return new double[] {1.0, 1 - value};
	}

}
