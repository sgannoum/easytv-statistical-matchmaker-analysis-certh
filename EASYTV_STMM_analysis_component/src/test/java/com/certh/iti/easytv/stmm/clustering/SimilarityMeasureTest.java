package com.certh.iti.easytv.stmm.clustering;

import java.io.IOException;
import java.util.Map.Entry;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.json.JSONObject;
import org.testng.annotations.Test;

import com.certh.iti.easytv.stmm.similarity.DimensionsGenerator;
import com.certh.iti.easytv.stmm.similarity.SimilarityMeasure;
import com.certh.iti.easytv.user.UserProfile;
import com.certh.iti.easytv.user.preference.Preference;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.NumericAttribute;
import com.certh.iti.easytv.user.preference.attributes.OrdinalAttribute;

public class SimilarityMeasureTest {
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

	
	
	private DimensionsGenerator dimensionsGenerator = new DimensionsGenerator(Preference.getOperands());
	
	@Test
	public void test_profile_distance() throws IOException {
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
