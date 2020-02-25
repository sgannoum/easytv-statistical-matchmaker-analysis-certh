package com.certh.iti.easytv.stmm.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;

import com.certh.iti.easytv.stmm.association.analysis.rules.RbmmRuleWrapper;
import com.certh.iti.easytv.stmm.association.analysis.rules.RuleWrapper;
import com.certh.iti.easytv.user.Profile;

public class HttpHandler implements ProfileWriter {
	
	private final static Logger logger = Logger.getLogger(HttpHandler.class.getName());

	private String url;
	public HttpHandler(String url) throws IOException {
		this.url = url;
	}

	@Override
	public void write(List<Profile> clusters) {
		
		logger.info("Post newly generated profiles to " + url);
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);


		//Entity body
		EntityBuilder entity = EntityBuilder.create();
		JSONArray profiles = new JSONArray();
		Iterator<Profile> iter = clusters.iterator();
		while(iter.hasNext()) 
			profiles.put(iter.next().getJSONObject());
		
		entity.setContentType(ContentType.APPLICATION_JSON);
		entity.setText(profiles.toString());
				
		//set http post content
		post.setEntity(entity.build());
		
		try {
				
			HttpResponse response = client.execute(post);
			logger.info("Response Code : " 
		                + response.getStatusLine().getStatusCode());
	
			BufferedReader rd = new BufferedReader(
			        new InputStreamReader(response.getEntity().getContent()));
	
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		
			rd.close();
		} 
		catch (UnsupportedEncodingException e) { e.printStackTrace();} 
		catch (UnsupportedOperationException e) { e.printStackTrace();} 
		catch (IOException e) { e.printStackTrace();}
	}

	public static void writeRules(String url, Collection<RuleWrapper> associationRuleWrappers) {
		logger.info("Post newly generated rules to " + url);
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		StringBuffer result = new StringBuffer();
		
		EntityBuilder entity = EntityBuilder.create();
		JSONArray rules = new JSONArray();
		for(RuleWrapper associationRuleWrapper : associationRuleWrappers) 
			rules.put(associationRuleWrapper.getJSONObject());
						
		entity.setContentType(ContentType.APPLICATION_JSON);
		entity.setText(rules.toString());
		
		//set http post content
		post.setEntity(entity.build());
		
		try {
				
			HttpResponse response = client.execute(post);

			if(response.getStatusLine().getStatusCode() != 200) {
				logger.info("Response Code : "+ response.getStatusLine().getStatusCode()+" failed to get response.");
				return ; 
			}
			
			logger.info("Response Code : "+ response.getStatusLine().getStatusCode());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
			while ((line = rd.readLine()) != null) 
				result.append(line);
			
			rd.close();
		} 
		catch (UnsupportedEncodingException e) { logger.log(Level.SEVERE, e.getMessage());} 
		catch (UnsupportedOperationException e) { logger.log(Level.SEVERE, e.getMessage());} 
		catch (IOException e) { logger.log(Level.SEVERE, e.getMessage());}
	}

	public static Vector<RbmmRuleWrapper> readRules(String url) {
		logger.info("Get RBMM rules from " + url);
		Vector<RbmmRuleWrapper> AssociationRuleWrapper = new Vector<RbmmRuleWrapper>();
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(url);
		StringBuffer result = new StringBuffer();
		
		try {
				
			HttpResponse response = client.execute(get);
			
			if(response.getStatusLine().getStatusCode() != 200) {
				logger.info("Response Code : "+ response.getStatusLine().getStatusCode()+" failed to get response.");
				return AssociationRuleWrapper; 
			}
			
			logger.info("Response Code : "+ response.getStatusLine().getStatusCode());
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
			while ((line = rd.readLine()) != null) 
				result.append(line);
			
			rd.close();
		} 
		catch (UnsupportedEncodingException e) { logger.log(Level.SEVERE, e.getMessage());} 
		catch (UnsupportedOperationException e) { logger.log(Level.SEVERE, e.getMessage());} 
		catch (IOException e) { logger.log(Level.SEVERE, e.getMessage());}
		
		
		JSONArray rules = new JSONArray(result.toString());
		for(int i = 0; i < rules.length(); i++)
			AssociationRuleWrapper.add(new RbmmRuleWrapper(rules.getJSONObject(i)));
		
		return AssociationRuleWrapper;
	}

}
