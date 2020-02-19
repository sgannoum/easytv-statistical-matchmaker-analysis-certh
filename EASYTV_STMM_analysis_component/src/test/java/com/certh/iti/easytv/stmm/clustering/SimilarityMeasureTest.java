package com.certh.iti.easytv.stmm.clustering;

import java.io.IOException;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.json.JSONObject;
import org.testng.annotations.Test;

import com.certh.iti.easytv.stmm.similarity.DimensionsGenerator;
import com.certh.iti.easytv.stmm.similarity.SimilarityMeasure;
import com.certh.iti.easytv.user.UserProfile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;
import com.certh.iti.easytv.user.preference.Preference;

public class SimilarityMeasureTest {
	JSONObject jsonProfile1 = new JSONObject("{\"user_preferences\": {\"default\": {\"preferences\": {" + 
			"                        \"http://registry.easytv.eu/common/volume\": 27," + 
			"                        \"http://registry.easytv.eu/common/content/audio/language\": it," +
			"                        \"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 1.2," + 
			"                        \"http://registry.easytv.eu/application/cs/audio/eq/low/shelf/gain\": -50," + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/enhancement/image/type\": \"none\"," + 
			"                        \"http://registry.easytv.eu/application/cs/ui/text/size\": \"15\"" + 
			"}}}}");
	
	JSONObject jsonProfile2 = new JSONObject("{\"user_preferences\": {\"default\": {\"preferences\": {" + 
			"                        \"http://registry.easytv.eu/common/volume\": 30," + 
			"                        \"http://registry.easytv.eu/common/content/audio/language\": it," +
			"                        \"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 1.2," + 
			"                        \"http://registry.easytv.eu/application/cs/audio/eq/low/shelf/gain\": 50," + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/enhancement/image/type\": \"none\"," + 
			"                        \"http://registry.easytv.eu/application/cs/ui/text/size\": \"15\"" + 
			"}}}}");
	
	private DimensionsGenerator dimensionsGenerator = new DimensionsGenerator(Preference.getUris(), Preference.getOperands());

	@Test
	public void test_check_points() throws IOException, UserProfileParsingException {
		UserProfile profile1 = new UserProfile(jsonProfile1);
		
		for(double point : profile1.getPoint()) {
			System.out.print(point+", ");

		}
	
		
		System.out.println();

	}
	
	@Test
	public void test_profile_distance() throws IOException, UserProfileParsingException {
		UserProfile profile1 = new UserProfile(jsonProfile1);
		UserProfile profile2 = new UserProfile(jsonProfile2);

		DistanceMeasure dist = new SimilarityMeasure(dimensionsGenerator.getLables(), dimensionsGenerator.getDimensions());
		
		System.out.println(dist.compute(profile1.getPoint(), profile2.getPoint()));

	}
	
}
