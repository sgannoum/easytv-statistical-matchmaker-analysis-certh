package com.certh.iti.easytv.stmm.clustering;

import java.util.HashSet;
import java.util.List;

import com.certh.iti.easytv.stmm.preferences.Profile;

public interface iClustering {
	
 public void Run(List<Profile> profiles, List<HashSet<Profile>> clusters, HashSet<Profile> noise);
 
 public iClustering Clone();
 
 public String get_Name();
 
}
