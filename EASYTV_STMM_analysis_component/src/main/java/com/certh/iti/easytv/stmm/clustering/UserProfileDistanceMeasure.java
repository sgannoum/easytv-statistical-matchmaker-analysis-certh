package com.certh.iti.easytv.stmm.clustering;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

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
	
	public UserProfileDistanceMeasure(String[] compareType) {
		this.compareType = toEnum(compareType);
		this.setMask(this.compareType);
	}
	
	public UserProfileDistanceMeasure(EnumSet<CompareType> compareType) {
		this.compareType = compareType;
		this.setMask(compareType);
	}
	
	public void setMask(long mask) {
		this.mask = mask;	
	}
	
	public void setMask(EnumSet<CompareType> compareType) {
		this.mask = 0;	
		
		if(compareType.contains(CompareType.GENERAL)) {
			this.mask |= MASK_BITS[0];
		}
		
		if(compareType.contains(CompareType.VISUAL)) {
			this.mask |= MASK_BITS[1];
		}
		
		if(compareType.contains(CompareType.AUDITORY)) {
			this.mask |= MASK_BITS[2];
		}
		
		if(compareType.contains(CompareType.DEFAULT_PREFERENCE)) {
			this.mask |= MASK_BITS[3];
		}
		
		if(compareType.contains(CompareType.ALL)) {
			this.mask |= MASK_BITS[4];
		}
	}

	public double compute(double[] a, double[] b) throws DimensionMismatchException {
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
	
	/**
	 * Convert a 
	 * @param compare
	 * @return
	 */
	public static EnumSet<CompareType> toEnum(String[] compare) {
		List<CompareType> compareType = new ArrayList<CompareType>();
		for(int i = 0; i < compare.length; i++) 
			compareType.add(UserProfileDistanceMeasure.strToEnum(compare[i]));
		return EnumSet.copyOf(compareType);
	}
	
	public static CompareType strToEnum(String type) {
		return CompareTypeEnum[indexOf(type)];
	}
	
	public static int indexOf(String type) {
		for(int i = 0; i < CompareTypeStr.length; i++) 
			if(CompareTypeStr[i].equalsIgnoreCase(type)) 
				return i;
			
		return -1;
	}

}
