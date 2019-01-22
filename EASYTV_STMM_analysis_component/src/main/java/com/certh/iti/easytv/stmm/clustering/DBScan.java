package com.certh.iti.easytv.stmm.clustering;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.certh.iti.easytv.stmm.preferences.Profile;


public class DBScan implements iClustering {
	
	public double minRegion = 3;
	public double maxDistance = 3;
	
	private List<Profile> _Profiles;
	private List<HashSet<Profile>> _Clusters;
	private HashSet<Profile> _Noise;

	public DBScan() {
	}

	public DBScan(double minRegion, double maxDistance) {
		this.minRegion = minRegion;
		this.maxDistance = maxDistance;
	}

	/*
	 * (non-Javadoc)
	 * @see com.certh.iti.easytv.stmm.clustering.iClustering#Run(java.util.List, java.util.List, java.util.HashSet)
	 */
	public void Run(List<Profile> profiles, List<HashSet<Profile>> clusters, HashSet<Profile> noise) {
		_Profiles = profiles;
        _Clusters = new ArrayList<HashSet<Profile>>();
        _Noise = new HashSet<Profile>();
        
        Iterator<Profile> profileIter = _Profiles.iterator();
        while(profileIter.hasNext()) {
        	Profile profile = profileIter.next();
        	HashSet<Profile> cluster = new HashSet<Profile>();
        	ExpandCluster(profile, cluster);
        	if(cluster.size() > 0) _Clusters.add(cluster);
        }
        
        profileIter = _Profiles.iterator();
        while(profileIter.hasNext()) {
        	Profile profile = profileIter.next();
        	if(IsPartOfCluster(profile)) _Noise.add(profile);
        }

        clusters.addAll(_Clusters);
        noise.addAll(_Noise);
	}

	/*
	 * (non-Javadoc)
	 * @see com.certh.iti.easytv.stmm.clustering.iClustering#Clone()
	 */
	public iClustering Clone() {
		return new DBScan(minRegion, maxDistance);
	}

	/*
	 * (non-Javadoc)
	 * @see com.certh.iti.easytv.stmm.clustering.iClustering#get_Name()
	 */
	public String get_Name() {
		return "DBScan";
	}
	
	/**
	 * Find all the user profiles that belongs to the profile region. 
	 * All profiles that belongs to the region has a distance smaller or equals to maxDistance from the given profile.
	 * 
	 * @param cur
	 * @return all profiles that belongs to the region
	 */
	private HashSet<Profile> Region(Profile cur){
		HashSet<Profile> result = new HashSet<Profile>();
		
		Iterator<Profile> profilesIter = _Profiles.iterator();
		while(profilesIter.hasNext()) {
			Profile profile = profilesIter.next();
			if(profile.equals(cur)) continue;
			if( cur.DistanceTo(profile) <= maxDistance) 
				result.add(profile); 
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param cur
	 * @param cluster
	 */
	private void ExpandCluster(Profile cur, HashSet<Profile> cluster) {
		if(IsPartOfCluster(cur)) return;
		
		HashSet<Profile> regionPoints = Region(cur);
		if(regionPoints.size() >= minRegion) {
			Iterator<Profile> regionItero = regionPoints.iterator();
			while(regionItero.hasNext()) {
				Profile r = regionItero.next();
				if(!cluster.contains(r)) {
                    cluster.add(r);
                    ExpandCluster(r, cluster);
				}
			}
		}
	}
	
	/**
	 * Check whether the profile is part of a cluster
	 * 
	 * @param cur a user profile
	 * @return True in case the profile belongs to a cluster, false otherwise.
	 */
	private boolean IsPartOfCluster(Profile cur) {
		Iterator<HashSet<Profile>> clustersIter = _Clusters.iterator();
		while(clustersIter.hasNext()) {
			if(clustersIter.next().contains(cur)) 
				return true;
		}
		return false;
	}

}
