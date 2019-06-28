package com.certh.iti.easytv.stmm.clustering;

import java.io.IOException;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.certh.iti.easytv.stmm.similarity.DimensionsGenerator;
import com.certh.iti.easytv.stmm.similarity.MaskGenerator;
import com.certh.iti.easytv.stmm.similarity.MaskedSimilarityMeasure;
import com.certh.iti.easytv.user.UserProfile;
import com.certh.iti.easytv.user.preference.Preference;

public class MaskedSimilarityMeasureTest {
	JSONObject jsonProfile1 = new JSONObject("{\"user_preferences\": {\"default\": {\"preferences\": {\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/cc/audioSubtitles\": false,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/audio/track\": \"CA\",\r\n" + 
			"    \"http://registry.easytv.eu/application/tts/speed\": 38,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/accessibility/textDetection\": false,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/cc/subtitles/backgroundColor\": \"#ee6243\",\r\n" + 
			"    \"http://registry.easytv.eu/common/subtitles\": \"EN\",\r\n" + 
			"    \"http://registry.easytv.eu/common/display/screen/enhancement/font/color\": \"#b23b41\",\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/accessibility/imageMagnification/scale\": 94,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/cc/subtitles/language\": \"EN\",\r\n" + 
			"    \"http://registry.easytv.eu/common/signLanguage\": \"EN\",\r\n" + 
			"    \"http://registry.easytv.eu/common/content/audio/language\": \"GR\",\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/cc/subtitles/fontColor\": \"#39dc2\",\r\n" + 
			"    \"http://registry.easytv.eu/common/display/screen/enhancement/font/type\": \"serif\",\r\n" + 
			"    \"http://registry.easytv.eu/common/display/screen/enhancement/background\": \"#18d4dc\",\r\n" + 
			"    \"http://registry.easytv.eu/application/tts/language\": \"ES\",\r\n" + 
			"    \"http://registry.easytv.eu/common/displayContrast\": 52,\r\n" + 
			"    \"http://registry.easytv.eu/application/tts/volume\": 24,\r\n" + 
			"    \"http://registry.easytv.eu/common/content/audio/volume\": 80,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/audio/volume\": 39,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/cc/subtitles/fontSize\": 58,\r\n" + 
			"    \"http://registry.easytv.eu/common/display/screen/enhancement/font/size\": 19,\r\n" + 
			"    \"http://registry.easytv.eu/application/tts/audioQuality\": 90,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/accessibility/faceDetection\": false,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/audio/audioDescription\": false\r\n" + 
			"}}}}");
	
	JSONObject jsonProfile2 = new JSONObject("{\"user_preferences\": {\"default\": {\"preferences\": {\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/cc/audioSubtitles\": false,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/audio/track\": \"CA\",\r\n" + 
			"    \"http://registry.easytv.eu/application/tts/speed\": 36,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/accessibility/textDetection\": false,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/cc/subtitles/backgroundColor\": \"#62f0ef\",\r\n" + 
			"    \"http://registry.easytv.eu/common/subtitles\": \"EN\",\r\n" + 
			"    \"http://registry.easytv.eu/common/display/screen/enhancement/font/color\": \"#49d234\",\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/accessibility/imageMagnification/scale\": 69,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/cc/subtitles/language\": \"IT\",\r\n" + 
			"    \"http://registry.easytv.eu/common/signLanguage\": \"GR\",\r\n" + 
			"    \"http://registry.easytv.eu/common/content/audio/language\": \"CA\",\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/cc/subtitles/fontColor\": \"#ffffff\",\r\n" + 
			"    \"http://registry.easytv.eu/common/display/screen/enhancement/font/type\": \"sans-serif\",\r\n" + 
			"    \"http://registry.easytv.eu/common/display/screen/enhancement/background\": \"#f9c315\",\r\n" + 
			"    \"http://registry.easytv.eu/application/tts/language\": \"EN\",\r\n" + 
			"    \"http://registry.easytv.eu/common/displayContrast\": 27,\r\n" + 
			"    \"http://registry.easytv.eu/application/tts/volume\": 77,\r\n" + 
			"    \"http://registry.easytv.eu/common/content/audio/volume\": 96,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/audio/volume\": 81,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/cc/subtitles/fontSize\": 49,\r\n" + 
			"    \"http://registry.easytv.eu/common/display/screen/enhancement/font/size\": 25,\r\n" + 
			"    \"http://registry.easytv.eu/application/tts/audioQuality\": 30,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/accessibility/faceDetection\": false,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/audio/audioDescription\": false\r\n" + 
			"}}}}");
	
	JSONObject jsonProfile3 = new JSONObject("{\"user_preferences\": {\"default\": {\"preferences\": {\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/cc/audioSubtitles\": false,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/audio/track\": \"ES\",\r\n" + 
			"    \"http://registry.easytv.eu/application/tts/speed\": 27,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/accessibility/textDetection\": false,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/cc/subtitles/backgroundColor\": \"#a27b52\",\r\n" + 
			"    \"http://registry.easytv.eu/common/subtitles\": \"ES\",\r\n" + 
			"    \"http://registry.easytv.eu/common/display/screen/enhancement/font/color\": \"#79ff8\",\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/accessibility/imageMagnification/scale\": 88,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/cc/subtitles/language\": \"GR\",\r\n" + 
			"    \"http://registry.easytv.eu/common/signLanguage\": \"IT\",\r\n" + 
			"    \"http://registry.easytv.eu/common/content/audio/language\": \"IT\",\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/cc/subtitles/fontColor\": \"#d8a0af\",\r\n" + 
			"    \"http://registry.easytv.eu/common/display/screen/enhancement/font/type\": \"sans-serif\",\r\n" + 
			"    \"http://registry.easytv.eu/common/display/screen/enhancement/background\": \"#9f54e5\",\r\n" + 
			"    \"http://registry.easytv.eu/application/tts/language\": \"CA\",\r\n" + 
			"    \"http://registry.easytv.eu/common/displayContrast\": 11,\r\n" + 
			"    \"http://registry.easytv.eu/application/tts/volume\": 25,\r\n" + 
			"    \"http://registry.easytv.eu/common/content/audio/volume\": 61,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/audio/volume\": 64,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/cc/subtitles/fontSize\": 6,\r\n" + 
			"    \"http://registry.easytv.eu/common/display/screen/enhancement/font/size\": 24,\r\n" + 
			"    \"http://registry.easytv.eu/application/tts/audioQuality\": 79,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/accessibility/faceDetection\": false,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/audio/audioDescription\": false\r\n" + 
			"}}}}");
	
	JSONObject jsonProfile4 = new JSONObject("{\"user_preferences\": {\"default\": {\"preferences\": {\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/cc/audioSubtitles\": false,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/audio/track\": \"EN\",\r\n" + 
			"    \"http://registry.easytv.eu/application/tts/speed\": 72,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/accessibility/textDetection\": false,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/cc/subtitles/backgroundColor\": \"#e2dc73\",\r\n" + 
			"    \"http://registry.easytv.eu/common/subtitles\": \"GR\",\r\n" + 
			"    \"http://registry.easytv.eu/common/display/screen/enhancement/font/color\": \"#8c099\",\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/accessibility/imageMagnification/scale\": 74,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/cc/subtitles/language\": \"GR\",\r\n" + 
			"    \"http://registry.easytv.eu/common/signLanguage\": \"CA\",\r\n" + 
			"    \"http://registry.easytv.eu/common/content/audio/language\": \"IT\",\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/cc/subtitles/fontColor\": \"#c3cfce\",\r\n" + 
			"    \"http://registry.easytv.eu/common/display/screen/enhancement/font/type\": \"fantasy\",\r\n" + 
			"    \"http://registry.easytv.eu/common/display/screen/enhancement/background\": \"#42aa57\",\r\n" + 
			"    \"http://registry.easytv.eu/application/tts/language\": \"IT\",\r\n" + 
			"    \"http://registry.easytv.eu/common/displayContrast\": 3,\r\n" + 
			"    \"http://registry.easytv.eu/application/tts/volume\": 61,\r\n" + 
			"    \"http://registry.easytv.eu/common/content/audio/volume\": 15,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/audio/volume\": 49,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/cc/subtitles/fontSize\": 6,\r\n" + 
			"    \"http://registry.easytv.eu/common/display/screen/enhancement/font/size\": 35,\r\n" + 
			"    \"http://registry.easytv.eu/application/tts/audioQuality\": 60,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/accessibility/faceDetection\": false,\r\n" + 
			"    \"http://registry.easytv.eu/application/cs/audio/audioDescription\": false\r\n" + 
			"}}}}");

	
	
	private String[] uris = Preference.getUris();
	private DimensionsGenerator dimensionsGenerator;
	private UserProfile[] userProfiles = new UserProfile[4];
	
	@BeforeTest
	public void beforeTest() throws IOException {
		//Read profiles
		userProfiles[0] = new UserProfile(jsonProfile1);
		userProfiles[1] = new UserProfile(jsonProfile2);
		userProfiles[2] = new UserProfile(jsonProfile3);
		userProfiles[3] = new UserProfile(jsonProfile4);
		
		//Get corresponding dimensions
		dimensionsGenerator = new DimensionsGenerator(Preference.getOperands());
	}
	
	@Test
	public void test_profile_distance_one_Numeric_dimension() throws IOException {
		UserProfile profile1 = userProfiles[0];
		UserProfile profile2 = userProfiles[1];
		
		long mask = MaskGenerator.getMask(uris, "http://registry.easytv.eu/common/content/audio/volume");
				
		DistanceMeasure dist = new MaskedSimilarityMeasure(mask, dimensionsGenerator.getDimensions());
		
		System.out.println(dist.compute(profile1.getPoint(), profile2.getPoint()));
	}
	
	@Test
	public void test_profile_distance_one_nominal_dimension() throws IOException {
		UserProfile profile1 = userProfiles[0];
		UserProfile profile2 = userProfiles[1];
		
		long mask = MaskGenerator.getMask(uris, "http://registry.easytv.eu/common/content/audio/language");
				
		DistanceMeasure dist = new MaskedSimilarityMeasure(mask, dimensionsGenerator.getDimensions());
		
		System.out.println(dist.compute(profile1.getPoint(), profile2.getPoint()));
	}
	
	@Test
	public void test_profile_distance_one_ordinal_dimension() throws IOException {
		UserProfile profile1 = userProfiles[0];
		UserProfile profile2 = userProfiles[1];
		
		long mask = MaskGenerator.getMask(uris, "http://registry.easytv.eu/common/content/audio/language");
				
		DistanceMeasure dist = new MaskedSimilarityMeasure(mask, dimensionsGenerator.getDimensions());
		
		System.out.println(dist.compute(profile1.getPoint(), profile2.getPoint()));
	}
	
	@Test
	public void test_profile_distance_numeric_nominal_dimension() throws IOException {
		UserProfile profile1 = userProfiles[0];
		UserProfile profile2 = userProfiles[1];
		
		long mask = MaskGenerator.getMask(uris, new String[] {"http://registry.easytv.eu/common/content/audio/volume", 
															  "http://registry.easytv.eu/common/content/audio/language"});
				
		DistanceMeasure dist = new MaskedSimilarityMeasure(mask, dimensionsGenerator.getDimensions());
		
		System.out.println(dist.compute(profile1.getPoint(), profile2.getPoint()));
	}
	

}
