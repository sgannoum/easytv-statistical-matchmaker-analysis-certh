package com.certh.iti.easytv.stmm.generator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.certh.iti.easytv.stmm.user.profile.UserProfile;

public class UserProfileGeneratorTest {
	
	@Test
	public void test_created_profiles() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		List<UserProfile> actualProfiles;
		UserProfileGenerator userProfileGenerator = new UserProfileGenerator();
		
/*		//set ranges
		userProfileGenerator.setAgeRange(new int[] {20, 30});
		//userProfileGenerator.setGenderRange(new int[] {20, 30});
		
		userProfileGenerator.setVisualAcuityRange(new int[] {20, 30});
		userProfileGenerator.setContrastSensitivityRange(new int[] {20, 30});
		userProfileGenerator.setColorBlindnessRange(new int[] {20, 30});
		
		userProfileGenerator.setQuarterKRange(new int[] {20, 30});
		userProfileGenerator.setHalfKRange(new int[] {20, 30});
		userProfileGenerator.setOneKRange(new int[] {20, 30});
		userProfileGenerator.setTwoKRange(new int[] {20, 30});
		userProfileGenerator.setFourKRange(new int[] {20, 30});
		userProfileGenerator.setEightKRange(new int[] {20, 30});*/
		
		actualProfiles = userProfileGenerator.generate(1500);
		
		// Check that user profile are correctly generated 
		// Covert generated profile into JSON
		// Construct a user profile from JSON
		for(int i = 0; i < actualProfiles.size(); i++) {
			UserProfile expectedProfile = actualProfiles.get(i);
			UserProfile actualProfile = new UserProfile(expectedProfile.getJSONObject());

			//System.out.println(actualProfile.getJSONObject().toString(4));
			Assert.assertEquals(actualProfile, expectedProfile);
			
			File file = new File("C:\\Users\\salgan\\git\\EASYTV_STMM_analysis_component\\EASYTV_STMM_analysis_component\\auto_generated_testing_profiles"+File.separator+"user_"+i+".json");
			if(!file.exists())
				Assert.assertTrue(file.createNewFile());
			
			PrintWriter writer = new PrintWriter(file);
			writer.write(actualProfile.getJSONObject().toString(4));
			writer.close();
		}	
	}
	
	
	//@Test
	public void test_apache_DBScan() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		DBSCANClusterer<UserProfile> dbscan = new DBSCANClusterer<UserProfile>(150, 4);
		HashSet<HashSet<Clusterable> > expectedClustersSet = new HashSet<HashSet<Clusterable>>();
		
		UserProfileGenerator userProfileGenerator = new UserProfileGenerator();
		List<UserProfile> actualProfiles = userProfileGenerator.generate(1250);
		
		List<Cluster<UserProfile>> actualClusters = dbscan.cluster(actualProfiles);
		System.out.println("clusters: "+actualClusters.size());
		for(int i = 0 ; i < actualClusters.size(); i++) {
			List<UserProfile> cluster = actualClusters.get(i).getPoints();
			System.out.println(i+": "+cluster.size());
/*			for(int j = 0; j < cluster.size(); j++) {
				System.out.println(cluster.get(j).getJSONObject().toString(4));
			}
*/
		}
		
	}

}
