package com.certh.iti.easytv.stmm.clustering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.certh.iti.easytv.stmm.similarity.DimensionsGenerator;
import com.certh.iti.easytv.stmm.similarity.DissimilarityMeasure;
import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

public class DBScanProfilesTest {
	
	private DimensionsGenerator dimensionsGenerator;
	private DistanceMeasure distanceMeasure;
	
	@BeforeClass
	public void beforeTest() {
		dimensionsGenerator = new DimensionsGenerator(Profile.getAggregator().getAttributes());
		distanceMeasure = new DissimilarityMeasure(dimensionsGenerator.getLables(), dimensionsGenerator.getDimensions());
	}

	@Test
	public void test_cluster() throws IOException, UserProfileParsingException {
		
		Profile profile_1 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 1," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 5," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 1.2," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"normal\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"20\"" + 
				"    }}}}" + 
				"}"));
		
		Profile profile_11 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 11," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 3," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 2.2," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"normal\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"20\"" + 
				"    }}}}" + 
				"}"));
		
		Profile profile_12 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 12," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 7," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 1.0," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"normal\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"20\"" + 
				"    }}}}" + 
				"}"));

		Profile profile_2 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 2," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 30," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 10.8," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"slow\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"15\"" + 
				"    }}}}" + 
				"}"));
		
		Profile profile_21 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 21," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 27," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 10.5," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"slow\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"15\"" + 
				"    }}}}" + 
				"}"));
		
		Profile profile_22 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 22," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 33," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 11.1," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"slow\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"15\"" + 
				"    }}}}" + 
				"}"));
		
		Profile profile_3 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 3," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 70," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 5.5," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"fast\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"23\"" + 
				"    }}}}" + 
				"}"));
		
		Profile profile_31 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 31," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 67," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 5.2," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"fast\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"23\"" + 
				"    }}}}" + 
				"}"));
		
		
		Profile profile_32 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 32," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 73," + 
				"		\"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 5.8," + 
				"		\"http://registry.easytv.eu/application/hbbtv/screen/reader/speed\": \"fast\"," + 
				"		\"http://registry.easytv.eu/application/cs/ui/text/size\": \"23\"" + 
				"    }}}}" + 
				"}"));

		
		List<Profile> profiles = new ArrayList<Profile>();
		profiles.add(profile_1); profiles.add(profile_11); profiles.add(profile_12); //first cluster
		profiles.add(profile_2); profiles.add(profile_21); profiles.add(profile_22); //second cluster
		profiles.add(profile_3); profiles.add(profile_31); profiles.add(profile_32); //third cluster
		
		//print profiles distances
		print_dissimilarity_distances_of_profiles(profiles);
			
		DBSCANClusterer<Profile> dbscan = new DBSCANClusterer<Profile>(0.1, 2, distanceMeasure);
		List<Cluster<Profile>> actual = dbscan.cluster(profiles);
		
		
		List<Cluster<Profile>> expected = new ArrayList<Cluster<Profile>>();
		Cluster<Profile> cluster_1 = new Cluster<Profile>();
		Cluster<Profile> cluster_2 = new Cluster<Profile>();
		Cluster<Profile> cluster_3 = new Cluster<Profile>();
		cluster_1.addPoint(profile_1); cluster_1.addPoint(profile_11); cluster_1.addPoint(profile_12);
		cluster_2.addPoint(profile_2); cluster_2.addPoint(profile_21); cluster_2.addPoint(profile_22);
		cluster_3.addPoint(profile_3); cluster_3.addPoint(profile_31); cluster_3.addPoint(profile_32);

		expected.add(cluster_1); expected.add(cluster_2); expected.add(cluster_3);
	
		Assert.assertEquals(actual.size(), expected.size());
		Assert.assertEquals(actual.get(0).getPoints(), expected.get(0).getPoints());
		Assert.assertEquals(actual.get(1).getPoints(), expected.get(1).getPoints());
		Assert.assertEquals(actual.get(2).getPoints(), expected.get(2).getPoints());

	}
	
	
	@Test
	public void test_cluster_1() throws IOException, UserProfileParsingException {
		
		Profile profile_1 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 1," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 5," + 
				"		\"http://registry.easytv.eu/common/contrast\": 5" + 
				"    }}}}" + 
				"}"));
		
		Profile profile_11 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 11," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 5," + 
				"		\"http://registry.easytv.eu/common/contrast\": 6" + 
				"    }}}}" + 
				"}"));
		
		Profile profile_12 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 12," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 6," + 
				"		\"http://registry.easytv.eu/common/contrast\": 5" + 
				"    }}}}" + 
				"}"));

		Profile profile_2 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 2," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 30," + 
				"		\"http://registry.easytv.eu/common/contrast\": 30" + 
				"    }}}}" + 
				"}"));
		
		Profile profile_21 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 21," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 31," + 
				"		\"http://registry.easytv.eu/common/contrast\": 30" + 
				"    }}}}" + 
				"}"));
		
		Profile profile_22 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 22," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 30," + 
				"		\"http://registry.easytv.eu/common/contrast\": 31" + 
				"    }}}}" + 
				"}"));
		
		Profile profile_3 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 3," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 80," + 
				"		\"http://registry.easytv.eu/common/contrast\": 80" + 
				"    }}}}" + 
				"}"));
		
		Profile profile_31 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 31," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 81," + 
				"		\"http://registry.easytv.eu/common/contrast\": 80" + 
				"    }}}}" + 
				"}"));
		
		
		Profile profile_32 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 32," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 80," + 
				"		\"http://registry.easytv.eu/common/contrast\": 81" + 
				"    }}}}" + 
				"}"));

			
		List<Profile> profiles = new ArrayList<Profile>();
		profiles.add(profile_1); profiles.add(profile_11); profiles.add(profile_12);
		profiles.add(profile_2); profiles.add(profile_21); profiles.add(profile_22);
		profiles.add(profile_3); profiles.add(profile_31); profiles.add(profile_32);
		
		//print profiles distances
		//print_dissimilarity_distances_of_profiles(profiles);
			
		DBSCANClusterer<Profile> dbscan = new DBSCANClusterer<Profile>(0.04, 2, distanceMeasure);
		List<Cluster<Profile>> actual = dbscan.cluster(profiles);
		
		
		List<Cluster<Profile>> expected = new ArrayList<Cluster<Profile>>();
		Cluster<Profile> cluster_1 = new Cluster<Profile>();
		Cluster<Profile> cluster_2 = new Cluster<Profile>();
		Cluster<Profile> cluster_3 = new Cluster<Profile>();
		cluster_1.addPoint(profile_1); cluster_1.addPoint(profile_11); cluster_1.addPoint(profile_12);
		cluster_2.addPoint(profile_2); cluster_2.addPoint(profile_21); cluster_2.addPoint(profile_22);
		cluster_3.addPoint(profile_3); cluster_3.addPoint(profile_31); cluster_3.addPoint(profile_32);

		expected.add(cluster_1); expected.add(cluster_2); expected.add(cluster_3);
	
		Assert.assertEquals(actual.size(), expected.size());
		Assert.assertEquals(actual.get(0).getPoints(), expected.get(0).getPoints());
		Assert.assertEquals(actual.get(1).getPoints(), expected.get(1).getPoints());
		Assert.assertEquals(actual.get(2).getPoints(), expected.get(2).getPoints());
		
	}
	
	/**
	 * A utility function that help printing profiles distances
	 * @param profiles
	 */
	private void print_dissimilarity_distances_of_profiles(List<Profile> profiles) {
		
		for(int i = 0; i < profiles.size(); i++) {
			Profile p1 = profiles.get(i);
			for(int j = 0; j < profiles.size(); j++) {
				if(i == j) {
					System.out.print(String.format(" %5.2f ", 0.0));
				} else {
					Profile p2 = profiles.get(j);
					System.out.print(String.format(" %5.2f ",distanceMeasure.compute(p1.getPoint(), p2.getPoint())));
				}
			}
			System.out.println();
		}
		
	}
	
}
