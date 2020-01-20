package com.certh.iti.easytv.stmm.association.analysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;

import com.certh.iti.easytv.stmm.Main;
import com.certh.iti.easytv.stmm.association.analysis.fpgrowth.FPGrowthWrapper;
import com.certh.iti.easytv.stmm.association.analysis.fpgrowth.Itemset;
import com.certh.iti.easytv.stmm.association.analysis.rules.AssociationRule;
import com.certh.iti.easytv.stmm.association.analysis.rules.AssociationRuleConverter;
import com.certh.iti.easytv.stmm.association.analysis.rules.AssociationRuleGenerator;
import com.certh.iti.easytv.stmm.association.analysis.rules.AssociationRuleWrapper;
import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.preference.attributes.Attribute.Bin;

public class RuleRefiner {
	
	private final static Logger logger = Logger.getLogger(RuleRefiner.class.getName());

	
	private AssociationAnalyzer fpgrowth;
	private AssociationRuleGenerator ruleGenerator;
	private AssociationRuleConverter rulesConverter;
	
	private Vector<Itemset> frequentItemset;
	private Vector<AssociationRule> associationRules;
	private Vector<AssociationRuleWrapper> associationRulesWrapper;

	private double minSupport = 0, minConfidence = 0;

	
	public RuleRefiner(List<Profile> profiles, Vector<Bin> bins) {
		fpgrowth = new FPGrowthWrapper(profiles, bins);
		ruleGenerator = new AssociationRuleGenerator(fpgrowth.getItemsets());
		rulesConverter = new AssociationRuleConverter(bins);
	}
	
	public double getMinSupport() {
		return minSupport;
	}
	
	public double getMinConfidence() {
		return minConfidence;
	}

	public Vector<Itemset> getFrequentItemsete() {
		return frequentItemset;
	}
	
	public Vector<AssociationRule> getAssociationRules() {
		return associationRules;
	}
	
	public Vector<AssociationRuleWrapper> generatedRules(double minSupport, double minConfidence) {
		
		this.minSupport = minSupport;
		this.minConfidence = minConfidence;
		
		logger.info(String.format("Generate rules with  Minimume support: %.1f, Minimume confidence: %.1f", minSupport, minConfidence));
		
		//Generate frequent itemsets
		frequentItemset = fpgrowth.getFrequentItemsets(minSupport);
		
		logger.info(String.format("Found %d frequent itemsets with minSupport:%f", frequentItemset.size(), minSupport));

		//Generate association rules
		associationRules = ruleGenerator.findAssociationRules(frequentItemset, minConfidence);
		
		logger.info(String.format("Found %d rules with minConfidence:%f", associationRules.size(), minConfidence));

		//Convert rules
		return rulesConverter.getRules(associationRules);
	}	
	
	private String postRBMMRules(String url) {
		
		logger.info("Post newly generated rules to " + url);
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		StringBuffer result = new StringBuffer();
		
		EntityBuilder entity = EntityBuilder.create();
		JSONArray rules = new JSONArray();
		Vector<AssociationRuleWrapper> associationRuleWrappers = rulesConverter.getRules(associationRules);
		for(AssociationRuleWrapper associationRuleWrapper : associationRuleWrappers) 
			rules.put(associationRuleWrapper.getJSONObject());
						
		entity.setContentType(ContentType.APPLICATION_JSON);
		entity.setText(rules.toString());
		
		//set http post content
		post.setEntity(entity.build());
		
		try {
				
			HttpResponse response = client.execute(post);
			logger.info("Response Code : "+ response.getStatusLine().getStatusCode());
	
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
			while ((line = rd.readLine()) != null) 
				result.append(line);
			
			rd.close();
			
		} 
		catch (UnsupportedEncodingException e) { e.printStackTrace();} 
		catch (UnsupportedOperationException e) { e.printStackTrace();} 
		catch (IOException e) { e.printStackTrace();}
		
		return result.toString();
	}
	
	private String getRBMMRules(String url) {
		
		logger.info("Get RBMM rules from " + url);
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(url);
		StringBuffer result = new StringBuffer();
		
		try {
				
			HttpResponse response = client.execute(get);
			logger.info("Response Code : "+ response.getStatusLine().getStatusCode());
	
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
			while ((line = rd.readLine()) != null) 
				result.append(line);
			
			rd.close();
			
		} 
		catch (UnsupportedEncodingException e) { e.printStackTrace();} 
		catch (UnsupportedOperationException e) { e.printStackTrace();} 
		catch (IOException e) { e.printStackTrace();}
		
		return result.toString();
	}

}