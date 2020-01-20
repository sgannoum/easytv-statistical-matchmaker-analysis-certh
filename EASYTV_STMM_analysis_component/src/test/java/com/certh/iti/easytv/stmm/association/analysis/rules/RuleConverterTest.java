package com.certh.iti.easytv.stmm.association.analysis.rules;

import java.util.Vector;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.certh.iti.easytv.stmm.association.analysis.fpgrowth.Itemset;
import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.preference.attributes.Attribute.Bin;

public class RuleConverterTest {
	
	private Vector<Bin> bins;
	private AssociationRuleConverter rulesConverter;
	
	@BeforeTest
	public void beforeTest() {
		bins = Profile.getBins();
		rulesConverter = new AssociationRuleConverter(bins);
	}

	@Test
	public void test1() {
		Vector<AssociationRule> associationRule = new Vector<AssociationRule>();
		associationRule.add(new AssociationRule(new Itemset(new int[] {100,200,300}), new Itemset(new int[] {55})));	
		
		System.out.println(rulesConverter.getRules(associationRule).get(0));
	}

}
