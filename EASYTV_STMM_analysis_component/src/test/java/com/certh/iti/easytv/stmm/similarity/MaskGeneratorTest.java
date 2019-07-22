package com.certh.iti.easytv.stmm.similarity;

import java.io.IOException;
import org.testng.annotations.Test;

import com.certh.iti.easytv.user.preference.Preference;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.ColorAttribute;
import com.certh.iti.easytv.user.preference.attributes.IntegerAttribute;
import com.certh.iti.easytv.user.preference.attributes.LanguageAttribute;
import com.certh.iti.easytv.user.preference.attributes.NominalAttribute;

import junit.framework.Assert;

public class MaskGeneratorTest {
	
	String[] uris = Preference.getUris();
	private Attribute[] attribute = Preference.getOperands();
	
	
	@Test
	public void test_mask_generato() throws IOException {
				
		String[] categories = new String[] {uris[0], uris[2]};
		Assert.assertEquals((long) (Math.pow(2, 0) + Math.pow(2, 2)), MaskGenerator.getMask(uris, attribute, categories));
		
		categories = new String[] {uris[0], uris[4], uris[3]};
		Assert.assertEquals((long) (Math.pow(2, 0) + 
									Math.pow(2, 3) + 
									Math.pow(2, 4) + Math.pow(2, 5) + Math.pow(2, 6)), 
									MaskGenerator.getMask(uris, attribute, categories));

	}
	
	@Test
	public void test_categorical_mask_generato() throws IOException {
				
		String[] categories = new String[] {"display/screen/enhancement"};
		Assert.assertEquals((long) (Math.pow(2, 2) + 					 				//http://registry.easytv.eu/common/display/screen/enhancement/font/size
									Math.pow(2, 3) + 									//http://registry.easytv.eu/common/display/screen/enhancement/font/type
									Math.pow(2, 4) + Math.pow(2, 5) + Math.pow(2, 6) +	//http://registry.easytv.eu/common/display/screen/enhancement/font/color 
									Math.pow(2, 7) + Math.pow(2, 8) +  Math.pow(2, 9)), //http://registry.easytv.eu/common/display/screen/enhancement/background
									MaskGenerator.getMask(uris, attribute, categories));

	}
	
	@Test
	public void test_categorical_mask_generato_common() throws IOException {
				
	
		
		String[] categories = new String[] {"common"};
		Assert.assertEquals((long) (Math.pow(2, 0) + 									//http://registry.easytv.eu/common/content/audio/volume
									Math.pow(2, 1) + 									//http://registry.easytv.eu/common/content/audio/language
									Math.pow(2, 2) + 					 				//http://registry.easytv.eu/common/display/screen/enhancement/font/size
									Math.pow(2, 3) + 									//http://registry.easytv.eu/common/display/screen/enhancement/font/type
									Math.pow(2, 4) + Math.pow(2, 5) + Math.pow(2, 6) +	//http://registry.easytv.eu/common/display/screen/enhancement/font/color 
									Math.pow(2, 7) + Math.pow(2, 8) +  Math.pow(2, 9) + //http://registry.easytv.eu/common/display/screen/enhancement/background
									Math.pow(2, 10) +									//http://registry.easytv.eu/common/subtitles
									Math.pow(2, 11) + 									//http://registry.easytv.eu/common/signLanguage
									Math.pow(2, 12)), 									//http://registry.easytv.eu/common/displayContrast
									MaskGenerator.getMask(uris, attribute, categories));

	}
	
	@Test
	public void test_color_mask_generato_1() throws IOException {
				
		String[] categories = new String[] {"http://registry.easytv.eu/common/display/screen/enhancement/font/color"};
		Assert.assertEquals((long) (Math.pow(2, 4) + Math.pow(2, 5) + Math.pow(2, 6)), MaskGenerator.getMask(uris, attribute, categories));

	}
	
	@Test
	public void test_color_mask_generato_2() throws IOException {
				
		String[] categories = new String[] {"http://registry.easytv.eu/common/display/screen/enhancement/background"};
		Assert.assertEquals((long) (Math.pow(2, 7)+ Math.pow(2, 8) + Math.pow(2, 9)), MaskGenerator.getMask(uris, attribute, categories));

	}


}
