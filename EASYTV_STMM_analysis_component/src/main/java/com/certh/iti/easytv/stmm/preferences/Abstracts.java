package com.certh.iti.easytv.stmm.preferences;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Abstracts {
	
	/**
	 * @brief Find the profile that represents the cluster center.
	 * The center of the cluster is the profile that has the lowest mean distance 
	 * from all other cluster profiles.
	 * 
	 * @param cluster The cluster profiles
	 * @param resultProfile The center of the cluster
	 * @param distances a map of distance and point that represent the distance of the current profile from all others
	 * @param resultMeanDistance The center mean distance from all cluster profiles.
	 * @return The mean distance of the center profile.
	 */
	public static double FindCenter(HashSet<Profile> cluster, Profile resultProfile, TreeMap<Double, HashSet<Profile>> distances, Double resultMeanDistance) {
		Double curMeanDistance = null, curDistance;
		TreeMap<Double, HashSet<Profile>> curDistances;
		HashSet<Profile> curDistancesSub = null;
		
		Iterator<Profile> clusterIter1 = cluster.iterator();
		while(clusterIter1.hasNext()){
			curMeanDistance = 0.0;
			curDistances = new TreeMap<Double, HashSet<Profile>>();
			Profile candidateCenter = clusterIter1.next();
			
			Iterator<Profile> clusterIter2 = cluster.iterator();
			while(clusterIter2.hasNext() && !clusterIter1.equals(clusterIter2)){
				Profile other = clusterIter2.next();
				curDistance = candidateCenter.DistanceTo(other);
				
				if((curDistancesSub = curDistances.get(curDistance)) == null) {
                    curDistancesSub = new HashSet<Profile>();
                    curDistances.put(curDistance, curDistancesSub);
				}
				
                curDistancesSub.add(other);
                curMeanDistance += curDistance;
			}
			
			curMeanDistance /= cluster.size();
			if(curMeanDistance < resultMeanDistance) {
				resultProfile.copy(candidateCenter);
                resultMeanDistance = curMeanDistance;
                distances.putAll(curDistances);
			}
		}
		
		return curMeanDistance;
	}
	
	public static double FindCenter(HashSet<Profile> cluster, Profile resultProfile, TreeMap<Double, HashSet<Profile>> distances) {
		return FindCenter(cluster, resultProfile, distances, Double.MAX_VALUE);
	}
	
	/**
	 * 
	 * 
	 * @param center
	 * @param clusterDistances
	 * @param profiles
	 * @return
	 */
	public static Profile GeneralizeProfile(Profile center ,TreeMap<Double, HashSet<Profile>> clusterDistances, List<Profile> profiles) {
		Profile result = new Profile();
		Double centerUser, centerOs;
		centerUser = center.ContextEntries.get("context||user").get_Value();
		centerOs = center.ContextEntries.get("context||os").get_Value();
		
		Iterator<Profile> profilesIter = profiles.iterator();
		while(profilesIter.hasNext()) {
			Profile p = profilesIter.next();
			if(p.ContextEntries.get("context||user").get_Value() == centerUser && p.ContextEntries.get("context||os").get_Value() != centerOs) {
				Iterator<Entry<String, com.certh.iti.easytv.stmm.preferences.Entry>> preferenceIter = p.PreferenceEntries.entrySet().iterator();
				while(preferenceIter.hasNext()) {
					Entry<String, com.certh.iti.easytv.stmm.preferences.Entry> pair = preferenceIter.next();
					result.PreferenceEntries.put(pair.getKey(), pair.getValue());
				}
			}
		}
		
		Iterator<Entry<String, com.certh.iti.easytv.stmm.preferences.Entry>> preferenceIter = center.PreferenceEntries.entrySet().iterator();
		while(preferenceIter.hasNext()) {
			Entry<String, com.certh.iti.easytv.stmm.preferences.Entry> pair = preferenceIter.next();
			result.PreferenceEntries.put(pair.getKey(), pair.getValue());
		}
		
		//Enter missing stuff
		 HashMap<Double, HashSet<Profile>> allDistances = null;
		 HashSet<Profile> allDistancesSub = null;
		 Double curDistance;
		 
		 profilesIter = profiles.iterator();
		 while(profilesIter.hasNext()) {
			 Profile p = profilesIter.next();
			 curDistance = center.DistanceTo(p);
			 if((allDistancesSub = allDistances.get(curDistance)) == null) {
                 allDistancesSub = new HashSet<Profile>();
                 allDistances.put(curDistance, allDistancesSub);
			 }
			 allDistancesSub.add(p);
		 }
		 
     	Iterator<String> entryNamesIter = EntryManager.EntryNames().iterator();
        while(entryNamesIter.hasNext()){
			 boolean found = false;
			 String k = entryNamesIter.next();
			 
			 if(result.PreferenceEntries.containsKey(k)) 
				 continue;
			 
			 Iterator<Entry<Double, HashSet<Profile>>> clusterDistanceIter = clusterDistances.entrySet().iterator();
			 while(clusterDistanceIter.hasNext() && !found) {
				 Iterator<Profile> c = clusterDistanceIter.next().getValue().iterator();
				 while(c.hasNext()) {
					 Profile cp = c.next();
					 if(!cp.PreferenceEntries.containsKey(k)) continue;
					 result.PreferenceEntries.put(k, cp.PreferenceEntries.get(k));
					 found = true;
				 }
				 
				 if(found) continue;
				 
				 Iterator<Entry<Double, HashSet<Profile>>> c1 = allDistances.entrySet().iterator();
				 while(c1.hasNext()) {
					 Iterator<Profile> profileItero = clusterDistanceIter.next().getValue().iterator();
					 while(profileItero.hasNext()) {
						 Profile cp1 = profileItero.next();
						 if(!cp1.PreferenceEntries.containsKey(k)) continue;
						 result.PreferenceEntries.put(k, cp1.PreferenceEntries.get(k));
						 found = true;
					 }
					 if(found) break;
				 }
			 }
		 } 
		return result;	
	}
	
}