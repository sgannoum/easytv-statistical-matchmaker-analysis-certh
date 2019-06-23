package com.certh.iti.easytv.stmm.similarity;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.certh.iti.easytv.stmm.similarity.SimilarityMeasure;
import com.certh.iti.easytv.stmm.similarity.dimension.Dimension;
import com.certh.iti.easytv.stmm.similarity.dimension.Nominal;
import com.certh.iti.easytv.stmm.similarity.dimension.Numeric;
import com.certh.iti.easytv.stmm.similarity.dimension.Ordinal;

public class SimilarityMeasureTest {
	
	
	@Test
	public void test_numeric_attribute() {
		
		Dimension[] dimensions = new Dimension[1];
		dimensions[0] = new Numeric(64.0, 22.0);
		
		double[][] data = { {45},
							{22},
							{64},
							{28}};

		SimilarityMeasure useProfileSimilarityMeasure = new SimilarityMeasure(dimensions);
		
		double[][] expectedValues = {{0, 0, 0, 0},
									{0.45238095238095233,  0, 0, 0},
									{0.5476190476190477, 0.0,  0, 0} ,
									{0.5952380952380952, 0.8571428571428572, 0.1428571428571429, 0}
									};
		
		double[][] actualValues = new double[4][4];
		for(int i = 1; i < actualValues.length; i++) {
			//System.out.println();
			for(int j = 0; j < i; j++) {
				actualValues[i][j] = useProfileSimilarityMeasure.compute(data[i], data[j]);
				//System.out.print(actualValues[i][j] + ", ");
				Assert.assertEquals(actualValues[i][j], expectedValues[i][j]);
			}
		}
		
		//System.out.println();
		
	}
	
	@Test
	public void test_ordinal_attribute() {
		
		Dimension[] dimensions = new Dimension[1];
		dimensions[0] = new Ordinal(2, 0);
		
		double[][] data = { {2},
							{0},
							{1},
							{2}};

		SimilarityMeasure useProfileSimilarityMeasure = new SimilarityMeasure(dimensions);
		
		double[][] expectedValues = {{0, 0, 0, 0},
									{0.0,  0, 0, 0},
									{0.5, 0.5,  0, 0} ,
									{1.0, 0.0, 0.5, 0}
									};
		
		double[][] actualValues = new double[4][4];
		for(int i = 1; i < actualValues.length; i++) {
			//System.out.println();
			for(int j = 0; j < i; j++) {
				actualValues[i][j] = useProfileSimilarityMeasure.compute(data[i], data[j]);
				//System.out.print(actualValues[i][j] + ", ");
				Assert.assertEquals(actualValues[i][j], expectedValues[i][j]);
			}
		}
		
		//System.out.println();
		
	}
	
	
	@Test
	public void test_mixed_attributes() {
		
		Dimension[] dimensions = new Dimension[3];
		dimensions[0] = new Nominal();
		dimensions[1] = new Ordinal(2, 0);
		dimensions[2] = new Numeric(64.0, 22.0);
		
		double[][] data = { {0, 2, 45},
							{1, 0, 22},
							{2, 1, 64},
							{0, 2, 28}};
		
		SimilarityMeasure useProfileSimilarityMeasure = new SimilarityMeasure(dimensions);
		
		double[][] expectedValues = {{0, 0, 0, 0},
									{0.1507936507936508,  0, 0, 0},
									{0.3492063492063492, 0.16666666666666663,  0, 0} ,
									{0.8650793650793651, 0.2857142857142857, 0.2142857142857143, 0}
									};
		
		double[][] actualValues = new double[4][4];
		for(int i = 1; i < actualValues.length; i++) {
			//System.out.println();
			for(int j = 0; j < i; j++) {
				actualValues[i][j] = useProfileSimilarityMeasure.compute(data[i], data[j]);
				//System.out.print(actualValues[i][j] + ", ");
				Assert.assertEquals(actualValues[i][j], expectedValues[i][j]);
			}
		}
		
	}

}
