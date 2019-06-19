package com.certh.iti.easytv.stmm.clustering;

import java.io.IOException;
import java.util.EnumSet;

import org.json.JSONObject;
import org.testng.annotations.BeforeClass;

import com.certh.iti.easytv.user.UserProfile;

import junit.framework.Assert;

public class UserProfileDistanceMeasureTest {
	
	JSONObject jsonProfile1 = new JSONObject("{\r\n" + 
			"  \"general\": {\r\n" + 
			"    \"age\": 40,\r\n" + 
			"    \"gender\": \"male\"\r\n" + 
			"  },\r\n" + 
			"  \"visual\": {\r\n" + 
			"    \"visual_acuity\": 8,\r\n" + 
			"    \"contrast_sensitivity\": 24,\r\n" + 
			"    \"color_blindness\": \"normal\"\r\n" + 
			"  },\r\n" + 
			"  \"auditory\": {\r\n" + 
			"    \"quarterK\": 81,\r\n" + 
			"    \"halfK\": 35,\r\n" + 
			"    \"oneK\": 98,\r\n" + 
			"    \"twoK\": 18,\r\n" + 
			"    \"fourK\": 57,\r\n" + 
			"    \"eightK\": 27\r\n" + 
			"  },\r\n" + 
			"  \"user_preferences\": {\r\n" + 
			"    \"default\": {\r\n" + 
			"      \"preferences\": {\r\n" + 
			"        \"contrast\": 100,\r\n" + 
			"        \"font_size\": 20,\r\n" + 
			"        \"color_temperature\": 0.008,\r\n" + 
			"        \"language_subtitles\": \"spanish\",\r\n" + 
			"        \"language_sign\": \"spanish\",\r\n" + 
			"        \"language_audio\": \"spanish\",\r\n" +
			"        \"fontColor\": \"#030201\",\r\n" + 
			"        \"backgroundColor\": \"#060504\"\r\n" + 
			"      }\r\n" + 
			"    }\r\n" + 
			"  }\r\n" + 
			"}");
	
	JSONObject jsonProfile2 = new JSONObject("{\r\n" + 
			"  \"general\": {\r\n" + 
			"    \"age\": 50,\r\n" + 
			"    \"gender\": \"female\"\r\n" + 
			"  },\r\n" + 
			"  \"visual\": {\r\n" + 
			"    \"visual_acuity\": 18,\r\n" + 
			"    \"contrast_sensitivity\": 20,\r\n" + 
			"    \"color_blindness\": \"deuteranomaly\"\r\n" + 
			"  },\r\n" + 
			"  \"auditory\": {\r\n" + 
			"    \"quarterK\": 71,\r\n" + 
			"    \"halfK\": 25,\r\n" + 
			"    \"oneK\": 100,\r\n" + 
			"    \"twoK\": 13,\r\n" + 
			"    \"fourK\": 53,\r\n" + 
			"    \"eightK\": 37\r\n" + 
			"  },\r\n" + 
			"  \"user_preferences\": {\r\n" + 
			"    \"default\": {\r\n" + 
			"      \"preferences\": {\r\n" + 
			"        \"contrast\": 90,\r\n" + 
			"        \"font_size\": 40,\r\n" + 
			"        \"color_temperature\": 0.008,\r\n" + 
			"        \"language_subtitles\": \"spanish\",\r\n" + 
			"        \"language_sign\": \"spanish\",\r\n" + 
			"        \"language_audio\": \"spanish\",\r\n" +
			"        \"fontColor\": \"#030201\",\r\n" + 
			"        \"backgroundColor\": \"#060504\"\r\n" + 
			"      }\r\n" + 
			"    }\r\n" + 
			"  }\r\n" + 
			"}");
	
	private UserProfile userProfile1, userProfile2;
	
	
	@BeforeClass
	public void before_class() throws IOException {
		userProfile1 = new UserProfile(jsonProfile1);
		userProfile2 = new UserProfile(jsonProfile2);
	}
	
	//@Test
	public void test_only_general_mask() {
		
		UserProfileDistanceMeasure distanceEuclidean = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.GENERAL), "EUCLIDEAN");
		System.out.println("[EUCLIDEAN][GENERAL]: " +distanceEuclidean.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		UserProfileDistanceMeasure distanceCanberra = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.GENERAL), "CANBERRA");
		System.out.println("[CANBERRA][GENERAL] " +distanceCanberra.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		UserProfileDistanceMeasure distanceChebyshev = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.GENERAL), "CHEBYSHEV");
		System.out.println("[CHEBYSHEV][GENERAL] "+distanceChebyshev.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		UserProfileDistanceMeasure distanceEarthmovers = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.GENERAL), "EARTHMOVERS");
		System.out.println("[EARTHMOVERS][GENERAL] "+distanceEarthmovers.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		UserProfileDistanceMeasure distanceManhattan = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.GENERAL), "MANHATTAN");
		System.out.println("[MANHATTAN][GENERAL]:" +distanceManhattan.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		
		
		
		UserProfileDistanceMeasure distanceMeasure = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.GENERAL), "EUCLIDEAN");
		Assert.assertEquals(10.04987562112089, distanceMeasure.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		distanceMeasure = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.GENERAL), "CANBERRA");
		Assert.assertEquals(2.4579504972013466, distanceMeasure.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		distanceMeasure = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.GENERAL), "CHEBYSHEV");
		Assert.assertEquals(20.0, distanceMeasure.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		distanceMeasure = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.GENERAL), "EARTHMOVERS");
		Assert.assertEquals(136.0, distanceMeasure.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		distanceMeasure = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.GENERAL), "MANHATTAN");
		Assert.assertEquals(86.0, distanceMeasure.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		
		
		
		
	}
	
	//@Test
	public void test_only_visual_mask() {
		
		UserProfileDistanceMeasure distanceEuclidean = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.VISUAL), "EUCLIDEAN");
		System.out.println("[EUCLIDEAN][VISUAL]: " +distanceEuclidean.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		UserProfileDistanceMeasure distanceCanberra = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.VISUAL), "CANBERRA");
		System.out.println("[CANBERRA][VISUAL] " +distanceCanberra.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		UserProfileDistanceMeasure distanceChebyshev = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.VISUAL), "CHEBYSHEV");
		System.out.println("[CHEBYSHEV][VISUAL] "+distanceChebyshev.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		UserProfileDistanceMeasure distanceEarthmovers = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.VISUAL), "EARTHMOVERS");
		System.out.println("[EARTHMOVERS][VISUAL] "+distanceEarthmovers.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		UserProfileDistanceMeasure distanceManhattan = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.VISUAL), "MANHATTAN");
		System.out.println("[MANHATTAN][VISUAL]:" +distanceManhattan.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		
		
		
		UserProfileDistanceMeasure distanceMeasure = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.VISUAL));
		
		Assert.assertEquals(Math.sqrt(117.0), distanceMeasure.compute(userProfile1.getPoint(), userProfile2.getPoint()));
	}
	
	//@Test
	public void test_only_auditory_mask() {
		
		UserProfileDistanceMeasure distanceEuclidean = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.VISUAL), "EUCLIDEAN");
		System.out.println("[EUCLIDEAN][AUDITORY]: " +distanceEuclidean.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		UserProfileDistanceMeasure distanceCanberra = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.AUDITORY), "CANBERRA");
		System.out.println("[CANBERRA][AUDITORY] " +distanceCanberra.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		UserProfileDistanceMeasure distanceChebyshev = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.AUDITORY), "CHEBYSHEV");
		System.out.println("[CHEBYSHEV][AUDITORY] "+distanceChebyshev.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		UserProfileDistanceMeasure distanceEarthmovers = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.AUDITORY), "EARTHMOVERS");
		System.out.println("[EARTHMOVERS][AUDITORY] "+distanceEarthmovers.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		UserProfileDistanceMeasure distanceManhattan = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.AUDITORY), "MANHATTAN");
		System.out.println("[MANHATTAN][AUDITORY]:" +distanceManhattan.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		
		UserProfileDistanceMeasure distanceMeasure = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.AUDITORY));
		
		Assert.assertEquals(Math.sqrt(345.0), distanceMeasure.compute(userProfile1.getPoint(), userProfile2.getPoint()));
	}
	
	//@Test
	public void test_only_preferences_mask() {
		UserProfileDistanceMeasure distanceEuclidean = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.DEFAULT_PREFERENCE), "EUCLIDEAN");
		System.out.println("[EUCLIDEAN][DEFAULT_PREFERENCE]: " +distanceEuclidean.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		UserProfileDistanceMeasure distanceCanberra = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.DEFAULT_PREFERENCE), "CANBERRA");
		System.out.println("[CANBERRA][DEFAULT_PREFERENCE] " +distanceCanberra.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		UserProfileDistanceMeasure distanceChebyshev = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.DEFAULT_PREFERENCE), "CHEBYSHEV");
		System.out.println("[CHEBYSHEV][DEFAULT_PREFERENCE] "+distanceChebyshev.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		UserProfileDistanceMeasure distanceEarthmovers = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.DEFAULT_PREFERENCE), "EARTHMOVERS");
		System.out.println("[EARTHMOVERS][DEFAULT_PREFERENCE] "+distanceEarthmovers.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		UserProfileDistanceMeasure distanceManhattan = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.DEFAULT_PREFERENCE), "MANHATTAN");
		System.out.println("[MANHATTAN][DEFAULT_PREFERENCE]:" +distanceManhattan.compute(userProfile1.getPoint(), userProfile2.getPoint()));
		
		
		UserProfileDistanceMeasure distanceMeasure = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.DEFAULT_PREFERENCE));
		
		Assert.assertEquals(Math.sqrt(500.0), distanceMeasure.compute(userProfile1.getPoint(), userProfile2.getPoint()));
	}
	
	//@Test
	public void test_general_auditory_mask() {
		UserProfileDistanceMeasure distanceMeasure = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.GENERAL, UserProfileDistanceMeasure.CompareType.AUDITORY));
		
		Assert.assertEquals(Math.sqrt(446.0), distanceMeasure.compute(userProfile1.getPoint(), userProfile2.getPoint()));
	}
	
	//@Test
	public void test_general_visual_auditory_mask() {
		UserProfileDistanceMeasure distanceMeasure = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.GENERAL, UserProfileDistanceMeasure.CompareType.AUDITORY, UserProfileDistanceMeasure.CompareType.VISUAL));
		
		Assert.assertEquals(Math.sqrt(563.0), distanceMeasure.compute(userProfile1.getPoint(), userProfile2.getPoint()));
	}
	
	//@Test
	public void test_all_mask() {
		UserProfileDistanceMeasure distanceMeasure = new UserProfileDistanceMeasure(EnumSet.of(UserProfileDistanceMeasure.CompareType.ALL));
		
		Assert.assertEquals(Math.sqrt(1063.0), distanceMeasure.compute(userProfile1.getPoint(), userProfile2.getPoint()));
	}
	

}
