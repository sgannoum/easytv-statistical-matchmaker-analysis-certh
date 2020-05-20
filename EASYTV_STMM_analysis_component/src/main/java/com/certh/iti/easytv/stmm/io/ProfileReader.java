package com.certh.iti.easytv.stmm.io;

import java.util.List;
import java.util.Map;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.json.JSONObject;

import com.certh.iti.easytv.user.Profile;

public interface ProfileReader {
	
	/**
	 * Get all the profiles from DB
	 * @return
	 */
	public Cluster<Profile> readProfiles();
	
	/**
	 * Get the history of interaction of the specific user as a set of profiles
	 * @param id
	 * @return
	 */
	public Cluster<Profile> readUserHisotryOfInteractionOfModel(int id, long timeInterval);
	
	
	/**
	 * Write profile modification suggestions
	 * @param id user profile id
	 * @param Suggestions profile modification suggestions
	 * @return
	 */
	public void writeUserModificationSuggestions(int id, JSONObject Suggestions);
	
	/**
	 * 
	 * @param params a map of modelId and suggestion to update
	 */
	void writeUserModificationSuggestions(Map<Integer, JSONObject> params);

	/**
	 * Clean up history of interaction
	 * @param id
	 * @return
	 */
	public void clearHisotryOfInteraction();
	
	/**
	 * Get all users ids
	 * @return
	 */
	public List<Integer> getModelsId();

}
