package com.certh.iti.easytv.stmm.clustering;

import java.util.Arrays;

import org.apache.commons.math3.ml.clustering.Clusterer;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;

import com.certh.iti.easytv.stmm.similarity.DistanceMeasureFactory;
import com.certh.iti.easytv.user.UserProfile;

public class KMeansPlusPlusWrapper implements iCluster {
	
	private int k;
	private int maxIterations;
	private String[] compareMode;
	private String distanceMeasure;	

	
	public KMeansPlusPlusWrapper() {
	}

	@Override
	public String get_Name() {
		return "KMeans++";
	}

	@Override
	public iCluster Clone() {
		KMeansPlusPlusWrapper k_means = new KMeansPlusPlusWrapper();
		k_means.k = k;
		k_means.maxIterations = maxIterations;
		k_means.distanceMeasure = distanceMeasure;
		k_means.compareMode = Arrays.copyOf(compareMode, compareMode.length);
				
		System.out.print(k_means.toString());
		
		return k_means;
	}

	@Override
	public Clusterer<UserProfile> getClusterer() {
		return new KMeansPlusPlusClusterer<UserProfile>(k, maxIterations, DistanceMeasureFactory.getInstance(compareMode));
	}

	@Override
	public String toString() {
		
		String str = new String(); 

		str += "Initiating Apache KMeans++ with values k="+k+" maxIterations="+maxIterations+" compareMode=[";
		for(int i = 0; i < compareMode.length - 1; i++)
			str += "\""+compareMode[i]+"\",";
		
		str += "\""+compareMode[compareMode.length - 1] + "\"]";
		
		return str;
		
	}
}
