package com.certh.iti.easytv.stmm.similarity;

import java.io.IOException;
import org.testng.annotations.Test;

import com.certh.iti.easytv.user.preference.Preference;

import junit.framework.Assert;

public class MaskGeneratorTest {
	
	String[] uris = Preference.getUris();
	
	
	@Test
	public void test_all_unique_masks() throws IOException {
				
		for(int i = 0; i < uris.length; i++)
			Assert.assertEquals((long) Math.pow(2, i), MaskGenerator.getMask(uris, uris[i]));
	}
	
	@Test
	public void test_mask_generato() throws IOException {
				
		String[] categories = new String[] {uris[0], uris[2]};
		Assert.assertEquals((long) (Math.pow(2, 0) + Math.pow(2, 2)), MaskGenerator.getMask(uris, categories));
		
		categories = new String[] {uris[0], uris[4], uris[3]};
		Assert.assertEquals((long) (Math.pow(2, 0) + Math.pow(2, 3) + Math.pow(2, 4)), MaskGenerator.getMask(uris, categories));

	}
	
	@Test
	public void test_categorical_mask_generato() throws IOException {
				
		String[] categories = new String[] {"display/screen/enhancement"};
		Assert.assertEquals((long) (Math.pow(2, 2) + Math.pow(2, 3) + Math.pow(2, 4) + Math.pow(2, 5)), MaskGenerator.getMask(uris, categories));

	}
	
	@Test
	public void test_categorical_mask_generato_repeated_uris() throws IOException {
				
		String[] categories = new String[] {"display/screen/enhancement"};
		Assert.assertEquals((long) (Math.pow(2, 2) + Math.pow(2, 3) + Math.pow(2, 4) + Math.pow(2, 5)), MaskGenerator.getMask(uris, categories));

	}
	
	@Test
	public void test_categorical_mask_generato_common() throws IOException {
				
		String[] categories = new String[] {"common"};
		Assert.assertEquals((long) (Math.pow(2, 0) + Math.pow(2, 1) + Math.pow(2, 2) + Math.pow(2, 3) + Math.pow(2, 4) + Math.pow(2, 5) + Math.pow(2, 6) + Math.pow(2, 7) + Math.pow(2, 8)), MaskGenerator.getMask(uris, categories));

	}


}
