package com.certh.iti.easytv.stmm.association.analysis.rules;

import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.certh.iti.easytv.stmm.association.analysis.fpgrowth.Itemset;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.AttributesAggregator;
import com.certh.iti.easytv.user.preference.attributes.DoubleAttribute;
import com.certh.iti.easytv.user.preference.attributes.IntegerAttribute;
import com.certh.iti.easytv.user.preference.attributes.NominalAttribute;
import com.certh.iti.easytv.user.preference.attributes.OrdinalAttribute;
import com.certh.iti.easytv.user.preference.attributes.SymmetricBinaryAttribute;

public class AssociationRuleConverterTest {

	@Test
	public void test_integer_no_bins() {
				
		Map<String, Attribute> attributes  =  new LinkedHashMap<String, Attribute>() {
			private static final long serialVersionUID = 1L;
		{	
	     	put("first", new IntegerAttribute(new double[] {0.0, 0.0}, 1.0));
	     	put("second", new IntegerAttribute(new double[] {1.0, 1.0}, 1.0));
	    }};
		
	    //load values
	    attributes.get("first").handle(0);
	    attributes.get("second").handle(1);

	    AttributesAggregator aggregator = new AttributesAggregator();
	    aggregator.add(attributes);

		AssociationRuleConverter rulesConverter = new AssociationRuleConverter(aggregator);
		AssociationRule associationRule = new AssociationRule(new Itemset(new int[] {0}), new Itemset(new int[] {1}));
		
		AssociationRuleWrapper actual = rulesConverter.convert(associationRule);
		AssociationRuleWrapper expected = new AssociationRuleWrapper("first = 0 -> second = 1");

		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
	}
	
	@Test
	public void test_integer_bins() {
		
		Map<String, Attribute> attributes  =  new LinkedHashMap<String, Attribute>() {
			private static final long serialVersionUID = 1L;
		{	
	     	put("first", new IntegerAttribute(new double[] {0.0, 4.0}, 1.0, 1, -1));
	     	put("second", new IntegerAttribute(new double[] {5.0, 9.0}, 1.0, 1, -1));
	    }};
		
	    //load values
	    attributes.get("first").handle(0); attributes.get("first").handle(4);
	    attributes.get("second").handle(7);
	    
	    AttributesAggregator aggregator = new AttributesAggregator();
	    aggregator.add(attributes);
			
		AssociationRuleConverter rulesConverter = new AssociationRuleConverter(aggregator);
		AssociationRule associationRule = new AssociationRule(new Itemset(new int[] {0}), new Itemset(new int[] {1}));
		
		AssociationRuleWrapper actual = rulesConverter.convert(associationRule);
		AssociationRuleWrapper expected = new AssociationRuleWrapper("first >= 0 ^ first <= 4 -> second = 7");

		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
	}
	
	@Test
	public void test_double_no_bins() {
		
		Map<String, Attribute> attributes  =  new LinkedHashMap<String, Attribute>() {
			private static final long serialVersionUID = 1L;
		{	
	     	put("first", new DoubleAttribute(new double[] {0.0, 0.0}, 1.0));
	     	put("second", new DoubleAttribute(new double[] {1.0, 1.0}, 1.0));
	    }};
	    
	    //load values
	    attributes.get("first").handle(0.0);
	    attributes.get("second").handle(1.0);
		
	    AttributesAggregator aggregator = new AttributesAggregator();
	    aggregator.add(attributes);
			
		AssociationRuleConverter rulesConverter = new AssociationRuleConverter(aggregator);
		AssociationRule associationRule = new AssociationRule(new Itemset(new int[] {0}), new Itemset(new int[] {1}));
		
		AssociationRuleWrapper actual = rulesConverter.convert(associationRule);
		AssociationRuleWrapper expected = new AssociationRuleWrapper("first = 0.0 -> second = 1.0");

		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
	}
	
	@Test
	public void test_double_bins() {
		
		Map<String, Attribute> attributes  =  new LinkedHashMap<String, Attribute>() {
			private static final long serialVersionUID = 1L;
		{	
	     	put("first", new DoubleAttribute(new double[] {0.0, 4.0}, 1.0, 1, -1));
	     	put("second", new DoubleAttribute(new double[] {5.0, 9.0}, 1.0, 1, -1));
	    }};
		
	    //load values
	    attributes.get("first").handle(0.0);  attributes.get("first").handle(4.0);
	    attributes.get("second").handle(7.0);
	    
	    AttributesAggregator aggregator = new AttributesAggregator();
	    aggregator.add(attributes);
		
			
		AssociationRuleConverter rulesConverter = new AssociationRuleConverter(aggregator);
		AssociationRule associationRule = new AssociationRule(new Itemset(new int[] {0}), new Itemset(new int[] {1}));
		
		AssociationRuleWrapper actual = rulesConverter.convert(associationRule);
		AssociationRuleWrapper expected = new AssociationRuleWrapper("first >= 0.0 ^ first <= 4.0 -> second = 7.0");

		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
	}
	
	@Test
	public void test_nominal_no_bins() {
		
		Map<String, Attribute> attributes  =  new LinkedHashMap<String, Attribute>() {
			private static final long serialVersionUID = 1L;
		{	
	     	put("first", new NominalAttribute(new String[] {"0"}));
	     	put("second", new NominalAttribute(new String[] {"1"}));
	    }};
		
	    //load values
	    attributes.get("first").handle("0"); 
	    attributes.get("second").handle("1");
	    
	    AttributesAggregator aggregator = new AttributesAggregator();
	    aggregator.add(attributes);
		
		
		AssociationRuleConverter rulesConverter = new AssociationRuleConverter(aggregator);
		AssociationRule associationRule = new AssociationRule(new Itemset(new int[] {0}), new Itemset(new int[] {1}));
		
		AssociationRuleWrapper actual = rulesConverter.convert(associationRule);
		AssociationRuleWrapper expected = new AssociationRuleWrapper("first = \"0\" -> second = \"1\"");

		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
	}
	
	@Test
	public void test_ordinal_no_bins() {
		
		Map<String, Attribute> attributes  =  new LinkedHashMap<String, Attribute>() {
			private static final long serialVersionUID = 1L;
		{	
	     	put("first", new NominalAttribute(new String[] {"0"}));
	     	put("second", new NominalAttribute(new String[] {"1"}));
	    }};
		
	    //load values
	    attributes.get("first").handle("0"); 
	    attributes.get("second").handle("1");
	    
	    
	    AttributesAggregator aggregator = new AttributesAggregator();
	    aggregator.add(attributes);
			
		AssociationRuleConverter rulesConverter = new AssociationRuleConverter(aggregator);
		AssociationRule associationRule = new AssociationRule(new Itemset(new int[] {0}), new Itemset(new int[] {1}));
		
		AssociationRuleWrapper actual = rulesConverter.convert(associationRule);
		AssociationRuleWrapper expected = new AssociationRuleWrapper("first = \"0\" -> second = \"1\"");

		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
	}
	
	@Test
	public void test_symetricalBinary_no_bins() {
		
		Map<String, Attribute> attributes  =  new LinkedHashMap<String, Attribute>() {
			private static final long serialVersionUID = 1L;
		{	
	     	put("first", new SymmetricBinaryAttribute());
	     	put("second", new SymmetricBinaryAttribute());
	    }};
	    
	    //load values
	    attributes.get("first").handle(false); 
	    attributes.get("second").handle(false); 
		
	    AttributesAggregator aggregator = new AttributesAggregator();
	    aggregator.add(attributes);
			
		AssociationRuleConverter rulesConverter = new AssociationRuleConverter(aggregator);
		AssociationRule associationRule = new AssociationRule(new Itemset(new int[] {0}), new Itemset(new int[] {2}));
		
		AssociationRuleWrapper actual = rulesConverter.convert(associationRule);
		AssociationRuleWrapper expected = new AssociationRuleWrapper("first = false -> second = false");

		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
	}
	
	@Test
	public void test_multiAttributes() {
		
		Map<String, Attribute> attributes  =  new LinkedHashMap<String, Attribute>() {
			private static final long serialVersionUID = 1L;
		{	
	     	put("integer_no_bin", new IntegerAttribute(new double[] {0, 1}));
	     	put("integer_bin", new IntegerAttribute(new double[] {0, 9}, 1.0, 2, -1));
	     	put("double_no_bin", new DoubleAttribute(new double[] {0.0, 1.0}));
	     	put("double_bin", new DoubleAttribute(new double[] {0.0, 9.0}, 1.0, 2, -1));
	     	put("nominal_no_bin", new NominalAttribute(new String[] {"0", "1"}));
	     	put("ordinal_no_bin", new OrdinalAttribute(new String[] {"0", "1"}));
	     	put("symetrical_no_bin", new SymmetricBinaryAttribute());
	    }};
	    
	    //load values
	    attributes.get("integer_no_bin").handle(0);  attributes.get("integer_no_bin").handle(1); 
	    attributes.get("integer_bin").handle(0);  attributes.get("integer_bin").handle(4); attributes.get("integer_bin").handle(7); 
	    attributes.get("double_no_bin").handle(0.0);  attributes.get("double_no_bin").handle(1.0); 
	    attributes.get("double_bin").handle(0.0);  attributes.get("double_bin").handle(4.0); attributes.get("double_bin").handle(7.0); 
	    attributes.get("nominal_no_bin").handle("0");  attributes.get("nominal_no_bin").handle("1"); 
	    attributes.get("ordinal_no_bin").handle("0");  attributes.get("ordinal_no_bin").handle("1"); 
	    attributes.get("symetrical_no_bin").handle(false);  attributes.get("symetrical_no_bin").handle(false); 
	    
	    AttributesAggregator aggregator = new AttributesAggregator();
	    aggregator.add(attributes);

		AssociationRuleWrapper actual, expected; 
		AssociationRuleConverter rulesConverter = new AssociationRuleConverter(aggregator);
				
		actual = rulesConverter.convert(new AssociationRule(new Itemset(new int[] {0}), new Itemset(new int[] {1})));
		expected = new AssociationRuleWrapper("integer_no_bin = 0 -> integer_no_bin = 1");
		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
		
		actual = rulesConverter.convert(new AssociationRule(new Itemset(new int[] {2}), new Itemset(new int[] {3})));
		expected = new AssociationRuleWrapper("integer_bin >= 0 ^ integer_bin <= 4 -> integer_bin = 7");
		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
		
		actual = rulesConverter.convert(new AssociationRule(new Itemset(new int[] {4}), new Itemset(new int[] {5})));
		expected = new AssociationRuleWrapper("double_no_bin = 0.0 -> double_no_bin = 1.0");
		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
		
		actual = rulesConverter.convert(new AssociationRule(new Itemset(new int[] {6}), new Itemset(new int[] {7})));
		expected = new AssociationRuleWrapper("double_bin >= 0.0 ^ double_bin <= 4.0 -> double_bin = 7.0");
		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
		
		actual = rulesConverter.convert(new AssociationRule(new Itemset(new int[] {8}), new Itemset(new int[] {9})));
		expected = new AssociationRuleWrapper("nominal_no_bin = \"0\" -> nominal_no_bin = \"1\"");
		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
		
		actual = rulesConverter.convert(new AssociationRule(new Itemset(new int[] {10}), new Itemset(new int[] {11})));
		expected = new AssociationRuleWrapper("ordinal_no_bin = \"0\" -> ordinal_no_bin = \"1\"");
		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
		
		actual = rulesConverter.convert(new AssociationRule(new Itemset(new int[] {12}), new Itemset(new int[] {13})));
		expected = new AssociationRuleWrapper("symetrical_no_bin = false -> symetrical_no_bin = true");
		Assert.assertEquals(actual, expected);
		Assert.assertTrue(actual.getJSONObject().similar(expected.getJSONObject()));
	}

}
