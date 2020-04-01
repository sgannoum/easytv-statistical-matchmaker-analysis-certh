package com.certh.iti.easytv.stmm.similarity;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.certh.iti.easytv.config.Config;
import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

public class DissimilarityMeasureProfileTest {
	
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
	public void test_profiles_identical() throws IOException, UserProfileParsingException {
			
		double[] points1 = Arrays.copyOf(profile.getPoint(), profile.getPoint().length);
		double[] points2 = Arrays.copyOf(profile.getPoint(), profile.getPoint().length);

		//test equals
		Assert.assertEquals(dist.compute(points1, points2), 0.0);
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
		
		//check that the second profile is more dissimilar than the thrid one
		Assert.assertTrue(similarity_1_3 > similarity_1_2);
	}

}
