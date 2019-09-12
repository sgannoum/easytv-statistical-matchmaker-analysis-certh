package com.certh.iti.easytv.stmm.io;

import org.apache.commons.math3.ml.clustering.Cluster;

import com.certh.iti.easytv.user.UserProfile;

public interface ProfileReader {
	
	public Cluster<UserProfile> readProfiles();

}
