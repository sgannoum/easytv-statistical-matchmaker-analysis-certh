package com.certh.iti.easytv.stmm.similarity.dimension;

import java.io.IOException;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class MultiNumericTest {
	
	
	@Test
	public void test_multidimension_missing() throws IOException {
		Numeric[] dimensions = {new Numeric(255, 0), new Numeric(255, 0), new Numeric(255, 0) };
		MultiNumeric multiNumeric = new MultiNumeric(dimensions, 8);

		double a  = -1, b = -1;
		
		double[] actual = multiNumeric.dissimilarity(a, b);
		double[] expected = {0.0, 0.0};
		
		for(int i = 0; i < actual.length; i ++) 
			Assert.assertEquals(expected[i], actual[i]);
		
	}

	@Test
	public void test_multidimension() throws IOException {
		Numeric[] dimensions = {new Numeric(255, 0), new Numeric(255, 0), new Numeric(255, 0) };
		MultiNumeric multiNumeric = new MultiNumeric(dimensions, 8);

		double a  = 255, b = 60000;
		
		double[] actual = multiNumeric.dissimilarity(a, b);
		double[] expected = {1.0, 0.6235294117647059,
							1.0, 0.9176470588235294,
							1.0, 0.0};
		
		for(int i = 0; i < actual.length; i ++) 
			Assert.assertEquals(expected[i], actual[i]);
	}

}
