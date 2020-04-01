package com.certh.iti.easytv.stmm.similarity;

import org.apache.commons.math3.ml.distance.DistanceMeasure;

import com.certh.iti.easytv.user.Profile;

/**
 * A generator of distance measurement instances
 * 
 * @author salgan
 *
 */
public class DistanceMeasureFactory {
	
	private static DimensionsGenerator dimensionsGenerator = new DimensionsGenerator(Profile.getUris(), Profile.getOperands());
	
	/**
	 * Get an instance for all dimensions
	 * 
	 * @return
	 */
	public static DistanceMeasure getInstance() {
		return new DissimilarityMeasure(dimensionsGenerator.getLables(), dimensionsGenerator.getDimensions());
	}
	
	/**
	 * Get an instance for all dimensions
	 * 
	 * @return
	 */
	public static DistanceMeasure getInstance(long mask) {
		return new MaskedSimilarityMeasure(mask, dimensionsGenerator.getLables(), dimensionsGenerator.getDimensions());
	}
	
	
	/**
	 * Create a distance measure instance given the set of the available dimensions 
	 * 
	 * @param compareMode
	 * @return
	 */
	public static DistanceMeasure getInstance(String[] compareMode) {
		
		//All dimensions
		for(String mode : compareMode)
			if(mode.equalsIgnoreCase("all")) 
				return new DissimilarityMeasure(dimensionsGenerator.getLables(), dimensionsGenerator.getDimensions());
		
		//long that corresponds to dimensions
		long mask = MaskGenerator.getMask(Profile.getUris(), compareMode);
		
		//Subset of dimensions
		return new MaskedSimilarityMeasure(mask, dimensionsGenerator.getLables(), dimensionsGenerator.getDimensions());
	}

}
