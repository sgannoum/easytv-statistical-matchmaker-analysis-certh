package com.certh.iti.easytv.stmm.io;

import org.apache.commons.math3.ml.clustering.Cluster;

import com.certh.iti.easytv.user.Profile;

public interface ProfileReader {
	
	public Cluster<Profile> readProfiles();

}
