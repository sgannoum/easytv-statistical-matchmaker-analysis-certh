package com.certh.iti.easytv.stmm.clustering;

import org.apache.commons.math3.ml.clustering.Clusterer;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;

import com.certh.iti.easytv.stmm.user.preference.StatisticalPreference;
import com.certh.iti.easytv.user.UserProfile;
import com.certh.iti.easytv.user.preference.Preference;

public class DBScanWrapper implements iCluster{
	
	private double eps = 155;
	private int minPts = 4;
	private String[] compareMode;
	private String distanceMeasure;
	
	
	public DBScanWrapper() {
	}
	
	public  Clusterer<UserProfile> Clone() {
		
		System.out.print("Initiating Apache DBScan with values eps= "+eps+" minPts= "+minPts+" compareMode= ");
		for(int i = 0; i < compareMode.length - 1; i++)
			System.out.print(compareMode[i]+",");
		System.out.print(compareMode[compareMode.length - 1]);
		System.out.println(" distanceMeasure= "+distanceMeasure);
				
		//Filter out user profile dimensions
		AllDimensionsDistanceMeasure allDimensions = new AllDimensionsDistanceMeasure(StatisticalPreference.getOperands());
		
		//Call clustering algorithm
		return new DBSCANClusterer<UserProfile>(eps, minPts, allDimensions.getDistanceMeasure());
	}

	public String get_Name() {
		return "DBScan";
	}

}
