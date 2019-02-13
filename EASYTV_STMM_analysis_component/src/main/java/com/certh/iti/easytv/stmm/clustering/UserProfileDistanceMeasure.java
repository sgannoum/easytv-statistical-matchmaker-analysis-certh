package com.certh.iti.easytv.stmm.clustering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.ml.distance.CanberraDistance;
import org.apache.commons.math3.ml.distance.ChebyshevDistance;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EarthMoversDistance;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.ml.distance.ManhattanDistance;

public class UserProfileDistanceMeasure implements DistanceMeasure {

	private static final long serialVersionUID = -965077931847109377L;
	
	public enum CompareType {
		GENERAL,
		VISUAL,
		AUDITORY,
		DEFAULT_PREFERENCE,
		ALL
	};
	
	private static final CompareType[]  CompareTypeEnum = {CompareType.GENERAL, CompareType.VISUAL, CompareType.AUDITORY, CompareType.DEFAULT_PREFERENCE, CompareType.ALL};
	private static final String[]  CompareTypeStr = {"GENERAL", "VISUAL", "AUDITORY", "DEFAULT_PREFERENCE", "ALL"};

	
	private final long[] MASK_BITS = {3L, 28L, 2016L, 8589932544L, 8589934591L};
	private EnumSet<CompareType> compareType;
	private long mask;
	private int maskBits;
	private DistanceMeasure dist = new EuclideanDistance();
	
	public UserProfileDistanceMeasure(String[] compareType, String distanceMeasure) {
		this.compareType = toEnum(compareType);
		this.setMask(this.compareType);
		this.setDistanceMeasure(distanceMeasure);
	}
	
	public UserProfileDistanceMeasure(EnumSet<CompareType> compareType, String distanceMeasure) {
		this.compareType = compareType;
		this.setMask(compareType);
		this.setDistanceMeasure(distanceMeasure);
	}
	
	public UserProfileDistanceMeasure(EnumSet<CompareType> compareType) {
		this.compareType = compareType;
		this.setMask(compareType);
	}
	
	public void setDistanceMeasure(String distanceMeasure) {
		if(distanceMeasure != null && !distanceMeasure.equals(""))
			this.dist = toDistanceMeasure(distanceMeasure);
		
		if(this.dist == null)
			throw new IllegalArgumentException("Unknwon distance measure "+distanceMeasure);
	}
	
	public void setMask(long mask) {
		this.mask = mask;	
	}
	
	public void setMask(EnumSet<CompareType> compareType) {
		this.mask = 0;	
		this.maskBits = 0;
		
		if(compareType.contains(CompareType.GENERAL)) {
			this.mask |= MASK_BITS[0];
			this.maskBits += 2;
		}
		
		if(compareType.contains(CompareType.VISUAL)) {
			this.mask |= MASK_BITS[1];
			this.maskBits += 3;
		}
		
		if(compareType.contains(CompareType.AUDITORY)) {
			this.mask |= MASK_BITS[2];
			this.maskBits += 6;
		}
		
		if(compareType.contains(CompareType.DEFAULT_PREFERENCE)) {
			this.mask |= MASK_BITS[3];
			this.maskBits += 22;
		}
		
		if(compareType.contains(CompareType.ALL)) {
			this.mask |= MASK_BITS[4];
			this.maskBits = 33;
		}
	}

/*	public double compute(double[] a, double[] b) throws DimensionMismatchException {
		//TO-DO better similarity distance measure
		int index = 0;
		double distance = 0;
		long tmpMask = this.mask;
		
		do {
			if((tmpMask & 1L) == 1) 
				distance += Math.pow(a[index] - b[index], 2);
			index++;
		} while((tmpMask /=2) != 0.0);

		return Math.sqrt(distance);
	}
*/
	
	public double compute(double[] a, double[] b) throws DimensionMismatchException {
		int index = 0, cindex = 0;
		long tmpMask = this.mask;
		double ca[] = new double[this.maskBits];
		double cb[] = new double[this.maskBits];
		
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
	
	/**
	 * Convert a 
	 * @param compare
	 * @return
	 */
	public static EnumSet<CompareType> toEnum(String[] compare) {
		List<CompareType> compareType = new ArrayList<CompareType>();
		for(int i = 0; i < compare.length; i++) 
			compareType.add(CompareTypeEnum[indexOf(compare[i], CompareTypeStr)]);
		return EnumSet.copyOf(compareType);
	}
	
	public static DistanceMeasure toDistanceMeasure(String distanceFunction) {
		
		if(distanceFunction.equalsIgnoreCase("CANBERRA"))
			return new CanberraDistance();
		else if(distanceFunction.equalsIgnoreCase("CHEBYSHEV"))
			return new ChebyshevDistance();
		else if(distanceFunction.equalsIgnoreCase("EARTHMOVERS"))
			return new EarthMoversDistance();
		else if(distanceFunction.equalsIgnoreCase("EUCLIDEAN"))
			return new EuclideanDistance();
		else if(distanceFunction.equalsIgnoreCase("MANHATTAN"))
			return new ManhattanDistance();
	
		return null;
	}
	
	public static int indexOf(String type, String[] CompareTypeStr) {
		for(int i = 0; i < CompareTypeStr.length; i++) 
			if(CompareTypeStr[i].equalsIgnoreCase(type)) 
				return i;
			
		return -1;
	}

}
