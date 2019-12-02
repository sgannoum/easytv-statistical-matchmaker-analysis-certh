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

	
	@Override
	public double[] dissimilarity(double a, double b) {	
		
		//handle missing value situation
		if(a == missingValue && a == b) 
			return new double[] {0.0, 0.0};
		
		long la = (long) a, lb = (long) b;
		long common = la | lb;
		int cindex = 0;
		
		//no commons
		if(common == 0)
			return new double[] {1.0, 1.0};
		
		for(int i =0; i < length && common != 0; common /=2, i++)
			if((common & 1L) == 1) 
				cindex++;
		

		return new double[] {0.0, 1 - (cindex/length)};
	}

}
