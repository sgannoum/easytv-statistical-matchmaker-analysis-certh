package com.certh.iti.easytv.stmm.io;

import java.util.List;

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
	public Cluster<Profile> readUserHisotryOfInteraction(int id, long timeInterval);
	
	
	/**
	 * Write profile modification suggestions
	 * @param id user profile id
	 * @param Suggestions profile modification suggestions
	 * @return
	 */
	public void writeUserModificationSuggestions(int id, JSONObject Suggestions);
	
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
	public List<Integer> getUsersIds();

}
