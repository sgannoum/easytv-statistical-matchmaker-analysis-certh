package com.certh.iti.easytv.stmm.preferences;

import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

public class Abstracts {
	
	/**
	 * @brief Find the profile that represents the cluster center.
	 * The center of the cluster is the profile that has the lowest mean distance 
	 * from all other cluster's profiles.
	 * 
	 * @param cluster The cluster profiles
	 * @param resultProfile The center of the cluster
	 * @param distances a map of distance and point that represent the distance of the current profile from all others
	 * @param resultMeanDistance The center mean distance from all cluster profiles.
	 * @return The mean distance of the center profile.
	 * @throws UserProfileParsingException 
	 */
	public static double FindCenter(DistanceMeasure distFunction, Cluster<Profile> cluster, Profile resultProfile, TreeMap<Double, HashSet<Profile>> distances, Double resultMeanDistance) throws UserProfileParsingException {
		Double curMeanDist = null, distance;
		TreeMap<Double, HashSet<Profile>> curDistances;
		HashSet<Profile> curDistancesSub = null;
		int clusterSize = cluster.getPoints().size();
		
		//compare each cluster with all others
		for(Profile candidateCenter : cluster.getPoints()) {
			
			curMeanDist = 0.0;
			curDistances = new TreeMap<Double, HashSet<Profile>>();
			
			for(Profile other : cluster.getPoints()) {
				
				//Don't compare with itself
				if(candidateCenter.equals(other)) 
					continue;

				//compute distance
				distance = distFunction.compute(candidateCenter.getPoint(), other.getPoint());				
				if((curDistancesSub = curDistances.get(distance)) == null) {
                    curDistancesSub = new HashSet<Profile>();
                    curDistances.put(distance, curDistancesSub);
				}
				
                curDistancesSub.add(other);
                curMeanDist += distance;
			}
			
			curMeanDist /= clusterSize;
			if(curMeanDist < resultMeanDistance) {
				resultProfile.setJSONObject(candidateCenter.getJSONObject());

                resultMeanDistance = curMeanDist;
                distances.putAll(curDistances);
			}
		}

		return curMeanDist;
	}
	
	public static double FindCenter(DistanceMeasure distFunction, Cluster<Profile> cluster, Profile resultProfile, TreeMap<Double, HashSet<Profile>> distances) throws UserProfileParsingException {
		return FindCenter(distFunction, cluster, resultProfile, distances, Double.MAX_VALUE);
	}
	
	
	/**
	 * @brief Find the profile that represents the cluster center.
	 * The center of the cluster is the profile that has the lowest mean distance 
	 * from all other cluster's profiles.
	 * 
	 * @param cluster The cluster profiles
	 * @param centerProfile The center of the cluster
	 * @param distances a map of distance and point that represent the distance of the current profile from all others
	 * @param resultMeanDistance The center mean distance from all cluster profiles.
	 * @return The mean distance of the center profile.
	 * @throws UserProfileParsingException 
	 */
	public static double FindCenter(DistanceMeasure distFunction, Cluster<Profile> cluster, Profile centerProfile) throws UserProfileParsingException {
		double meanDist = Double.MAX_VALUE;
		List<Profile> profiles = cluster.getPoints();
		//double[][] distances = new double[profiles.size()][profiles.size()];
		double[] weights = new double[profiles.size()];

		
		//calculate profiles distance
		for(int i = 0; i < profiles.size(); i++) {
			for(int j = i; j <  profiles.size(); j++) {
				Profile profile1 = profiles.get(i);
				Profile profile2 = profiles.get(j);
				double weight = distFunction.compute(profile1.getPoint(), profile2.getPoint());
				//distances[i][j] = distFunction.compute(profile1.getPoint(), profile2.getPoint());	
				//distances[j][i] = distances[i][j];
				
				weights[i] += weight;
				weights[j] += weight;
			}
		}
		
		
		//select the profile with minimum distance
		Profile selectedProfile = null;
		for(int i = 0; i < profiles.size(); i++) 
			if(meanDist > weights[i]) { 
				meanDist = weights[i];
				selectedProfile = profiles.get(i);
			}
		
		//initialize center
		centerProfile.setJSONObject(selectedProfile.getJSONObject());

		return meanDist / profiles.size();
	}
	
	/**
	 * 
	 * 
	 * @param center
	 * @param clusterDistances
	 * @param profiles
	 * @return
	 */
	
/*	public static Profile GeneralizeProfile(Profile center ,TreeMap<Double, HashSet<Profile>> clusterDistances, List<Profile> profiles) {
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
	}*/
	
}
