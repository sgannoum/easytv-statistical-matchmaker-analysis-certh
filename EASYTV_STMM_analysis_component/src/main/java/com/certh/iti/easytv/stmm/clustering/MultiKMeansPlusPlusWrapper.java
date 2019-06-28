package com.certh.iti.easytv.stmm.clustering;

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
	public Clusterer<UserProfile> Clone() {
		System.out.print("Initiating Apache KMeans++ with values k= "+k+" maxIterations= "+maxIterations+" numTrials= "+numTrials+" compareMode= [");
		for(int i = 0; i < compareMode.length - 1; i++)
			System.out.print("\""+compareMode[i]+"\",");
		
		System.out.print("\""+compareMode[compareMode.length - 1] + "\"]");
		
		//Call clustering algorithm
		return new MultiKMeansPlusPlusClusterer<UserProfile>(new KMeansPlusPlusClusterer<UserProfile>(k, maxIterations, DistanceMeasureFactory.getInstance(compareMode)), numTrials);
	}

	@Override
	public String get_Name() {
		return "MultiKMeans++";
	}

}
