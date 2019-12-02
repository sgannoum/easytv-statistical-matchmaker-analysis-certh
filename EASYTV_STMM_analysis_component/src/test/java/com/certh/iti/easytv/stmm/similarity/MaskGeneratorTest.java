package com.certh.iti.easytv.stmm.similarity;

import java.io.IOException;

import org.testng.annotations.Test;

import com.certh.iti.easytv.user.Profile;

import junit.framework.Assert;

public class MaskGeneratorTest {
	
	String[] uris = Profile.getUris();
	
	
	@Test
	public void test_mask_generato() throws IOException {
				
		String[] categories = new String[] {uris[0], uris[2]};
		Assert.assertEquals((long) (Math.pow(2, 0) + Math.pow(2, 2)), MaskGenerator.getMask(uris, categories));
		
		categories = new String[] {uris[0], uris[4], uris[3]};
		Assert.assertEquals((long) (Math.pow(2, 0) + 
									Math.pow(2, 3) + 
									Math.pow(2, 4) ), 
									MaskGenerator.getMask(uris, categories));

	}
	
	@Test
	public void test_categorical_mask_generato() throws IOException {
				
		String[] categories = new String[] {"display/screen/enhancement"};
		Assert.assertEquals((long) (
									Math.pow(2, 3) + 									//http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size 
									Math.pow(2, 4) ), 									//http://registry.easytv.eu/common/display/screen/enhancement/cursor/color
									MaskGenerator.getMask(uris, categories));

	}
	
	@Test
	public void test_categorical_mask_generato_common() throws IOException {

		String[] categories = new String[] {"common"};
		Assert.assertEquals((long) (Math.pow(2, 0) + 									//http://registry.easytv.eu/common/volume
									Math.pow(2, 1) + 									//http://registry.easytv.eu/common/contrast
									Math.pow(2, 2) + 					 				//http://registry.easytv.eu/common/content/audio/language
									Math.pow(2, 3) + 									//http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size
									Math.pow(2, 4) ), 									//http://registry.easytv.eu/common/display/screen/enhancement/cursor/color 
									
									MaskGenerator.getMask(uris, categories));

	}
	
	@Test
	public void test_color_mask_generato_1() throws IOException {
				
		String[] categories = new String[] {"http://registry.easytv.eu/application/cs/cc/subtitles/language"};
		Assert.assertEquals((long) (Math.pow(2, 10)), MaskGenerator.getMask(uris, categories));

	}

}
