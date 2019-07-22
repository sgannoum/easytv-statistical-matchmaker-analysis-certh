package com.certh.iti.easytv.stmm.similarity.dimension;

public class MultiNumeric extends Dimension {

	private Numeric[] dimensions;
	private int dimensionLength;

	public MultiNumeric(Numeric[] dimensions, int i) {
		this.dimensions = dimensions;
		this.dimensionLength = i;
	}
	
	@Override
	public double[] dissimilarity(double a, double b) {	
		
		double[] dissimilarites = new double[dimensions.length * 2];
		long la = (long) a, lb = (long) b;
		long mask = (long) Math.pow(2, dimensionLength);
		
		for(int i = 0; i < dimensions.length; i++) {
			//get dimension value
			long tmpa = (la >> (dimensionLength * i)) & mask;
			long tmpb = (lb >> (dimensionLength * i)) & mask;
			
			//calculate dissimilarity
			double[] res = dimensions[i].dissimilarity(tmpa, tmpb);
			
			//add to results
			dissimilarites[i*2] = res[0];
			dissimilarites[(i*2) + 1] = res[1];
		}

		return dissimilarites;
	}

}
