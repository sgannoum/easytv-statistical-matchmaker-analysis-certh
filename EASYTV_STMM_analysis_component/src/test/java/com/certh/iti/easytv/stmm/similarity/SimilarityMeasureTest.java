package com.certh.iti.easytv.stmm.similarity;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.certh.iti.easytv.stmm.similarity.dimension.Dimension;
import com.certh.iti.easytv.stmm.similarity.dimension.MultiNominal;
import com.certh.iti.easytv.stmm.similarity.dimension.MultiNumeric;
import com.certh.iti.easytv.stmm.similarity.dimension.Nominal;
import com.certh.iti.easytv.stmm.similarity.dimension.Numeric;
import com.certh.iti.easytv.stmm.similarity.dimension.Ordinal;
import com.certh.iti.easytv.stmm.similarity.dimension.SymmetricBinary;

public class SimilarityMeasureTest {
	
	@Test
	public void test_numeric_attribute() {
		
		Dimension[] dimensions = new Dimension[1];
		dimensions[0] = new Numeric(-1.0, 20.0, 0.0);
		String[] lable = new String[] {"Numeric"};
		
		double[][] data = { {5.0},
							{2.0},
							{5.0},
							{8.0},
							{9.0},
							{20.0},
							{-1.0}};

		SimilarityMeasure similarityDist = new SimilarityMeasure(lable, dimensions);
		
		double similarity_0_1 = similarityDist.compute(data[0],  data[1]);
		double similarity_0_2 = similarityDist.compute(data[0],  data[2]);
		double similarity_0_3 = similarityDist.compute(data[0],  data[3]);
		double similarity_0_4 = similarityDist.compute(data[0],  data[4]);
		double similarity_0_5 = similarityDist.compute(data[0],  data[5]);
		double similarity_0_6 = similarityDist.compute(data[0],  data[6]); //with missing value
		double similarity_6_0 = similarityDist.compute(data[6],  data[0]); //with missing value
		double similarity_6_6 = similarityDist.compute(data[6],  data[6]); //with missing value


		//check that 5 is identical to 5
		Assert.assertEquals(similarity_0_2, 1.0);
		
		//check that 2 is same similar to 5 with 8
		Assert.assertTrue(similarity_0_1 == similarity_0_3);
		
		//check that 2 is more similar to 5 than 9
		Assert.assertTrue(similarity_0_1 > similarity_0_4);

		//check that 9 is more similar to 5 than 20
		Assert.assertTrue(similarity_0_4 > similarity_0_5);
		
		
		//two missing values are identical
		Assert.assertEquals(similarity_6_6, 1.0);
		
		//distance with missing value is symmetric
		Assert.assertTrue(similarity_0_6 == similarity_6_0);
		
		//check that 2 is more similar than missing value
		Assert.assertTrue(similarity_0_1 > similarity_0_6);
		
		//check that 9 is more similar than missing value
		Assert.assertTrue(similarity_0_4 > similarity_0_6);
		
		//check that missing value is more similar than 20
		Assert.assertTrue(similarity_0_6 > similarity_0_5);
	}
	
	@Test
	public void test_nominal_attribute() {
		
		Dimension[] dimensions = new Dimension[1];
		dimensions[0] = new Nominal();
		String[] lable = new String[] {"Nominal"};

		double[][] data = { {1},
							{0},
							{1},
							{2},
							{3},
							{4},
							{-1.0}};
		
		SimilarityMeasure similarityDist = new SimilarityMeasure(lable, dimensions);
		
		double similarity_0_1 = similarityDist.compute(data[0],  data[1]);
		double similarity_0_2 = similarityDist.compute(data[0],  data[2]);
		double similarity_0_3 = similarityDist.compute(data[0],  data[3]);
		double similarity_0_4 = similarityDist.compute(data[0],  data[4]);
		double similarity_0_5 = similarityDist.compute(data[0],  data[5]);
		double similarity_0_6 = similarityDist.compute(data[0],  data[6]); //with missing value
		double similarity_6_0 = similarityDist.compute(data[6],  data[0]); //with missing value
		double similarity_6_6 = similarityDist.compute(data[6],  data[6]); //with missing value

		//check that 1 is identical to 1
		Assert.assertEquals(similarity_0_2, 1.0);
		
		//check that 0 is same similar to 1 with 2
		Assert.assertTrue(similarity_0_1 == similarity_0_3);
		
		//check that 0 is more similar to 1 than 3
		Assert.assertTrue(similarity_0_1 == similarity_0_4);

		//check that 3 is more similar to 1 than 4
		Assert.assertTrue(similarity_0_4 == similarity_0_5);
		
		
		//two missing values are identical
		Assert.assertEquals(similarity_6_6, 1.0);
		
		//distance with missing value is symmetric
		Assert.assertTrue(similarity_0_6 == similarity_6_0);
		
		//check that 2 is more similar than missing value
		Assert.assertTrue(similarity_0_1 == similarity_0_6);
	}
	
	
	@Test
	public void test_ordinal_attribute() {
		
		Dimension[] dimensions = new Dimension[1];
		dimensions[0] = new Ordinal(5, 4, 0);
		String[] lable = new String[] {"Ordinal"};

		double[][] data = { {1},
							{0},
							{1},
							{2},
							{3},
							{4},
							{-1.0}};
		
		SimilarityMeasure similarityDist = new SimilarityMeasure(lable, dimensions);
		
		double similarity_0_1 = similarityDist.compute(data[0],  data[1]);
		double similarity_0_2 = similarityDist.compute(data[0],  data[2]);
		double similarity_0_3 = similarityDist.compute(data[0],  data[3]);
		double similarity_0_4 = similarityDist.compute(data[0],  data[4]);
		double similarity_0_5 = similarityDist.compute(data[0],  data[5]);
		double similarity_0_6 = similarityDist.compute(data[0],  data[6]); //with missing value
		double similarity_6_0 = similarityDist.compute(data[6],  data[0]); //with missing value
		double similarity_6_6 = similarityDist.compute(data[6],  data[6]); //with missing value

		//check that 1 is identical to 1
		Assert.assertEquals(similarity_0_2, 1.0);
		
		//check that 0 is same similar to 1 with 2
		Assert.assertTrue(similarity_0_1 == similarity_0_3);
		
		//check that 0 is more similar to 1 than 3
		Assert.assertTrue(similarity_0_1 > similarity_0_4);

		//check that 3 is more similar to 1 than 4
		Assert.assertTrue(similarity_0_4 > similarity_0_5);
		
		//two missing values are identical
		Assert.assertEquals(similarity_6_6, 1.0);
		
		//distance with missing value is symmetric
		Assert.assertTrue(similarity_0_6 == similarity_6_0);
		
		//check that 0 is more similar than missing value
		Assert.assertTrue(similarity_0_1 > similarity_0_6);
		
		//check that 3 is more similar than missing value
		Assert.assertTrue(similarity_0_4 == similarity_0_6);
		
		//check that missing value is more similar than 20
		Assert.assertTrue(similarity_0_6 > similarity_0_5);
	}
	
	@Test
	public void test_symmetricalBinary_attribute() {
		
		Dimension[] dimensions = new Dimension[1];
		dimensions[0] = new SymmetricBinary(-1.0);
		String[] lable = new String[] {"SymmetricBinary"};

		double[][] data = { {1},
							{0},
							{1},
							{-1.0}};
		
		SimilarityMeasure similarityDist = new SimilarityMeasure(lable, dimensions);
		
		double similarity_0_1 = similarityDist.compute(data[0],  data[1]);
		double similarity_0_2 = similarityDist.compute(data[0],  data[2]);
		double similarity_0_3 = similarityDist.compute(data[0],  data[3]);
		double similarity_4_4 = similarityDist.compute(data[3],  data[3]);

		//check that 1 is identical to 1
		Assert.assertEquals(similarity_0_2, 1.0);
		
		//check that 1 is not identical to 0
		Assert.assertTrue(similarity_0_1 == 0.0);
		
		//check that similarity with missing value is 0.0
		Assert.assertTrue(similarity_0_3 == 0.0);
				
		//check that similarity of missing values is 1.0
		Assert.assertTrue(similarity_4_4 == 1.0);
	}
	
	@Test
	public void test_multiNumeric_attribute() {
		
		Dimension[] dimensions = new Dimension[1];
		Numeric[] numericDimensions = {new Numeric(255, 0), new Numeric(255, 0), new Numeric(255, 0) };
		dimensions[0] = new MultiNumeric(numericDimensions, 8);
		String[] lable = new String[] {"MultiNumeric"};

		double[][] data = { {0xff0000}, //red
							{0xffa200}, //orange
							{0xff0000}, //red
							{0xfffb00}, //yellow
							{0x0004ff},	//blue
							{0xffffff},	//black
							{-1}		//missing value
							};
		
		SimilarityMeasure similarityDist = new SimilarityMeasure(lable, dimensions);
		
		double similarity_0_1 = similarityDist.compute(data[0],  data[1]);
		double similarity_0_2 = similarityDist.compute(data[0],  data[2]);
		double similarity_0_3 = similarityDist.compute(data[0],  data[3]);
		double similarity_0_4 = similarityDist.compute(data[0],  data[4]);
		double similarity_0_5 = similarityDist.compute(data[0],  data[5]);
		double similarity_0_6 = similarityDist.compute(data[0],  data[6]); //with missing value
		double similarity_6_0 = similarityDist.compute(data[6],  data[0]); //with missing value
		double similarity_6_6 = similarityDist.compute(data[6],  data[6]); //with missing value
		
		//check that 1 is identical to 1
		Assert.assertEquals(similarity_0_2, 1.0);
		
		//check that 0 is same similar to 1 with 2
		Assert.assertTrue(similarity_0_1 != similarity_0_2);
		
		//check that red is more similar to orange than yellow
		Assert.assertTrue(similarity_0_1 > similarity_0_3);
		
		//check that red is more similar to orange than blue
		Assert.assertTrue(similarity_0_1 > similarity_0_4);
		
		//check that black is more similar than blue
		Assert.assertTrue(similarity_0_5 > similarity_0_4);
		
		//two missing values are identical
		Assert.assertEquals(similarity_6_6, 1.0);
		
		//distance with missing value is symmetric
		Assert.assertTrue(similarity_0_6 == similarity_6_0);
		
		//check that orange is more similar than no color
		Assert.assertTrue(similarity_0_1 > similarity_0_6);
		
		//check that no color is more similar than blue
		Assert.assertTrue(similarity_0_6 > similarity_0_4);
		
		//check that black and no color are the same similar to red
		Assert.assertTrue(similarity_0_5 == similarity_0_6);
	}
	
	@Test
	public void test_multiNominal_attribute() {
		
		Dimension[] dimensions = new Dimension[1];
		dimensions[0] = new MultiNominal(8, 0.0);
		String[] lable = new String[] {"MultiNominal"};

		double[][] data = { {0b00000011}, 
							{0b00000001}, 
							{0b00000011}, 
							{0b11100011}, 
							{0b11100111},	//this number has higher similarity values than the following one
							{0b11111111},
							{0b00000000}
							};
		
		SimilarityMeasure similarityDist = new SimilarityMeasure(lable, dimensions);
		
		double similarity_0_1 = similarityDist.compute(data[0],  data[1]);
		double similarity_0_2 = similarityDist.compute(data[0],  data[2]);
		double similarity_0_3 = similarityDist.compute(data[0],  data[3]);
		double similarity_0_4 = similarityDist.compute(data[0],  data[4]);
		double similarity_0_5 = similarityDist.compute(data[0],  data[5]);
		double similarity_0_6 = similarityDist.compute(data[0],  data[6]); //with missing value
		double similarity_6_0 = similarityDist.compute(data[6],  data[0]); //with missing value
		double similarity_6_6 = similarityDist.compute(data[6],  data[6]); //with missing value

		//check that the element 0 is identical to element 2
		Assert.assertEquals(similarity_0_2, 1.0);
		
		//check the similarity with the element 1 differs from this of element 2
		Assert.assertTrue(similarity_0_1 != similarity_0_2);
		
		//check that the element 1 is more similar than element 3
		Assert.assertTrue(similarity_0_1 > similarity_0_3);
		
		//check that the element 1 is more similar than element 4
		Assert.assertTrue(similarity_0_1 > similarity_0_4);
		
		//check that the element 4 is more similar than element 5
		Assert.assertTrue(similarity_0_4 > similarity_0_5);
		
		
		//two missing values are identical
		Assert.assertEquals(similarity_6_6, 1.0);
		
		//distance with missing value is symmetric
		Assert.assertTrue(similarity_0_6 == similarity_6_0);
		
		//check that the element 1 is more similar than element 6
		Assert.assertTrue(similarity_0_1 > similarity_0_6);
		
		//check that the element 4 is more similar than element 6
		Assert.assertTrue(similarity_0_4 > similarity_0_6);
		
		//check that the element 5 is more similar than element 6
		Assert.assertTrue(similarity_0_5 > similarity_0_6);
	}
	
	@Test
	public void test_mixed_attributes() {
		
		Dimension[] dimensions = new Dimension[3];
		dimensions[0] = new Nominal();
		dimensions[1] = new Ordinal(5, 4, 0);
		dimensions[2] = new Numeric(20.0, 0.0);
		String[] lable = new String[] {"Nominal", "Ordinal", "Numeric"};
		
		double[][] data = { {1, 1, 5.0},
							{0, 0, 2.0},
							{1, 1, 5.0},
							{2, 2, 8.0},
							{3, 3, 9.0},
							{4, 4, 20.0}};
		
		SimilarityMeasure similarityDist = new SimilarityMeasure(lable, dimensions);
		
		double similarity_0_1 = similarityDist.compute(data[0],  data[1]);
		double similarity_0_2 = similarityDist.compute(data[0],  data[2]);
		double similarity_0_3 = similarityDist.compute(data[0],  data[3]);
		double similarity_0_4 = similarityDist.compute(data[0],  data[4]);
		double similarity_0_5 = similarityDist.compute(data[0],  data[5]);

		//check that the first element is identical to second
		Assert.assertEquals(similarity_0_2, 1.0);
		
		//check that the first element and third one have the same similarity distance
		Assert.assertTrue(similarity_0_1 == similarity_0_3);
		
		//check that the first element is more similar than the fourth one
		Assert.assertTrue(similarity_0_1 > similarity_0_4);

		//check that the fourth element is more similar than the fifth
		Assert.assertTrue(similarity_0_4 > similarity_0_5);
	}

}
