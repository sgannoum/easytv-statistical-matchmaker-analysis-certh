package com.certh.iti.easytv.stmm.similarity;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import com.certh.iti.easytv.stmm.similarity.dimension.Dimension;

public class MaskedSimilarityMeasure implements DistanceMeasure {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long mask;
	private Dimension[] dimensions;

	
	public MaskedSimilarityMeasure(long mask, Dimension[] dimensions) {
		this.dimensions = dimensions;
		this.mask = mask;
	}

	@Override
	public double compute(double[] a, double[] b) throws DimensionMismatchException {
		int index = 0;
		long tmpMask = mask;
		double dividend = 0, divisor = 0;

		do {
			if((tmpMask & 1L) == 1) {
				double[] delta_d = dimensions[index].dissimilarity(a[index], b[index]);
				
				dividend += delta_d[0] * delta_d[1];
				divisor += delta_d[0];
			}
			index++;
		} while((tmpMask /=2) != 0.0);
		
		//System.out.println(1.0 - (dividend/divisor));
		//return similarity
		return 1.0 - (dividend/divisor);
	}
}
