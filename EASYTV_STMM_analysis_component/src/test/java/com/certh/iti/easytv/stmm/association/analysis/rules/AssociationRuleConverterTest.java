package com.certh.iti.easytv.stmm.association.analysis.rules;

import java.util.Vector;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.certh.iti.easytv.stmm.association.analysis.fpgrowth.Itemset;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.Attribute.Bin;
import com.certh.iti.easytv.user.preference.attributes.DoubleAttribute;
import com.certh.iti.easytv.user.preference.attributes.IntegerAttribute;
import com.certh.iti.easytv.user.preference.attributes.NominalAttribute;
import com.certh.iti.easytv.user.preference.attributes.OrdinalAttribute;
import com.certh.iti.easytv.user.preference.attributes.SymmetricBinaryAttribute;

public class AssociationRuleConverterTest {

	@Test
	public void test_integer_no_bins() {
		
		Attribute  integerAttribute = new IntegerAttribute(new double[] {0, 9});

		Bin bin_0 = new Bin();
		bin_0.label = "first";
		bin_0.range = new Object[] {0};
		bin_0.center = 0;
		bin_0.type = integerAttribute;
		
		Bin bin_1 = new Bin();
		bin_1.label = "second";
		bin_1.range = new Object[] {1};
		bin_1.center = 1;
		bin_1.type = integerAttribute;
		
		Vector<Bin> bins = new Vector<Bin>();
		bins.add(bin_0);
		bins.add(bin_1);
			
		AssociationRuleConverter rulesConverter = new AssociationRuleConverter(bins);
		AssociationRule associationRule = new AssociationRule(new Itemset(new int[] {0}), new Itemset(new int[] {1}));
		
		AssociationRuleWrapper actual = rulesConverter.convert(associationRule);
		AssociationRuleWrapper expected = new AssociationRuleWrapper("first = 0 -> second = 1");

		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
	}
	
	@Test
	public void test_integer_bins() {
		
		Attribute  integerAttribute = new IntegerAttribute(new double[] {0, 9});

		Bin bin_0 = new Bin();
		bin_0.label = "first";
		bin_0.range = new Object[] {0, 4};
		bin_0.center = 3;
		bin_0.type = integerAttribute;
		
		Bin bin_1 = new Bin();
		bin_1.label = "second";
		bin_1.range = new Object[] {5, 9};
		bin_1.center = 7;
		bin_1.type = integerAttribute;
		
		Vector<Bin> bins = new Vector<Bin>();
		bins.add(bin_0);
		bins.add(bin_1);
			
		AssociationRuleConverter rulesConverter = new AssociationRuleConverter(bins);
		AssociationRule associationRule = new AssociationRule(new Itemset(new int[] {0}), new Itemset(new int[] {1}));
		
		AssociationRuleWrapper actual = rulesConverter.convert(associationRule);
		AssociationRuleWrapper expected = new AssociationRuleWrapper("first >= 0 ^ first <= 4 -> second = 7");

		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
	}
	
	@Test
	public void test_double_no_bins() {
		
		Attribute  DoubleAttribute = new DoubleAttribute(new double[] {0.0, 9.0});

		Bin bin_0 = new Bin();
		bin_0.label = "first";
		bin_0.range = new Object[] {0.0};
		bin_0.center = 0.0;
		bin_0.type = DoubleAttribute;
		
		Bin bin_1 = new Bin();
		bin_1.label = "second";
		bin_1.range = new Object[] {1.0};
		bin_1.center = 1.0;
		bin_1.type = DoubleAttribute;
		
		Vector<Bin> bins = new Vector<Bin>();
		bins.add(bin_0);
		bins.add(bin_1);
			
		AssociationRuleConverter rulesConverter = new AssociationRuleConverter(bins);
		AssociationRule associationRule = new AssociationRule(new Itemset(new int[] {0}), new Itemset(new int[] {1}));
		
		AssociationRuleWrapper actual = rulesConverter.convert(associationRule);
		AssociationRuleWrapper expected = new AssociationRuleWrapper("first = 0.0 -> second = 1.0");

		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
	}
	
	@Test
	public void test_double_bins() {
		
		Attribute  DoubleAttribute = new DoubleAttribute(new double[] {0.0, 9.0});

		Bin bin_0 = new Bin();
		bin_0.label = "first";
		bin_0.range = new Object[] {0.0, 4.0};
		bin_0.center = 3.0;
		bin_0.type = DoubleAttribute;
		
		Bin bin_1 = new Bin();
		bin_1.label = "second";
		bin_1.range = new Object[] {5.0, 9.0};
		bin_1.center = 7.0;
		bin_1.type = DoubleAttribute;
		
		Vector<Bin> bins = new Vector<Bin>();
		bins.add(bin_0);
		bins.add(bin_1);
			
		AssociationRuleConverter rulesConverter = new AssociationRuleConverter(bins);
		AssociationRule associationRule = new AssociationRule(new Itemset(new int[] {0}), new Itemset(new int[] {1}));
		
		AssociationRuleWrapper actual = rulesConverter.convert(associationRule);
		AssociationRuleWrapper expected = new AssociationRuleWrapper("first >= 0.0 ^ first <= 4.0 -> second = 7.0");

		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
	}
	
	@Test
	public void test_nominal_no_bins() {
		
		Attribute  nominalAttribute = new NominalAttribute(new String[] {"0", "1", "2", "3"});

		Bin bin_0 = new Bin();
		bin_0.label = "first";
		bin_0.range = new Object[] {"0"};
		bin_0.center = "0";
		bin_0.type = nominalAttribute;
		
		Bin bin_1 = new Bin();
		bin_1.label = "second";
		bin_1.range = new Object[] {"1"};
		bin_1.center = "1";
		bin_1.type = nominalAttribute;
		
		Vector<Bin> bins = new Vector<Bin>();
		bins.add(bin_0);
		bins.add(bin_1);
			
		AssociationRuleConverter rulesConverter = new AssociationRuleConverter(bins);
		AssociationRule associationRule = new AssociationRule(new Itemset(new int[] {0}), new Itemset(new int[] {1}));
		
		AssociationRuleWrapper actual = rulesConverter.convert(associationRule);
		AssociationRuleWrapper expected = new AssociationRuleWrapper("first = \"0\" -> second = \"1\"");

		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
	}
	
	@Test
	public void test_ordinal_no_bins() {
		
		Attribute  ordinalAttribute = new OrdinalAttribute(new String[] {"0", "1", "2", "3"});

		Bin bin_0 = new Bin();
		bin_0.label = "first";
		bin_0.range = new Object[] {"0"};
		bin_0.center = "0";
		bin_0.type = ordinalAttribute;
		
		Bin bin_1 = new Bin();
		bin_1.label = "second";
		bin_1.range = new Object[] {"1"};
		bin_1.center = "1";
		bin_1.type = ordinalAttribute;
		
		Vector<Bin> bins = new Vector<Bin>();
		bins.add(bin_0);
		bins.add(bin_1);
			
		AssociationRuleConverter rulesConverter = new AssociationRuleConverter(bins);
		AssociationRule associationRule = new AssociationRule(new Itemset(new int[] {0}), new Itemset(new int[] {1}));
		
		AssociationRuleWrapper actual = rulesConverter.convert(associationRule);
		AssociationRuleWrapper expected = new AssociationRuleWrapper("first = \"0\" -> second = \"1\"");

		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
	}
	
	@Test
	public void test_symetricalBinary_no_bins() {
		
		Attribute  symmetricBinaryAttribute = new SymmetricBinaryAttribute();

		Bin bin_0 = new Bin();
		bin_0.label = "first";
		bin_0.range = new Object[] {true};
		bin_0.center = true;
		bin_0.type = symmetricBinaryAttribute;
		
		Bin bin_1 = new Bin();
		bin_1.label = "second";
		bin_1.range = new Object[] {false};
		bin_1.center = false;
		bin_1.type = symmetricBinaryAttribute;
		
		Vector<Bin> bins = new Vector<Bin>();
		bins.add(bin_0);
		bins.add(bin_1);
			
		AssociationRuleConverter rulesConverter = new AssociationRuleConverter(bins);
		AssociationRule associationRule = new AssociationRule(new Itemset(new int[] {0}), new Itemset(new int[] {1}));
		
		AssociationRuleWrapper actual = rulesConverter.convert(associationRule);
		AssociationRuleWrapper expected = new AssociationRuleWrapper("first = true -> second = false");

		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
	}
	
	@Test
	public void test_multiAttributes() {
		
		Attribute[] attributes = new Attribute[] {
				new IntegerAttribute(new double[] {0, 9}),
				new IntegerAttribute(new double[] {0, 9}),
				new DoubleAttribute(new double[] {0.0, 9.0}),
				new DoubleAttribute(new double[] {0.0, 9.0}),
				new NominalAttribute(new String[] {"0", "1", "2", "3"}),
				new OrdinalAttribute(new String[] {"0", "1", "2", "3"}),
				new SymmetricBinaryAttribute()
		};

		Bin bin_0 = new Bin();
		bin_0.label = "integer_no_bin_1";
		bin_0.range = new Object[] {0};
		bin_0.center = 0;
		bin_0.type = attributes[0];
		
		Bin bin_1 = new Bin();
		bin_1.label = "integer_no_bin_2";
		bin_1.range = new Object[] {1};
		bin_1.center = 1;
		bin_1.type = attributes[0];
		
		Bin bin_2 = new Bin();
		bin_2.label = "integer_bin_1";
		bin_2.range = new Object[] {0, 4};
		bin_2.center = 3;
		bin_2.type = attributes[1];
		
		Bin bin_3 = new Bin();
		bin_3.label = "integer_bin_2";
		bin_3.range = new Object[] {5, 9};
		bin_3.center = 7;
		bin_3.type = attributes[1];
		
		Bin bin_4 = new Bin();
		bin_4.label = "double_no_bin_1";
		bin_4.range = new Object[] {0.0};
		bin_4.center = 0.0;
		bin_4.type = attributes[2];
		
		Bin bin_5 = new Bin();
		bin_5.label = "double_no_bin_1";
		bin_5.range = new Object[] {1.0};
		bin_5.center = 1.0;
		bin_5.type = attributes[2];
		
		Bin bin_6 = new Bin();
		bin_6.label = "double_bin_1";
		bin_6.range = new Object[] {0.0, 4.0};
		bin_6.center = 3.0;
		bin_6.type = attributes[3];
		
		Bin bin_7 = new Bin();
		bin_7.label = "double_bin_2";
		bin_7.range = new Object[] {5.0, 9.0};
		bin_7.center = 7.0;
		bin_7.type = attributes[3];
		
		Bin bin_8 = new Bin();
		bin_8.label = "nominal_no_bin_1";
		bin_8.range = new Object[] {"0"};
		bin_8.center = "0";
		bin_8.type = attributes[4];
		
		Bin bin_9 = new Bin();
		bin_9.label = "nominal_no_bin_2";
		bin_9.range = new Object[] {"1"};
		bin_9.center = "1";
		bin_9.type = attributes[4];
		
		Bin bin_10 = new Bin();
		bin_10.label = "ordinal_no_bin_1";
		bin_10.range = new Object[] {"0"};
		bin_10.center = "0";
		bin_10.type = attributes[5];
		
		Bin bin_11 = new Bin();
		bin_11.label = "ordinal_no_bin_2";
		bin_11.range = new Object[] {"1"};
		bin_11.center = "1";
		bin_11.type = attributes[5];
		
		Bin bin_12 = new Bin();
		bin_12.label = "symetrical_no_bin_1";
		bin_12.range = new Object[] {true};
		bin_12.center = true;
		bin_12.type = attributes[6];
		
		Bin bin_13 = new Bin();
		bin_13.label = "symetrical_no_bin_2";
		bin_13.range = new Object[] {false};
		bin_13.center = false;
		bin_13.type = attributes[6];
		
		Vector<Bin> bins = new Vector<Bin>();
		bins.add(bin_0);
		bins.add(bin_1);
		bins.add(bin_2);
		bins.add(bin_3);
		bins.add(bin_4);
		bins.add(bin_5);
		bins.add(bin_6);
		bins.add(bin_7);
		bins.add(bin_8);
		bins.add(bin_9);
		bins.add(bin_10);
		bins.add(bin_11);
		bins.add(bin_12);
		bins.add(bin_13);

		AssociationRuleWrapper actual, expected; 
		AssociationRuleConverter rulesConverter = new AssociationRuleConverter(bins);
				
		actual = rulesConverter.convert(new AssociationRule(new Itemset(new int[] {0}), new Itemset(new int[] {1})));
		expected = new AssociationRuleWrapper("integer_no_bin_1 = 0 -> integer_no_bin_2 = 1");
		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
		
		actual = rulesConverter.convert(new AssociationRule(new Itemset(new int[] {2}), new Itemset(new int[] {3})));
		expected = new AssociationRuleWrapper("integer_bin_1 >= 0 ^ integer_bin_1 <= 4 -> integer_bin_2 = 7");
		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
		
		actual = rulesConverter.convert(new AssociationRule(new Itemset(new int[] {4}), new Itemset(new int[] {5})));
		expected = new AssociationRuleWrapper("double_no_bin_1 = 0.0 -> double_no_bin_1 = 1.0");
		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
		
		actual = rulesConverter.convert(new AssociationRule(new Itemset(new int[] {6}), new Itemset(new int[] {7})));
		expected = new AssociationRuleWrapper("double_bin_1 >= 0.0 ^ double_bin_1 <= 4.0 -> double_bin_2 = 7.0");
		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
		
		actual = rulesConverter.convert(new AssociationRule(new Itemset(new int[] {8}), new Itemset(new int[] {9})));
		expected = new AssociationRuleWrapper("nominal_no_bin_1 = \"0\" -> nominal_no_bin_2 = \"1\"");
		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
		
		actual = rulesConverter.convert(new AssociationRule(new Itemset(new int[] {10}), new Itemset(new int[] {11})));
		expected = new AssociationRuleWrapper("ordinal_no_bin_1 = \"0\" -> ordinal_no_bin_2 = \"1\"");
		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
		
		actual = rulesConverter.convert(new AssociationRule(new Itemset(new int[] {12}), new Itemset(new int[] {13})));
		expected = new AssociationRuleWrapper("symetrical_no_bin_1 = true -> symetrical_no_bin_2 = false");
		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
	}

}
