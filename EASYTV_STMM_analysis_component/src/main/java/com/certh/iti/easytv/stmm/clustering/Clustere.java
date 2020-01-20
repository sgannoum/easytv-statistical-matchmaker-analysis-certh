package com.certh.iti.easytv.stmm.clustering;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import com.certh.iti.easytv.stmm.similarity.DistanceMeasureFactory;
import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

public class Clustere {
	
	private final static Logger logger = Logger.getLogger(Clustere.class.getName());

	private DistanceMeasure allDimensionsDistance;
	private List<iCluster> clusterer;
	
	public Clustere(List<iCluster> clusterer) {
        this.allDimensionsDistance = DistanceMeasureFactory.getInstance();
		this.clusterer = clusterer;
	}
	
	
	/**
	 * Cluster profiles and substituted each cluster with its center.
	 *  
	 * @param profiles
	 * @return
	 * @throws UserProfileParsingException
	 */
	public List<Profile> getGeneralizedClusters(List<Cluster<Profile>> profiles) throws UserProfileParsingException {
		
        //clusters
        List<Profile> generalized = new ArrayList<Profile>();
        List<Cluster<Profile>> foundedClusters = new ArrayList<Cluster<Profile>>();
    	List<Cluster<Profile>> tmp = new ArrayList<Cluster<Profile>>();
		
        //start with all profile as first cluster
        foundedClusters.addAll(profiles);
     
        //run all clustering algorithms
        for(iCluster clusterer : clusterer) {

        	logger.info("["+clusterer.get_Name()+"] "+ clusterer.toString());
        	
        	//cluster each cluster
        	for(Cluster<Profile> cluster : foundedClusters) 
        		tmp.addAll(clusterer.getClusterer().cluster(cluster.getPoints()));
        			
            logger.info("Clusters generated... " + tmp.size());
            for(int i = 0; i < tmp.size(); i++)
            	logger.info("cluster_" + (i + 1) + " : "+ tmp.get(i).getPoints().size());

        	foundedClusters.clear();
        	foundedClusters.addAll(tmp);
        	tmp.clear();
        }

        //When no clusters produced, don't proceed
        if(foundedClusters.isEmpty()) return generalized;
        
        //Generalize clusters
        for(Cluster<Profile> aCluster : foundedClusters) {
        	
        	//the center profile
        	Profile clusterCenter = new Profile();
        	
        	//Find the cluster center
        	Clustere.FindCenter(allDimensionsDistance, aCluster, clusterCenter);
        	
        	generalized.add(clusterCenter);
        }
        
        return generalized;
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

}
