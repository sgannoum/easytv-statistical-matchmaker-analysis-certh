package com.certh.iti.easytv.stmm.similarity;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import com.certh.iti.easytv.stmm.similarity.dimension.Dimension;

public class SimilarityMeasure extends DissimilarityMeasure{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SimilarityMeasure(String[] uris, Dimension[] dimensions) {
		super(uris,dimensions);
	}
	
	/**
	 * @return the similarity distance in the range [0, 1] of points 
	 */
	@Override
	public double compute(double[] a, double[] b) throws DimensionMismatchException {

		//return dissimilarity
		return 1.0 - super.compute(a, b);
	}
}
