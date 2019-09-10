package com.certh.iti.easytv.stmm.similarity.dimension;

public class MultiNumeric extends Dimension {

	private Numeric[] subDimensionsHandler;
	private int dimensionBitLength;

	public MultiNumeric(Numeric[] subDimensionsHandler, int dimensionBitLength) {
		this.subDimensionsHandler = subDimensionsHandler;
		this.dimensionBitLength = dimensionBitLength;
	}
	
	@Override
	public double[] dissimilarity(double a, double b) {	
		
		//handle missing value situation
		if(a == missingValue && a == b) 
			return new double[] {0.0, 0.0};
		
		long la = (long) a, lb = (long) b;
		
		//calculate mask
		long mask = (long) Math.pow(2, dimensionBitLength) - 1;
		
		//set up dissimilarities result table
		double[] dissimilarites = new double[subDimensionsHandler.length * 2];
		
		for(int i = 0; i < subDimensionsHandler.length; i++) {
			//get dimension value
			long tmpa = la & mask;
			long tmpb = lb & mask;

			
			//shift bits
			la >>= dimensionBitLength;
			lb >>= dimensionBitLength;
			
			//calculate dissimilarity
			double[] res = subDimensionsHandler[i].dissimilarity(tmpa, tmpb);
			
			//add to results
			dissimilarites[i*2] = res[0];
			dissimilarites[(i*2) + 1] = res[1];
		}

		return dissimilarites;
	}

}
