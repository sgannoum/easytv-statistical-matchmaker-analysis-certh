package com.certh.iti.easytv.stmm.similarity;

import java.io.IOException;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.certh.iti.easytv.stmm.similarity.DimensionsGenerator;
import com.certh.iti.easytv.stmm.similarity.MaskGenerator;
import com.certh.iti.easytv.stmm.similarity.MaskedSimilarityMeasure;
import com.certh.iti.easytv.stmm.similarity.SimilarityMeasure;
import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

public class MaskedSimilarityMeasureProfileTest {

	private DimensionsGenerator dimensionsGenerator;
	private DistanceMeasure allDistance;
	
	@BeforeTest
	public void beforeTest() throws IOException, UserProfileParsingException {

		//Get corresponding dimensions
		dimensionsGenerator = new DimensionsGenerator(Profile.getAggregator().getAttributes());		
		allDistance = new SimilarityMeasure(dimensionsGenerator.getLables(), dimensionsGenerator.getDimensions());
	}
	
	@Test
	public void test_profile_distance_one_Numeric_dimension_1() throws IOException, JSONException, UserProfileParsingException {
		Profile profile_1 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 0," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 5," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 1.2," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"normal\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"20\"" + 
				"    }}}}" + 
				"}"));

		Profile profile_2 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 0," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 2," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 0.8," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"slow\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"15\"" + 
				"    }}}}" + 
				"}"));
		
		long mask = MaskGenerator.getMask(dimensionsGenerator.getLables(), new String[] {"http://registry.easytv.eu/common/volume"});
		DistanceMeasure maskedDist = new MaskedSimilarityMeasure(mask, dimensionsGenerator.getLables(), dimensionsGenerator.getDimensions());
		
		Assert.assertTrue(maskedDist.compute(profile_1.getPoint(), profile_2.getPoint()) > allDistance.compute(profile_1.getPoint(), profile_2.getPoint()));
	}
	
	@Test
	public void test_profile_distance_one_double_dimension() throws IOException, JSONException, UserProfileParsingException {
		Profile profile_1 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 0," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 5," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 1.2," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"normal\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"20\"" + 
				"    }}}}" + 
				"}"));

		Profile profile_2 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 0," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 2," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 1.2," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"normal\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"15\"" + 
				"    }}}}" + 
				"}"));
		
		long mask = MaskGenerator.getMask(dimensionsGenerator.getLables(), new String[] {"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor"});
		DistanceMeasure maskedDist = new MaskedSimilarityMeasure(mask, dimensionsGenerator.getLables(), dimensionsGenerator.getDimensions());
		
		Assert.assertTrue(maskedDist.compute(profile_1.getPoint(), profile_2.getPoint()) > allDistance.compute(profile_1.getPoint(), profile_2.getPoint()));
	}
	
	@Test
	public void test_profile_distance_one_nominal_dimension() throws IOException, JSONException, UserProfileParsingException {
		Profile profile_1 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 0," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 5," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 1.2," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"normal\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"20\"" + 
				"    }}}}" + 
				"}"));

		Profile profile_2 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 0," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 2," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 0.8," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"normal\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"15\"" + 
				"    }}}}" + 
				"}"));
		
		long mask = MaskGenerator.getMask(dimensionsGenerator.getLables(), new String[] {"http://registry.easytv.eu/application/hbbtv/screen/reader/speed"});
		DistanceMeasure maskedDist = new MaskedSimilarityMeasure(mask, dimensionsGenerator.getLables(), dimensionsGenerator.getDimensions());
		
		Assert.assertTrue(maskedDist.compute(profile_1.getPoint(), profile_2.getPoint()) > allDistance.compute(profile_1.getPoint(), profile_2.getPoint()));
	}
	
	@Test
	public void test_profile_distance_one_ordinal_dimension() throws IOException, JSONException, UserProfileParsingException {
		Profile profile_1 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 0," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 5," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 1.2," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"normal\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"20\"" + 
				"    }}}}" + 
				"}"));

		Profile profile_2 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 0," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 2," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 0.8," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"normal\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"20\"" + 
				"    }}}}" + 
				"}"));
		
		long mask = MaskGenerator.getMask(dimensionsGenerator.getLables(), new String[] {"http://registry.easytv.eu/application/cs/ui/text/size"});
		DistanceMeasure maskedDist = new MaskedSimilarityMeasure(mask, dimensionsGenerator.getLables(), dimensionsGenerator.getDimensions());
		
		Assert.assertTrue(maskedDist.compute(profile_1.getPoint(), profile_2.getPoint()) > allDistance.compute(profile_1.getPoint(), profile_2.getPoint()));
	}
	

}
