package com.certh.iti.easytv.stmm.clustering;

import java.util.Arrays;

import org.apache.commons.math3.ml.clustering.Clusterer;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.MultiKMeansPlusPlusClusterer;

import com.certh.iti.easytv.stmm.similarity.DistanceMeasureFactory;
import com.certh.iti.easytv.user.UserProfile;

public class MultiKMeansPlusPlusWrapper implements iCluster {
	
	private int k;
	private int maxIterations;
	private int numTrials;
	private String[] compareMode;
	private String distanceMeasure;	

	
	public MultiKMeansPlusPlusWrapper() {
	}

	@Override
	public String get_Name() {
		return "MultiKMeans++";
	}

	@Override
	public iCluster Clone() {
		MultiKMeansPlusPlusWrapper k_means = new MultiKMeansPlusPlusWrapper();
		k_means.k = k;
		k_means.maxIterations = maxIterations;
		k_means.numTrials = numTrials;
		k_means.distanceMeasure = distanceMeasure;
		k_means.compareMode = Arrays.copyOf(compareMode, compareMode.length);
		
		System.out.print(k_means.toString());	
		
		//Call clustering algorithm
		return k_means;
	}

	@Override
	public Clusterer<UserProfile> getClusterer() {
		return new MultiKMeansPlusPlusClusterer<UserProfile>(new KMeansPlusPlusClusterer<UserProfile>(k, maxIterations, DistanceMeasureFactory.getInstance(compareMode)), numTrials);
	}
	
	@Override
	public String toString() {
		
		String str = new String(); 

		str += "Initiating Apache MultipleKMeans++ with values k="+k+" maxIterations="+maxIterations+" numTrials="+numTrials+" compareMode= [";
		for(int i = 0; i < compareMode.length - 1; i++)
			str += "\""+compareMode[i]+"\",";
		
		str += "\""+compareMode[compareMode.length - 1] + "\"]";
		
		return str;
		
	}

}
