package com.certh.iti.easytv.stmm.clustering;

import java.io.IOException;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.certh.iti.easytv.stmm.similarity.DimensionsGenerator;
import com.certh.iti.easytv.stmm.similarity.DissimilarityMeasure;
import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

public class ClustereTest {
	
	private DimensionsGenerator dimensionsGenerator;
	private DistanceMeasure distanceMeasure;
	
	@BeforeTest
	public void beforeTest() {
		dimensionsGenerator = new DimensionsGenerator(Profile.getUris(), Profile.getOperands());
		distanceMeasure = new DissimilarityMeasure(dimensionsGenerator.getLables(), dimensionsGenerator.getDimensions());
	}

	@Test
	public void test_findCenter_1() throws IOException, UserProfileParsingException {
		
		Profile profile_1 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 1," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 5," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 1.2," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"normal\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"20\"" + 
				"    }}}}" + 
				"}"));
		
		Profile profile_11 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 11," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 3," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 2.2," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"normal\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"20\"" + 
				"    }}}}" + 
				"}"));
		
		Profile profile_12 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 12," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 7," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 1.0," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"normal\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"20\"" + 
				"    }}}}" + 
				"}"));

		
		Cluster<Profile> cluster_1 = new Cluster<Profile>();
		cluster_1.addPoint(profile_1); cluster_1.addPoint(profile_11); cluster_1.addPoint(profile_12);

		Profile centerProfile = new Profile();
        Clustere.FindCenter(distanceMeasure, cluster_1, centerProfile);
		
        //check that the cluster center is the profile that has the minimum distance from all profiles in the cluster
		Assert.assertTrue(profile_1.getJSONObject().similar(centerProfile.getJSONObject()));
	}
	
	@Test
	public void test_findCenter_2() throws IOException, UserProfileParsingException {
		
		Profile profile_1 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 1," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 5," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 12.0," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"normal\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"20\"" + 
				"    }}}}" + 
				"}"));
		
		Profile profile_11 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 11," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 3," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 2.2," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"normal\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"20\"" + 
				"    }}}}" + 
				"}"));
		
		Profile profile_12 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 12," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 7," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 1.0," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"normal\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"20\"" + 
				"    }}}}" + 
				"}"));

		
		Cluster<Profile> cluster_1 = new Cluster<Profile>();
		cluster_1.addPoint(profile_1); cluster_1.addPoint(profile_11); cluster_1.addPoint(profile_12);

		Profile centerProfile = new Profile();
        Clustere.FindCenter(distanceMeasure, cluster_1, centerProfile);
		
        //check that the cluster center is the profile that has the minimum distance from all profiles in the cluster
		Assert.assertTrue(profile_11.getJSONObject().similar(centerProfile.getJSONObject()));
	}
	
	
}
