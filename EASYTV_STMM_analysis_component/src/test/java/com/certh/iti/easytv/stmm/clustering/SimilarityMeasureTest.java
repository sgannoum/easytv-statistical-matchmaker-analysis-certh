package com.certh.iti.easytv.stmm.clustering;

import java.io.IOException;
import java.util.Map.Entry;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.json.JSONObject;
import org.testng.annotations.Test;

import com.certh.iti.easytv.stmm.similarity.DimensionsGenerator;
import com.certh.iti.easytv.stmm.similarity.SimilarityMeasure;
import com.certh.iti.easytv.user.UserProfile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;
import com.certh.iti.easytv.user.preference.Preference;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.NumericAttribute;
import com.certh.iti.easytv.user.preference.attributes.OrdinalAttribute;

public class SimilarityMeasureTest {
	JSONObject jsonProfile1 = new JSONObject("{\"user_preferences\": {\"default\": {\"preferences\": {\r\n" + 
			"                        \"http://registry.easytv.eu/common/volume\": 27,\r\n" + 
			"                        \"http://registry.easytv.eu/common/contrast\": 100,\r\n" + 
			"                        \"http://registry.easytv.eu/application/control/voice\": true,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/audio/track\": \"en\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/ui/language\": \"en\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/audio/volume\": 27,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/ui/text/size\": \"16\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/tts/audio/speed\": 0,\r\n" + 
			"                        \"http://registry.easytv.eu/application/tts/audio/voice\": \"male\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/audio/eq/bass\": 0,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/audio/eq/mids\": 0,\r\n" + 
			"                        \"http://registry.easytv.eu/application/tts/audio/volume\": 27,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/audio/eq/highs\": 0,\r\n" + 
			"                        \"http://registry.easytv.eu/common/content/audio/language\": \"en\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/tts/audio/language\": \"en\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/cc/audio/subtitle\": false,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/ui/vibration/touch\": false,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/language\": \"en\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/font/size\": 12,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/font/color\": \"#ffffff\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/sign/language\": \"en\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/ui/text/magnification/scale\": false,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/text\": false,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/ui/audioAssistanceBasedOnTTS\": false,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": false,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/background/color\": \"#000000\",\r\n" + 
			"                        \"http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size\": 1,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/audio/description\": false,\r\n" + 
			"                        \"http://registry.easytv.eu/common/display/screen/enhancement/cursor/color\": \"#ffffff\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/control/csGazeAndGestureControlType\": \"gesture_control\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/character\": false,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/magnification/scale\": 2,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/enhancement/image/type\": \"none\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiLanguage\": \"en\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiTextSize\": 1"+
			"}}}}");
	
	JSONObject jsonProfile2 = new JSONObject("{\"user_preferences\": {\"default\": {\"preferences\": {\r\n" + 
			"                        \"http://registry.easytv.eu/common/volume\": 90,\r\n" + 
			"                        \"http://registry.easytv.eu/common/contrast\": 100,\r\n" + 
			"                        \"http://registry.easytv.eu/application/control/voice\": true,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/audio/track\": \"ca\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/ui/language\": \"en\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/audio/volume\": 33,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/ui/text/size\": \"20\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/tts/audio/speed\": 0,\r\n" + 
			"                        \"http://registry.easytv.eu/application/tts/audio/voice\": \"male\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/audio/eq/bass\": -4,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/audio/eq/mids\": 5,\r\n" + 
			"                        \"http://registry.easytv.eu/application/tts/audio/volume\": 90,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/audio/eq/highs\": -12,\r\n" + 
			"                        \"http://registry.easytv.eu/common/content/audio/language\": \"en\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/tts/audio/language\": \"en\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/cc/audio/subtitle\": false,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/ui/vibration/touch\": true,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/language\": \"en\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/font/size\": 12,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/font/color\": \"#ffffff\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/sign/language\": \"en\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/ui/text/magnification/scale\": true,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/text\": true,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/ui/audioAssistanceBasedOnTTS\": true,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/sound\": true,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/cc/subtitles/background/color\": \"#000000\",\r\n" + 
			"                        \"http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size\": 1,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/audio/description\": false,\r\n" + 
			"                        \"http://registry.easytv.eu/common/display/screen/enhancement/cursor/color\": \"#ffffff\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/control/csGazeAndGestureControlType\": \"gesture_control\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/detection/character\": true,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/magnification/scale\": 2,\r\n" + 
			"                        \"http://registry.easytv.eu/application/cs/accessibility/enhancement/image/type\": \"face-detection\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiLanguage\": \"en\",\r\n" + 
			"                        \"http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiTextSize\": 1"+
			"}}}}");

	
	
	private DimensionsGenerator dimensionsGenerator = new DimensionsGenerator(Preference.getOperands());
	
	@Test
	public void test_profile_distance() throws IOException, UserProfileParsingException {
		UserProfile profile1 = new UserProfile(jsonProfile1);
		UserProfile profile2 = new UserProfile(jsonProfile2);

		for(double d : profile1.getPoint())
			System.out.print(d+",");

		System.out.println();
		
		for(double d : profile2.getPoint())
			System.out.print(d+",");

		System.out.println();
		
		for(Entry<String, Attribute> entry : Preference.preferencesAttributes.entrySet()) {
			Attribute att = entry.getValue();
			if(OrdinalAttribute.class.isInstance(att)) {
				OrdinalAttribute ordinal = OrdinalAttribute.class.cast(att);
				System.out.println(entry.getKey()+": [" + ordinal.getMaxValue() + ", " + ordinal.getMinValue()+"]");
			} else if(NumericAttribute.class.isInstance(att)) {
				NumericAttribute numeric = NumericAttribute.class.cast(att);
				System.out.println(entry.getKey()+": [" + numeric.getMaxValue() + ", " + numeric.getMinValue()+"]");
			}

		}
		
		DistanceMeasure dist = new SimilarityMeasure(dimensionsGenerator.getDimensions());
		
		System.out.println(dist.compute(profile1.getPoint(), profile2.getPoint()));

	}
	
}
