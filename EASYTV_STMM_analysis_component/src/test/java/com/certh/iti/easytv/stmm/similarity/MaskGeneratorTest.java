package com.certh.iti.easytv.stmm.similarity;

import java.io.IOException;
import org.testng.annotations.Test;

import com.certh.iti.easytv.user.preference.Preference;
import com.certh.iti.easytv.user.preference.attributes.Attribute;

import junit.framework.Assert;

public class MaskGeneratorTest {
	
	String[] uris = Preference.getUris();
	private Attribute[] attribute = Preference.getOperands();
	
	@Test
	public void test_all_unique_masks() throws IOException {
		
		for(int i = 0, pos = 0; i < uris.length; i++) {
			long mask = 0;
			for(int j = 0; j < attribute[i].getDimensionsNumber(); j++)
				mask |= (long) Math.pow(2, pos++);
			
			Assert.assertEquals(mask, MaskGenerator.getMask(uris, attribute, new String[] {uris[i]}));
		}
	}
	
	@Test
	public void test_mask_generato() throws IOException {
				
		String[] categories = new String[] {uris[0], uris[2]};
		Assert.assertEquals((long) (Math.pow(2, 0) + Math.pow(2, 2)), MaskGenerator.getMask(uris, attribute, categories));
		
		categories = new String[] {uris[0], uris[4], uris[3]};
		Assert.assertEquals((long) (Math.pow(2, 0) + Math.pow(2, 3) + Math.pow(2, 4)+ Math.pow(2, 5) + 
				Math.pow(2, 6) + Math.pow(2, 7)), MaskGenerator.getMask(uris, attribute, categories));

	}
	
	@Test
	public void test_categorical_mask_generato() throws IOException {
				
		String[] categories = new String[] {"display/screen/enhancement"};
		Assert.assertEquals((long) (Math.pow(2, 2) + Math.pow(2, 3) + Math.pow(2, 4) + Math.pow(2, 5) + Math.pow(2, 6) + Math.pow(2, 7) + Math.pow(2, 8)+ Math.pow(2, 9) + Math.pow(2, 10) + Math.pow(2, 11)), MaskGenerator.getMask(uris, attribute, categories));

	}
	
	@Test
	public void test_categorical_mask_generato_common() throws IOException {
				
		String[] categories = new String[] {"common"};
		Assert.assertEquals((long) (Math.pow(2, 0) + Math.pow(2, 1) + Math.pow(2, 2) + Math.pow(2, 3) + Math.pow(2, 4) + Math.pow(2, 5) + 
									Math.pow(2, 6) + Math.pow(2, 7) + Math.pow(2, 8) + Math.pow(2, 9) + Math.pow(2, 10) + Math.pow(2, 11) +
									Math.pow(2, 12) + Math.pow(2, 13) + Math.pow(2, 14)), MaskGenerator.getMask(uris, attribute, categories));

	}
	
	@Test
	public void test_color_mask_generato_1() throws IOException {
				
		String[] categories = new String[] {"http://registry.easytv.eu/common/display/screen/enhancement/font/color"};
		Assert.assertEquals((long) (Math.pow(2, 4) + Math.pow(2, 5) + Math.pow(2, 6) + Math.pow(2, 7)), MaskGenerator.getMask(uris, attribute, categories));

	}
	
	@Test
	public void test_color_mask_generato_2() throws IOException {
				
		String[] categories = new String[] {"http://registry.easytv.eu/common/display/screen/enhancement/background"};
		Assert.assertEquals((long) (Math.pow(2, 8)+ Math.pow(2, 9) + Math.pow(2, 10) + Math.pow(2, 11)), MaskGenerator.getMask(uris, attribute, categories));

	}


}
