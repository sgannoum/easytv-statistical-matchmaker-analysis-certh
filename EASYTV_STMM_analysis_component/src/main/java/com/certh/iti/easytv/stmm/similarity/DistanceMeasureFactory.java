package com.certh.iti.easytv.stmm.similarity;

import org.apache.commons.math3.ml.distance.DistanceMeasure;

import com.certh.iti.easytv.user.preference.Preference;

public class DistanceMeasureFactory {
	
	private static DimensionsGenerator dimensionsGenerator = new DimensionsGenerator(Preference.getOperands());
	
	public static DistanceMeasure getInstance(String[] compareMode) {
		
		//All dimensions
		for(String mode : compareMode)
			if(mode.equalsIgnoreCase("all")) {
				return  new SimilarityMeasure(dimensionsGenerator.getDimensions());
			}
		
		//Subset of dimensions
		return new MaskedSimilarityMeasure(MaskGenerator.getMask(Preference.getUris(), Preference.getOperands(), compareMode), dimensionsGenerator.getDimensions());
	}

}
