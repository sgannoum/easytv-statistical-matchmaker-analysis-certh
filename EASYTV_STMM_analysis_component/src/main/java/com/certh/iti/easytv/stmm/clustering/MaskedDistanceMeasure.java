package com.certh.iti.easytv.stmm.clustering;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.ml.distance.CanberraDistance;
import org.apache.commons.math3.ml.distance.ChebyshevDistance;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EarthMoversDistance;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.ml.distance.ManhattanDistance;

import com.certh.iti.easytv.stmm.similarity.MaskGenerator;
import com.certh.iti.easytv.stmm.similarity.dimension.Dimension;

public class MaskedDistanceMeasure implements DistanceMeasure {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7065276430774353372L;
	private long mask;
	private int bits;
	private Dimension[] dimensions;
	private DistanceMeasure dist;

	
	public MaskedDistanceMeasure(long mask, Dimension[] dimensions, String distanceMeasure) {
		this.dimensions = dimensions;
		this.mask = mask;
		this.bits = MaskGenerator.getBits(mask);
		
		if(distanceMeasure.equalsIgnoreCase("CANBERRA"))
			dist = new CanberraDistance();
		else if(distanceMeasure.equalsIgnoreCase("CHEBYSHEV"))
			dist = new ChebyshevDistance();
		else if(distanceMeasure.equalsIgnoreCase("EARTHMOVERS"))
			dist = new EarthMoversDistance();
		else if(distanceMeasure.equalsIgnoreCase("EUCLIDEAN"))
			dist = new EuclideanDistance();
		else if(distanceMeasure.equalsIgnoreCase("MANHATTAN"))
			dist = new ManhattanDistance();
		else
			throw new IllegalArgumentException("Unknow distance measure \""+distanceMeasure+"\"");
		
	}


	@Override
	public double compute(double[] a, double[] b) throws DimensionMismatchException {
		int index = 0, cindex = 0;
		long tmpMask = this.mask;
		double ca[] = new double[bits];
		double cb[] = new double[bits];
		
		do {
			if((tmpMask & 1L) == 1) {
				ca[cindex] = a[index];
				cb[cindex] = b[index];
				cindex++;
			}
			index++;
		} while((tmpMask /=2) != 0.0);

		return dist.compute(ca, cb);
	}

}
