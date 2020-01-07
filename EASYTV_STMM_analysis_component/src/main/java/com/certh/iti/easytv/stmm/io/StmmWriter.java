package com.certh.iti.easytv.stmm.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;

import com.certh.iti.easytv.stmm.association.analysis.RuleRefiner;
import com.certh.iti.easytv.user.Profile;

public class StmmWriter implements ProfileWriter{
	
	private final static Logger logger = Logger.getLogger(StmmWriter.class.getName());

	
	private List<Profile> clusters;
	private String url;
	
	public StmmWriter(String url, List<Profile> clusters) throws IOException {
		this.clusters = clusters;
		this.url = url;
	}

	@Override
	public void write() {
		
		logger.info("Post newly generated profiles to " + url);
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);

		// add header
	    //post.setHeader("Content-Type", "application/json");

		//set http post content
		post.setEntity(CreateHttpEntity());
		
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
	
	private HttpEntity CreateHttpEntity() {
		EntityBuilder entity = EntityBuilder.create();
		
		JSONArray profiles = new JSONArray();
		Iterator<Profile> iter = clusters.iterator();
		while(iter.hasNext()) {
			profiles.put(iter.next().getJSONObject());
		}
		
		String content = profiles.toString();
				
		entity.setContentType(ContentType.APPLICATION_JSON);
		entity.setText(content);
		
		return entity.build();
	}

}
