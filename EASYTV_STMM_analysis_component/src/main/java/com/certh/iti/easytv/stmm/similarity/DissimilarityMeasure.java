package com.certh.iti.easytv.stmm.similarity;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import com.certh.iti.easytv.stmm.similarity.dimension.Dimension;

public class DissimilarityMeasure implements DistanceMeasure{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Dimension[] dimensions;
	private String[] uris;
	
	
	public DissimilarityMeasure(String[] uris, Dimension[] dimensions) {
		this.dimensions = dimensions;
		this.uris = uris;
	}
	
	/**
	 * @return the dissimilarity distance in the range [0, 1] of points 
	 */
	@Override
	public double compute(double[] a, double[] b) throws DimensionMismatchException {
		double dividend = 0, divisor = 0;
				
		for(int i = 0; i < dimensions.length; i++) {
			double[] delta_d = dimensions[i].dissimilarity(a[i], b[i]);
			
			//Add dissimilarities of subDimensions
			for(int j = 0; j < delta_d.length; j += 2) {
				dividend += delta_d[j] * delta_d[j + 1];
				divisor += delta_d[j];
			}
		}
		
		
		if(divisor == 0.0)
			return 0.0;
		
		//return dissimilarity
		return (dividend/divisor);
	}
}
