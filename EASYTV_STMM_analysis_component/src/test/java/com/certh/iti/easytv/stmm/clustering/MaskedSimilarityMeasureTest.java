package com.certh.iti.easytv.stmm.clustering;

import java.io.IOException;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.certh.iti.easytv.stmm.similarity.DimensionsGenerator;
import com.certh.iti.easytv.stmm.similarity.MaskGenerator;
import com.certh.iti.easytv.stmm.similarity.MaskedSimilarityMeasure;
import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.UserProfile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

public class MaskedSimilarityMeasureTest {
	JSONObject jsonProfile1 = new JSONObject("{\"user_preferences\": {\"default\": {\"preferences\": {" + 
			"\"http://registry.easytv.eu/common/volume\": 22," + 
			"\"http://registry.easytv.eu/common/contrast\": 100," + 
			"\"http://registry.easytv.eu/application/control/voice\": true," + 
			"\"http://registry.easytv.eu/application/cs/audio/track\": \"en\"," + 
			"\"http://registry.easytv.eu/application/cs/ui/language\": \"en\"," + 
			"\"http://registry.easytv.eu/application/cs/audio/volume\": 2," + 
			"\"http://registry.easytv.eu/application/cs/ui/text/size\": \"15\"," + 
			"\"http://registry.easytv.eu/application/tts/audio/speed\": 3," + 
			"\"http://registry.easytv.eu/application/tts/audio/voice\": \"male\"," + 
			"\"http://registry.easytv.eu/application/cs/audio/eq/bass\": 0," + 
			"\"http://registry.easytv.eu/application/cs/audio/eq/mids\": 0," + 
			"\"http://registry.easytv.eu/application/tts/audio/volume\": 32," + 
			"\"http://registry.easytv.eu/application/cs/audio/eq/highs\": 0," + 
			"\"http://registry.easytv.eu/common/content/audio/language\": \"gr\"," + 
			"\"http://registry.easytv.eu/application/tts/audio/language\": \"en\"," + 
			"\"http://registry.easytv.eu/application/cs/cc/audio/subtitle\": false," + 
			"\"http://registry.easytv.eu/application/cs/ui/vibration/touch\": false," + 
			"\"http://registry.easytv.eu/application/cs/cc/subtitles/language\": \"en\"," + 
			"\"http://registry.easytv.eu/application/cs/cc/subtitles/font/size\": 12," + 
			"\"http://registry.easytv.eu/application/cs/cc/subtitles/font/color\": \"#ffffff\"," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/sign/language\": \"en\"," + 
			"\"http://registry.easytv.eu/application/cs/ui/text/magnification/scale\": false," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/detection/text\": false," + 
			"\"http://registry.easytv.eu/application/cs/ui/audioAssistanceBasedOnTTS\": false," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": false," + 
			"\"http://registry.easytv.eu/application/cs/cc/subtitles/background/color\": \"#000000\"," + 
			"\"http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size\": 1," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/audio/description\": false," + 
			"\"http://registry.easytv.eu/common/display/screen/enhancement/cursor/color\": \"#ffffff\"," + 
			"\"http://registry.easytv.eu/application/control/csGazeAndGestureControlType\": \"gesture_control\"," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/detection/character\": false," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/magnification/scale\": 2," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/enhancement/image/type\": \"none\"," + 
			"\"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiLanguage\": \"en\"," + 
			"\"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiTextSize\": 1"+
			"}}}}");
	
	JSONObject jsonProfile2 = new JSONObject("{\"user_preferences\": {\"default\": {\"preferences\": {" + 
			"\"http://registry.easytv.eu/common/volume\": 90," + 
			"\"http://registry.easytv.eu/common/contrast\": 100," + 
			"\"http://registry.easytv.eu/application/control/voice\": true," + 
			"\"http://registry.easytv.eu/application/cs/audio/track\": \"ca\"," + 
			"\"http://registry.easytv.eu/application/cs/ui/language\": \"en\"," + 
			"\"http://registry.easytv.eu/application/cs/audio/volume\": 3," + 
			"\"http://registry.easytv.eu/application/cs/ui/text/size\": \"20\"," + 
			"\"http://registry.easytv.eu/application/tts/audio/speed\": 0," + 
			"\"http://registry.easytv.eu/application/tts/audio/voice\": \"male\"," + 
			"\"http://registry.easytv.eu/application/cs/audio/eq/bass\": -4," + 
			"\"http://registry.easytv.eu/application/cs/audio/eq/mids\": 5," + 
			"\"http://registry.easytv.eu/application/tts/audio/volume\": 90," + 
			"\"http://registry.easytv.eu/application/cs/audio/eq/highs\": -12," + 
			"\"http://registry.easytv.eu/common/content/audio/language\": \"en\"," + 
			"\"http://registry.easytv.eu/application/tts/audio/language\": \"en\"," + 
			"\"http://registry.easytv.eu/application/cs/cc/audio/subtitle\": false," + 
			"\"http://registry.easytv.eu/application/cs/ui/vibration/touch\": true," + 
			"\"http://registry.easytv.eu/application/cs/cc/subtitles/language\": \"en\"," + 
			"\"http://registry.easytv.eu/application/cs/cc/subtitles/font/size\": 12," + 
			"\"http://registry.easytv.eu/application/cs/cc/subtitles/font/color\": \"#ffffff\"," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/sign/language\": \"en\"," + 
			"\"http://registry.easytv.eu/application/cs/ui/text/magnification/scale\": true," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/detection/text\": true," + 
			"\"http://registry.easytv.eu/application/cs/ui/audioAssistanceBasedOnTTS\": true," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": true," + 
			"\"http://registry.easytv.eu/application/cs/cc/subtitles/background/color\": \"#000000\"," + 
			"\"http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size\": 1," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/audio/description\": false," + 
			"\"http://registry.easytv.eu/common/display/screen/enhancement/cursor/color\": \"#ffffff\"," + 
			"\"http://registry.easytv.eu/application/control/csGazeAndGestureControlType\": \"gesture_control\"," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/detection/character\": true," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/magnification/scale\": 2," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/enhancement/image/type\": \"face-detection\"," + 
			"\"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiLanguage\": \"en\"," + 
			"\"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiTextSize\": 1"+
			"}}}}");
	
	JSONObject jsonProfile3 = new JSONObject("{\"user_preferences\": {\"default\": {\"preferences\": {" + 
			"\"http://registry.easytv.eu/common/volume\": 13," + 
			"\"http://registry.easytv.eu/common/contrast\": 100," + 
			"\"http://registry.easytv.eu/application/control/voice\": true," + 
			"\"http://registry.easytv.eu/application/cs/audio/track\": \"en\"," + 
			"\"http://registry.easytv.eu/application/cs/ui/language\": \"en\"," + 
			"\"http://registry.easytv.eu/application/cs/audio/volume\": 13," + 
			"\"http://registry.easytv.eu/application/cs/ui/text/size\": \"15\"," + 
			"\"http://registry.easytv.eu/application/tts/audio/speed\": 0," + 
			"\"http://registry.easytv.eu/application/tts/audio/voice\": \"male\"," + 
			"\"http://registry.easytv.eu/application/cs/audio/eq/bass\": 0," + 
			"\"http://registry.easytv.eu/application/cs/audio/eq/mids\": 0," + 
			"\"http://registry.easytv.eu/application/tts/audio/volume\": 13," + 
			"\"http://registry.easytv.eu/application/cs/audio/eq/highs\": 0," + 
			"\"http://registry.easytv.eu/common/content/audio/language\": \"en\"," + 
			"\"http://registry.easytv.eu/application/tts/audio/language\": \"en\"," + 
			"\"http://registry.easytv.eu/application/cs/cc/audio/subtitle\": false," + 
			"\"http://registry.easytv.eu/application/cs/ui/vibration/touch\": false," + 
			"\"http://registry.easytv.eu/application/cs/cc/subtitles/language\": \"en\"," + 
			"\"http://registry.easytv.eu/application/cs/cc/subtitles/font/size\": 12," + 
			"\"http://registry.easytv.eu/application/cs/cc/subtitles/font/color\": \"#ffffff\"," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/sign/language\": \"en\"," + 
			"\"http://registry.easytv.eu/application/cs/ui/text/magnification/scale\": false," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/detection/text\": true," + 
			"\"http://registry.easytv.eu/application/cs/ui/audioAssistanceBasedOnTTS\": false," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": true," + 
			"\"http://registry.easytv.eu/application/cs/cc/subtitles/background/color\": \"#000000\"," + 
			"\"http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size\": 1," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/audio/description\": false," + 
			"\"http://registry.easytv.eu/common/display/screen/enhancement/cursor/color\": \"#ffffff\"," + 
			"\"http://registry.easytv.eu/application/control/csGazeAndGestureControlType\": \"gesture_control\"," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/detection/character\": true," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/magnification/scale\": 2.5," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/enhancement/image/type\": \"image-magnification\"," + 
			"\"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiLanguage\": \"en\"," + 
			"\"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiTextSize\": 1"+
			"}}}}");
	
	JSONObject jsonProfile4 = new JSONObject("{\"user_preferences\": {\"default\": {\"preferences\": {" + 
			"\"http://registry.easytv.eu/common/volume\": 27," + 
			"\"http://registry.easytv.eu/common/contrast\": 100," + 
			"\"http://registry.easytv.eu/application/control/voice\": true," + 
			"\"http://registry.easytv.eu/application/cs/audio/track\": \"en\"," + 
			"\"http://registry.easytv.eu/application/cs/ui/language\": \"en\"," + 
			"\"http://registry.easytv.eu/application/cs/audio/volume\": 7," + 
			"\"http://registry.easytv.eu/application/cs/ui/text/size\": \"15\"," + 
			"\"http://registry.easytv.eu/application/tts/audio/speed\": 0," + 
			"\"http://registry.easytv.eu/application/tts/audio/voice\": \"male\"," + 
			"\"http://registry.easytv.eu/application/cs/audio/eq/bass\": 0," + 
			"\"http://registry.easytv.eu/application/cs/audio/eq/mids\": 0," + 
			"\"http://registry.easytv.eu/application/tts/audio/volume\": 7," + 
			"\"http://registry.easytv.eu/application/cs/audio/eq/highs\": 0," + 
			"\"http://registry.easytv.eu/common/content/audio/language\": \"en\"," + 
			"\"http://registry.easytv.eu/application/tts/audio/language\": \"en\"," + 
			"\"http://registry.easytv.eu/application/cs/cc/audio/subtitle\": false," + 
			"\"http://registry.easytv.eu/application/cs/ui/vibration/touch\": false," + 
			"\"http://registry.easytv.eu/application/cs/cc/subtitles/language\": \"en\"," + 
			"\"http://registry.easytv.eu/application/cs/cc/subtitles/font/size\": 12," + 
			"\"http://registry.easytv.eu/application/cs/cc/subtitles/font/color\": \"#ffffff\"," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/sign/language\": \"en\"," + 
			"\"http://registry.easytv.eu/application/cs/ui/text/magnification/scale\": false," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/detection/text\": false," + 
			"\"http://registry.easytv.eu/application/cs/ui/audioAssistanceBasedOnTTS\": false," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": false," + 
			"\"http://registry.easytv.eu/application/cs/cc/subtitles/background/color\": \"#000000\"," + 
			"\"http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size\": 1," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/audio/description\": false," + 
			"\"http://registry.easytv.eu/common/display/screen/enhancement/cursor/color\": \"#ffffff\"," + 
			"\"http://registry.easytv.eu/application/control/csGazeAndGestureControlType\": \"gesture_control\"," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/detection/character\": false," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/magnification/scale\": 2," + 
			"\"http://registry.easytv.eu/application/cs/accessibility/enhancement/image/type\": \"none\"," + 
			"\"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiLanguage\": \"en\"," + 
			"\"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiTextSize\": 1"+
			"}}}}");

	
	
	private DimensionsGenerator dimensionsGenerator;
	private UserProfile[] userProfiles = new UserProfile[4];
	
	@BeforeTest
	public void beforeTest() throws IOException, UserProfileParsingException {
		//Read profiles
		userProfiles[0] = new UserProfile(jsonProfile1);
		userProfiles[1] = new UserProfile(jsonProfile2);
		userProfiles[2] = new UserProfile(jsonProfile3);
		userProfiles[3] = new UserProfile(jsonProfile4);
		
		//Get corresponding dimensions
		dimensionsGenerator = new DimensionsGenerator(Profile.getUris(), Profile.getOperands());
	}
	
	@Test
	public void test_profile_distance_one_Numeric_dimension() throws IOException {
		UserProfile profile1 = userProfiles[0];
		UserProfile profile2 = userProfiles[1];
		
		long mask = MaskGenerator.getMask(dimensionsGenerator.getLables(), new String[] {"http://registry.easytv.eu/common/volume"});
				
		DistanceMeasure dist = new MaskedSimilarityMeasure(mask, dimensionsGenerator.getLables(), dimensionsGenerator.getDimensions());
		
		System.out.println(dist.compute(profile1.getPoint(), profile2.getPoint()));
	}
	
	@Test
	public void test_profile_distance_one_nominal_dimension() throws IOException {
		UserProfile profile1 = userProfiles[0];
		UserProfile profile2 = userProfiles[1];
		
		long mask = MaskGenerator.getMask(dimensionsGenerator.getLables(), new String[] {"http://registry.easytv.eu/common/volume"});
				
		DistanceMeasure dist = new MaskedSimilarityMeasure(mask, dimensionsGenerator.getLables(), dimensionsGenerator.getDimensions());
		
		System.out.println(dist.compute(profile1.getPoint(), profile2.getPoint()));
	}
	
	@Test
	public void test_profile_distance_one_ordinal_dimension() throws IOException {
		UserProfile profile1 = userProfiles[0];
		UserProfile profile2 = userProfiles[1];
		
		long mask = MaskGenerator.getMask(dimensionsGenerator.getLables(), new String[] {"http://registry.easytv.eu/common/volume"});
				
		DistanceMeasure dist = new MaskedSimilarityMeasure(mask, dimensionsGenerator.getLables(), dimensionsGenerator.getDimensions());
		
		System.out.println(dist.compute(profile1.getPoint(), profile2.getPoint()));
	}
	
	@Test
	public void test_profile_distance_numeric_nominal_dimension() throws IOException {
		UserProfile profile1 = userProfiles[0];
		UserProfile profile2 = userProfiles[1];
		
		long mask = MaskGenerator.getMask(dimensionsGenerator.getLables(), new String[] {"http://registry.easytv.eu/common/volume", 
															  "http://registry.easytv.eu/common/volume"});
				
		DistanceMeasure dist = new MaskedSimilarityMeasure(mask, dimensionsGenerator.getLables(), dimensionsGenerator.getDimensions());
		
		System.out.println(dist.compute(profile1.getPoint(), profile2.getPoint()));
	}
	

}
