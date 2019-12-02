package com.certh.iti.easytv.stmm.clustering;

import java.util.Arrays;
import java.util.logging.Logger;

import org.apache.commons.math3.ml.clustering.Clusterer;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import com.certh.iti.easytv.stmm.similarity.DistanceMeasureFactory;
import com.certh.iti.easytv.user.Profile;

public class DBScanWrapper implements iCluster{
	
	private final static Logger logger = Logger.getLogger(DBScanWrapper.class.getName());
	
	private double eps = 155;
	private int minPts = 4;
	private String[] compareMode;
	
	private DistanceMeasure dist;
	
	public DBScanWrapper() {
	}
	
	@Override
	public String get_Name() {
		return "DBScan";
	}

	@Override
	public iCluster Clone() {
		
		DBScanWrapper dbscan = new DBScanWrapper();
		dbscan.eps = eps;
		dbscan.minPts = minPts;
		dbscan.compareMode = Arrays.copyOf(compareMode, compareMode.length);
		
		logger.info(dbscan.toString());
		
		dbscan.dist = DistanceMeasureFactory.getInstance(compareMode);
		
		//Call clustering algorithm
		return dbscan;
	}

	@Override
	public Clusterer<Profile> getClusterer() {
		
		//initialize an instance
		return new DBSCANClusterer<Profile>(eps, minPts, dist);
	}
	
	@Override
	public String toString() {
		String str = new String(); 
		
		str += "Initiating Apache DBScan with values eps="+eps+" minPts="+minPts+" compareMode=[";
		for(int i = 0; i < compareMode.length - 1; i++)
			str +="\""+compareMode[i]+"\",";
		
		str += "\""+compareMode[compareMode.length - 1] + "\"]";
		
		return str;
		
	}

}
