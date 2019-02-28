package com.certh.iti.easytv.stmm.generator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.certh.iti.easytv.stmm.generator.preference.operand.RandomBooleanLiteral;
import com.certh.iti.easytv.stmm.generator.preference.operand.RandomColorLiteral;
import com.certh.iti.easytv.stmm.generator.preference.operand.RandomLanguageLiteral;
import com.certh.iti.easytv.stmm.generator.preference.operand.RandomTTSSpeed;
import com.certh.iti.easytv.stmm.generator.preference.operand.RandomTTSVolume;
import com.certh.iti.easytv.stmm.generator.preference.operand.RandomIntLiteral;
import com.certh.iti.easytv.stmm.user.profile.Auditory;
import com.certh.iti.easytv.stmm.user.profile.General;
import com.certh.iti.easytv.stmm.user.profile.UserPreferences;
import com.certh.iti.easytv.stmm.user.profile.UserProfile;
import com.certh.iti.easytv.stmm.user.profile.Visual;
import com.certh.iti.easytv.stmm.user.profile.preference.Preference;
import com.certh.iti.easytv.stmm.user.profile.preference.condition.operand.OperandLiteral;

public class UserProfileGenerator {
	
	private Random rand;
	
	protected static final String[] PREFERENCE_ATTRIBUTE = {"audio_volume",
															"language_audio",
															"contrast",
															"font_size",
														//	"font", 
															"language_subtitles",
															"language_sign", 
															"tts_speed", 
															"tts_volume", 
															"tts_language", 
															"cs_accessibility_imageMagnification_scale",
															"cs_accessibility_textDetection", 
															"cs_audio_volume", 
															"cs_audio_track", 
															"cs_audio_description", 
															"cs_cc_audio_subtitles", 
															"cs_cc_subtitles_language", 
															"cs_cc_subtitles_font_size",
															"fontColor", 
															"backgroundColor"};
	
	protected static final Class<?>[] PREFERENCE_CLASSES = {  RandomIntLiteral.class,	 //"audio_volume",
															  RandomLanguageLiteral.class,	 //"language_audio",
															  RandomIntLiteral.class, 	 //"contrast",
															  RandomIntLiteral.class,	 //"font_size",
															  RandomLanguageLiteral.class,   //"language_subtitles",
															  RandomLanguageLiteral.class,   //"language_sign", 
															  RandomTTSSpeed.class,   //"tts_speed", 
															  RandomTTSVolume.class,   //"tts_volume", 
															  RandomLanguageLiteral.class,  //"tts_language", 
															  RandomIntLiteral.class,   //"cs_accessibility_imageMagnification_scale",
															  RandomBooleanLiteral.class,   //"cs_accessibility_textDetection", 
															  RandomIntLiteral.class,   // "cs_audio_volume", 
															  RandomLanguageLiteral.class,  //"cs_audio_track", 
															  RandomBooleanLiteral.class,  //"cs_audio_description", 
															  RandomBooleanLiteral.class,   //"cs_cc_audio_subtitles", 
															  RandomLanguageLiteral.class, //"cs_cc_subtitles_language", 
															  RandomIntLiteral.class,  //"cs_cc_subtitles_font_size",
															  RandomColorLiteral.class,   //"fontColor", 
															  RandomColorLiteral.class    //"backgroundColor"
															  };
	
	private int[] age_range;
	private int[] gender_range;
	
	private int[] visualAcuity_range;
	private int[] contrastSensitivity_range;
	private int[] colorBlindness_range;
	
    private int[] quarterK_range;
    private int[] halfK_range;
    private int[] oneK_range;
    private int[] twoK_range;
    private int[] fourK_range;
    private int[] eightK_range; 
	
	
	public UserProfileGenerator() {
		rand = new Random();
		this.initateAllRanges();
	}
	
	public UserProfileGenerator(long seed) {
		rand = new Random(seed);
		this.initateAllRanges();
	}
	
	/**
	 * Initiate the limits of the different variables.
	 */
	private void initateAllRanges() {
		age_range = new int[] {20, 100};
		gender_range = new int[] {0, 1};
		
		visualAcuity_range = new int[] {1, 20};
		contrastSensitivity_range = new int[] {1, 20};
		colorBlindness_range = new int[] {0, 6};
		
	    quarterK_range = new int[] {0, 90};
	    halfK_range = new int[] {0, 90};
	    oneK_range = new int[] {0, 90};
	    twoK_range = new int[] {0, 90};
	    fourK_range = new int[] {0, 90};
	    eightK_range = new int[] {0, 90}; 
	}
	
	public void setAgeRange(int[] age) {
		this.age_range = age;
	}

	public void setVisualAcuityRange(int[] visualAcuity) {
		this.visualAcuity_range = visualAcuity;
	}

	public void setContrastSensitivityRange(int[] contrastSensitivity) {
		this.contrastSensitivity_range = contrastSensitivity;
	}

	public void setColorBlindnessRange(int[] colorBlindness) {
		this.colorBlindness_range = colorBlindness;
	}

	public void setQuarterKRange(int[] quarterK) {
		this.quarterK_range = quarterK;
	}

	public void setHalfKRange(int[] halfK) {
		this.halfK_range = halfK;
	}

	public void setOneKRange(int[] oneK) {
		this.oneK_range = oneK;
	}

	public void setTwoKRange(int[] twoK) {
		this.twoK_range = twoK;
	}

	public void setFourKRange(int[] fourK) {
		this.fourK_range = fourK;
	}

	public void setEightKRange(int[] eightK) {
		this.eightK_range = eightK;
	}
	
	public void setGenderRange(int[] gender_range) {
		this.gender_range = gender_range;
	}

	/**
	 * Generate randomly initiated set of user profiles. 
	 * 
	 * @param num
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public List<UserProfile> generate(int num) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		
		List<UserProfile> profiles =  new ArrayList<UserProfile>(num);	
		for(int i = 0; i < num; i++) {
		
			General general = new General(rand.nextInt(age_range[1] - age_range[0]) + age_range[0] , 
										  General.getGender(rand.nextInt(gender_range[1] - gender_range[0]) +  gender_range[0] ));
			
			Visual visualCapabilities = new Visual(rand.nextInt(visualAcuity_range[1] - visualAcuity_range[0]) + visualAcuity_range[0], 
												   rand.nextInt(contrastSensitivity_range[1] - contrastSensitivity_range[0]) + contrastSensitivity_range[0], 
												   Visual.getColorBlindness(rand.nextInt(colorBlindness_range[1] - colorBlindness_range[0]) + colorBlindness_range[0]));
			
			Auditory auditoryCapabilities = new Auditory(rand.nextInt(quarterK_range[1] - quarterK_range[0]) + quarterK_range[0], 
														 rand.nextInt(halfK_range[1] - halfK_range[0]) + halfK_range[0], 
														 rand.nextInt(oneK_range[1] - oneK_range[0]) + oneK_range[0], 
														 rand.nextInt(twoK_range[1] - twoK_range[0]) + twoK_range[0], 
														 rand.nextInt(fourK_range[1] - fourK_range[0]) + fourK_range[0], 
														 rand.nextInt(eightK_range[1] - eightK_range[0]) + eightK_range[0]);
				
			Map<String, OperandLiteral> map = new HashMap<String, OperandLiteral>();
			for(int j = 0 ; j < PREFERENCE_CLASSES.length; j++) {
				Class<?> cls = PREFERENCE_CLASSES[j];
				map.put(PREFERENCE_ATTRIBUTE[j], (OperandLiteral) cls.getConstructor(Random.class).newInstance(rand));
			}
			
			Preference defaultPreference = new Preference("default", map);
			List<Preference> preferences = new ArrayList<Preference>();
			preferences.add(defaultPreference);
			UserPreferences userPreferences = new UserPreferences(preferences);
			
			try {
				profiles.add(new UserProfile(general, visualCapabilities, auditoryCapabilities, userPreferences, false));
			} catch(Exception e) {}
		}
		return profiles;
	}

}
