package com.certh.iti.easytv.stmm.similarity;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import com.certh.iti.easytv.stmm.similarity.dimension.Dimension;

public class SimilarityMeasure implements DistanceMeasure{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Dimension[] dimensions;
	private String[] uris;
	
	
	public SimilarityMeasure(String[] uris, Dimension[] dimensions) {
		this.dimensions = dimensions;
		this.uris = uris;
	}
	
	@Override
	public double compute(double[] a, double[] b) throws DimensionMismatchException {
		double dividend = 0, divisor = 0;
				
		for(int i = 0; i < dimensions.length; i++) {
			double[] delta_d = dimensions[i].dissimilarity(a[i], b[i]);
			
			//Add dissimilarities of subDimensions
			dividend += delta_d[0] * delta_d[1];
			divisor += delta_d[0];
		}
		
		//return similarity
		return 1.0 - (dividend/divisor);
	}
}
