package com.certh.iti.easytv.stmm.similarity;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.certh.iti.easytv.config.Config;
import com.certh.iti.easytv.stmm.similarity.dimension.Dimension;
import com.certh.iti.easytv.stmm.similarity.dimension.Nominal;
import com.certh.iti.easytv.stmm.similarity.dimension.Numeric;
import com.certh.iti.easytv.stmm.similarity.dimension.Ordinal;
import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

public class SimilarityMeasureTest {
	
	private JSONObject jsonProfile;
	private Profile profile;
	private DistanceMeasure dist;
	
	@BeforeTest
	public void beforeTest() throws IOException, UserProfileParsingException {
		jsonProfile = Config.getProfile("profile_with_context_1.json");
		profile = new Profile(jsonProfile);
		
		dist = DistanceMeasureFactory.getInstance();
	}
	
	@Test
	public void test_numeric_attribute() {
		
		Dimension[] dimensions = new Dimension[1];
		dimensions[0] = new Numeric(20.0, 0.0);
		String[] lable = new String[] {"Numeric"};
		
		double[][] data = { {5.0},
							{2.0},
							{5.0},
							{8.0},
							{9.0},
							{20.0}};

		SimilarityMeasure similarityDist = new SimilarityMeasure(lable, dimensions);
		
		double similarity_0_1 = similarityDist.compute(data[0],  data[1]);
		double similarity_0_2 = similarityDist.compute(data[0],  data[2]);
		double similarity_0_3 = similarityDist.compute(data[0],  data[3]);
		double similarity_0_4 = similarityDist.compute(data[0],  data[4]);
		double similarity_0_5 = similarityDist.compute(data[0],  data[5]);

		//check that 5 is identical to 5
		Assert.assertEquals(similarity_0_2, 1.0);
		
		//check that 2 is same similar to 5 with 8
		Assert.assertTrue(similarity_0_1 == similarity_0_3);
		
		//check that 2 is more similar to 5 than 9
		Assert.assertTrue(similarity_0_1 > similarity_0_4);

		//check that 9 is more similar to 5 than 20
		Assert.assertTrue(similarity_0_4 > similarity_0_5);
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
							{4}};
		
		SimilarityMeasure similarityDist = new SimilarityMeasure(lable, dimensions);
		
		double similarity_0_1 = similarityDist.compute(data[0],  data[1]);
		double similarity_0_2 = similarityDist.compute(data[0],  data[2]);
		double similarity_0_3 = similarityDist.compute(data[0],  data[3]);
		double similarity_0_4 = similarityDist.compute(data[0],  data[4]);
		double similarity_0_5 = similarityDist.compute(data[0],  data[5]);

		//check that 1 is identical to 1
		Assert.assertEquals(similarity_0_2, 1.0);
		
		//check that 0 is same similar to 1 with 2
		Assert.assertTrue(similarity_0_1 == similarity_0_3);
		
		//check that 0 is more similar to 1 than 3
		Assert.assertTrue(similarity_0_1 == similarity_0_4);

		//check that 3 is more similar to 1 than 4
		Assert.assertTrue(similarity_0_4 == similarity_0_5);
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
							{4}};
		
		SimilarityMeasure similarityDist = new SimilarityMeasure(lable, dimensions);
		
		double similarity_0_1 = similarityDist.compute(data[0],  data[1]);
		double similarity_0_2 = similarityDist.compute(data[0],  data[2]);
		double similarity_0_3 = similarityDist.compute(data[0],  data[3]);
		double similarity_0_4 = similarityDist.compute(data[0],  data[4]);
		double similarity_0_5 = similarityDist.compute(data[0],  data[5]);

		//check that 1 is identical to 1
		Assert.assertEquals(similarity_0_2, 1.0);
		
		//check that 0 is same similar to 1 with 2
		Assert.assertTrue(similarity_0_1 == similarity_0_3);
		
		//check that 0 is more similar to 1 than 3
		Assert.assertTrue(similarity_0_1 > similarity_0_4);

		//check that 3 is more similar to 1 than 4
		Assert.assertTrue(similarity_0_4 > similarity_0_5);
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

	
	@Test
	public void test_profiles_identical() throws IOException, UserProfileParsingException {
			
		double[] points1 = Arrays.copyOf(profile.getPoint(), profile.getPoint().length);
		double[] points2 = Arrays.copyOf(profile.getPoint(), profile.getPoint().length);

		//test equals
		Assert.assertEquals(dist.compute(points1, points2), 1.0);
	}
	
	@Test
	public void test_profiles_similarity_symmetrical() throws IOException, UserProfileParsingException {
			
		double[] points1 = Arrays.copyOf(profile.getPoint(), profile.getPoint().length);
		double[] points2 = Arrays.copyOf(profile.getPoint(), profile.getPoint().length);

		//test symmetric
		Assert.assertEquals(dist.compute(points1, points2), dist.compute(points2, points1));
	}
	
	@Test
	public void test_profiles_similarity() throws IOException, UserProfileParsingException {
			
		double[] points1 = Arrays.copyOf(profile.getPoint(), profile.getPoint().length);
		double[] points2 = Arrays.copyOf(profile.getPoint(), profile.getPoint().length);
		double[] points3 = Arrays.copyOf(profile.getPoint(), profile.getPoint().length);

		//do small changes
		points2[0] = 33;
		points3[0] = 32;
		
		double similarity_1_2 = dist.compute(points1, points2);
		double similarity_1_3 = dist.compute(points1, points3);
		
		//check that the second profile is more similar than the thrid one
		Assert.assertTrue(similarity_1_2 > similarity_1_3);
	}

}
