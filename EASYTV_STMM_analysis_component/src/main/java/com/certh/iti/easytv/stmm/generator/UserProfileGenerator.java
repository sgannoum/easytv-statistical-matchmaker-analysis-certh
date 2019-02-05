package com.certh.iti.easytv.stmm.generator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.certh.iti.easytv.stmm.generator.preference.operand.RandomBooleanLiteral;
import com.certh.iti.easytv.stmm.generator.preference.operand.RandomColorLiteral;
import com.certh.iti.easytv.stmm.generator.preference.operand.RandomLanguageLiteral;
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
															  RandomIntLiteral.class,   //"tts_speed", 
															  RandomIntLiteral.class,   //"tts_volume", 
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
	
	
	public UserProfileGenerator() {
		rand = new Random();
	}
	
	public UserProfileGenerator(long seed) {
		rand = new Random(seed);
	}
	
	public List<UserProfile> generate(int num) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		
		List<UserProfile> profiles =  new ArrayList<UserProfile>(num);	
		for(int i = 0; i < num; i++) {
		
			General general = new General(rand.nextInt(100), General.getGender(rand.nextInt(1)));
			Visual visualCapabilities = new Visual(rand.nextInt(19) + 1, rand.nextInt(19) + 1, Visual.getColorBlindness(rand.nextInt(7)));
			Auditory auditoryCapabilities = new Auditory(rand.nextInt(90), rand.nextInt(90), rand.nextInt(90), rand.nextInt(90), rand.nextInt(90), rand.nextInt(90));
				
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
			} catch(Exception e) {
				
			}
		}
		return profiles;
	}

}
