package com.certh.iti.easytv.stmm.clustering;

import java.io.IOException;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.certh.iti.easytv.stmm.similarity.DimensionsGenerator;
import com.certh.iti.easytv.stmm.similarity.SimilarityMeasure;
import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;
import junit.framework.Assert;

public class SimilarityMeasureTest {
	
	private DimensionsGenerator dimensionsGenerator;
	private DistanceMeasure distanceMeasure;
	
	
	@BeforeClass
	public void beforeTest() {
		dimensionsGenerator = new DimensionsGenerator(Profile.getUris(), Profile.getOperands());
		distanceMeasure = new SimilarityMeasure(dimensionsGenerator.getLables(), dimensionsGenerator.getDimensions());
	}
	
	@Test
	public void test_only_integer_dimension() throws IOException, UserProfileParsingException {
		
		Profile profile_1 = new Profile( new JSONObject("{" + 
														"    \"user_id\": 0," + 
														"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
														"		\"http://registry.easytv.eu/common/volume\": 5" + 
														"		" + 
														"    }}}}" + 
														"}"));
		
		Profile profile_2 = new Profile( new JSONObject("{" + 
														"    \"user_id\": 0," + 
														"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
														"		\"http://registry.easytv.eu/common/volume\": 3" + 
														"		" + 
														"    }}}}" + 
														"}"));
		
		Profile profile_3 = new Profile( new JSONObject("{" + 
														"    \"user_id\": 0," + 
														"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
														"		\"http://registry.easytv.eu/common/volume\": 5" + 
														"		" + 
														"    }}}}" + 
														"}"));
		
		
		Profile profile_4 = new Profile( new JSONObject("{" + 
														"    \"user_id\": 0," + 
														"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
														"		\"http://registry.easytv.eu/common/volume\": 20" + 
														"		" + 
														"    }}}}" + 
														"}"));
		
		Profile profile_5 = new Profile( new JSONObject("{" + 
														"    \"user_id\": 0," + 
														"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
														"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 1.2" + 
														"		" + 
														"    }}}}" + 
														"}"));
		
		
		double distance_12 = distanceMeasure.compute(profile_1.getPoint(), profile_2.getPoint());	
		double distance_13 = distanceMeasure.compute(profile_1.getPoint(), profile_3.getPoint());	
		double distance_14 = distanceMeasure.compute(profile_1.getPoint(), profile_4.getPoint());	
		double distance_15 = distanceMeasure.compute(profile_1.getPoint(), profile_5.getPoint());	

		//check that first and second profiles are more similar than first and fourth
		Assert.assertTrue(distance_12 > distance_14);
		
		//check that first and third profiles are similar 
		Assert.assertEquals(1.0, distance_13);
		
		Assert.assertTrue(distance_12 > distance_15);

		Assert.assertTrue(distance_14 > distance_15);
	}
	
	@Test
	public void test_only_double_dimension() throws IOException, UserProfileParsingException {
		
		Profile profile_1 = new Profile( new JSONObject("{" + 
						"    \"user_id\": 0," + 
						"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
						"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 1.2" + 
						"		" + 
						"    }}}}" + 
						"}"));

		Profile profile_2 = new Profile( new JSONObject("{" + 
						"    \"user_id\": 0," + 
						"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
						"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 0.8" + 
						"		" + 
						"    }}}}" + 
						"}"));
		
		Profile profile_3 = new Profile( new JSONObject("{" + 
						"    \"user_id\": 0," + 
						"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
						"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 1.2" + 
						"		" + 
						"    }}}}" + 
						"}"));
		
		
		Profile profile_4 = new Profile( new JSONObject("{" + 
						"    \"user_id\": 0," + 
						"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
						"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 2.5" + 
						"		" + 
						"    }}}}" + 
						"}"));
		
		Profile profile_5 = new Profile( new JSONObject("{" + 
						"    \"user_id\": 0," + 
						"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
						"		\"http://registry.easytv.eu/common/volume\": 5" + 
						"		" + 
						"    }}}}" + 
						"}"));
		

		double distance_12 = distanceMeasure.compute(profile_1.getPoint(), profile_2.getPoint());	
		double distance_13 = distanceMeasure.compute(profile_1.getPoint(), profile_3.getPoint());	
		double distance_14 = distanceMeasure.compute(profile_1.getPoint(), profile_4.getPoint());	
		double distance_15 = distanceMeasure.compute(profile_1.getPoint(), profile_5.getPoint());	

	
		//check that first and second profiles are more similar than first and fourth
		Assert.assertTrue(distance_12 > distance_14);
		
		//check that first and third profiles are similar 
		Assert.assertEquals(1.0, distance_13);
		
		Assert.assertTrue(distance_12 > distance_15);

		Assert.assertTrue(distance_14 > distance_15);
	}
	
	@Test
	public void test_only_nomial_dimension() throws IOException, UserProfileParsingException {
		
		Profile profile_1 = new Profile( new JSONObject("{" + 
						"    \"user_id\": 0," + 
						"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
						"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"normal\"" + 
						"		" + 
						"    }}}}" + 
						"}"));

		Profile profile_2 = new Profile( new JSONObject("{" + 
						"    \"user_id\": 0," + 
						"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
						"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"slow\"" + 
						"		" + 
						"    }}}}" + 
						"}"));
		
		Profile profile_3 = new Profile( new JSONObject("{" + 
						"    \"user_id\": 0," + 
						"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
						"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"normal\"" + 
						"		" + 
						"    }}}}" + 
						"}"));
		
		
		Profile profile_4 = new Profile( new JSONObject("{" + 
						"    \"user_id\": 0," + 
						"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
						"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"fast\"" + 
						"		" + 
						"    }}}}" + 
						"}"));
		
		Profile profile_5 = new Profile( new JSONObject("{" + 
						"    \"user_id\": 0," + 
						"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
						"		\"http://registry.easytv.eu/common/volume\": 5" + 
						"		" + 
						"    }}}}" + 
						"}"));

		double distance_12 = distanceMeasure.compute(profile_1.getPoint(), profile_2.getPoint());	
		double distance_13 = distanceMeasure.compute(profile_1.getPoint(), profile_3.getPoint());	
		double distance_14 = distanceMeasure.compute(profile_1.getPoint(), profile_4.getPoint());	
		double distance_15 = distanceMeasure.compute(profile_1.getPoint(), profile_5.getPoint());	

	
		//check that these two dimensions are the same
		Assert.assertTrue(distance_12 == distance_14);
		
		//check that first and third profiles are similar 
		Assert.assertEquals(1.0, distance_13);
		
		Assert.assertTrue(distance_12 < distance_15);

		Assert.assertTrue(distance_14 < distance_15);
	}
	
	@Test
	public void test_only_ordinal_dimension() throws IOException, UserProfileParsingException {
		
		Profile profile_1 = new Profile( new JSONObject("{" + 
						"    \"user_id\": 0," + 
						"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
						"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"20\"" + 
						"		" + 
						"    }}}}" + 
						"}"));

		Profile profile_2 = new Profile( new JSONObject("{" + 
						"    \"user_id\": 0," + 
						"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
						"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"15\"" + 
						"		" + 
						"    }}}}" + 
						"}"));
		
		Profile profile_3 = new Profile( new JSONObject("{" + 
						"    \"user_id\": 0," + 
						"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
						"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"20\"" + 
						"		" + 
						"    }}}}" + 
						"}"));
		
		
		Profile profile_4 = new Profile( new JSONObject("{" + 
						"    \"user_id\": 0," + 
						"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
						"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"23\"" + 
						"		" + 
						"    }}}}" + 
						"}"));
		
		Profile profile_5 = new Profile( new JSONObject("{" + 
						"    \"user_id\": 0," + 
						"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
						"		\"http://registry.easytv.eu/common/volume\": 5" + 
						"		" + 
						"    }}}}" + 
						"}"));

		double distance_12 = distanceMeasure.compute(profile_1.getPoint(), profile_2.getPoint());	
		double distance_13 = distanceMeasure.compute(profile_1.getPoint(), profile_3.getPoint());	
		double distance_14 = distanceMeasure.compute(profile_1.getPoint(), profile_4.getPoint());	
		double distance_15 = distanceMeasure.compute(profile_1.getPoint(), profile_5.getPoint());	

	
		//check that these two dimensions are the same
		Assert.assertTrue(distance_12 == distance_14);
		
		//check that first and third profiles are similar 
		Assert.assertEquals(1.0, distance_13);
		
		Assert.assertTrue(distance_12 > distance_15);

		Assert.assertTrue(distance_14 > distance_15);
	}
	
	@Test
	public void test_dimensions_1() throws IOException, UserProfileParsingException {
		
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
		
		Profile profile_3 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 0," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 5," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 1.2," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"normal\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"20\"" + 
				"    }}}}" + 
				"}"));
		
		
		Profile profile_4 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 0," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 10," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 2.2," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"fast\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"23\"" + 
				"    }}}}" + 
				"}"));
		

		double distance_12 = distanceMeasure.compute(profile_1.getPoint(), profile_2.getPoint());	
		double distance_13 = distanceMeasure.compute(profile_1.getPoint(), profile_3.getPoint());	
		double distance_14 = distanceMeasure.compute(profile_1.getPoint(), profile_4.getPoint());	

	
		//check that these two dimensions are the same
		Assert.assertTrue(distance_12 > distance_14);
		
		//check that first and third profiles are similar 
		Assert.assertEquals(1.0, distance_13);		
	}
	
}
