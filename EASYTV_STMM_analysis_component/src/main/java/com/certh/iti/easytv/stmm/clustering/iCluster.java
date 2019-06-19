package com.certh.iti.easytv.stmm.clustering;

import org.apache.commons.math3.ml.clustering.Clusterer;

import com.certh.iti.easytv.user.UserProfile;

public interface iCluster {
	
	 public Clusterer<UserProfile> Clone();
	 
	 public String get_Name();

}
