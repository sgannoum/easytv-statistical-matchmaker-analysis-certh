package com.certh.iti.easytv.stmm.clustering;

import org.apache.commons.math3.ml.clustering.Clusterer;

import com.certh.iti.easytv.user.UserProfile;

public interface iCluster {
	
	/**
	 * @return A clone with the same initialization values
	 */
	 public iCluster Clone();
	 
	 /**
	  * 
	  * @return The clustering algorithm name
	  */
	 public String get_Name();
	 
	 /**
	  * 
	  * @return An instance of the clustering algorithm
	  */
	 public Clusterer<UserProfile> getClusterer();

}
