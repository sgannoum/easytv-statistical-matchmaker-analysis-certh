package com.certh.iti.easytv.stmm.clustering;

import java.util.Arrays;
import java.util.logging.Logger;

import org.apache.commons.math3.ml.clustering.Clusterer;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.MultiKMeansPlusPlusClusterer;

import com.certh.iti.easytv.stmm.similarity.DistanceMeasureFactory;
import com.certh.iti.easytv.user.Profile;

public class MultiKMeansPlusPlusWrapper implements iCluster {
	
	private final static Logger logger = Logger.getLogger(MultiKMeansPlusPlusWrapper.class.getName());

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
		
		logger.info(k_means.toString());	
		
		//Call clustering algorithm
		return k_means;
	}

	@Override
	public Clusterer<Profile> getClusterer() {
		return new MultiKMeansPlusPlusClusterer<Profile>(new KMeansPlusPlusClusterer<Profile>(k, maxIterations, DistanceMeasureFactory.getInstance(compareMode)), numTrials);
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
