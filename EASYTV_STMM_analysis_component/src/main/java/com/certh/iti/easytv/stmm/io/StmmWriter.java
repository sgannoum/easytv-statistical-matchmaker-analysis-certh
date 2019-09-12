package com.certh.iti.easytv.stmm.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;

import com.certh.iti.easytv.user.UserProfile;

public class StmmWriter implements ProfileWriter{
	
	private List<UserProfile> clusters;
	private String url;
	
	public StmmWriter(String url, List<UserProfile> clusters) throws IOException {
		this.clusters = clusters;
		this.url = url;
	}

	@Override
	public void write() {
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);

		// add header
	    //post.setHeader("Content-Type", "application/json");

		//set http post content
		post.setEntity(CreateHttpEntity());
		
		try {
				
			HttpResponse response = client.execute(post);
			System.out.println("Response Code : " 
		                + response.getStatusLine().getStatusCode());
	
			BufferedReader rd = new BufferedReader(
			        new InputStreamReader(response.getEntity().getContent()));
	
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private HttpEntity CreateHttpEntity() {
		EntityBuilder entity = EntityBuilder.create();
		
		JSONArray profiles = new JSONArray();
		Iterator<UserProfile> iter = clusters.iterator();
		while(iter.hasNext()) {
			profiles.put(iter.next().getJSONObject());
		}
		
		String content = profiles.toString();
				
		entity.setContentType(ContentType.APPLICATION_JSON);
		entity.setText(content);
		
		return entity.build();
	}

}
