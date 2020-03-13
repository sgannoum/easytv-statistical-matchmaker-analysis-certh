package com.certh.iti.easytv.stmm.clustering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.certh.iti.easytv.stmm.similarity.DimensionsGenerator;
import com.certh.iti.easytv.stmm.similarity.SimilarityMeasure;
import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

public class DBScanProfilesTest {
	
	private DimensionsGenerator dimensionsGenerator;
	private DistanceMeasure distanceMeasure;
	
	@BeforeClass
	public void beforeTest() {
		dimensionsGenerator = new DimensionsGenerator(Profile.getUris(), Profile.getOperands());
		distanceMeasure = new SimilarityMeasure(dimensionsGenerator.getLables(), dimensionsGenerator.getDimensions());
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
		profiles.add(profile_1); profiles.add(profile_11); profiles.add(profile_12);
		profiles.add(profile_2); profiles.add(profile_21); profiles.add(profile_22);
		profiles.add(profile_3); profiles.add(profile_31); profiles.add(profile_32);
		
		for(int i = 0; i < profiles.size(); i++) {
			Profile p1 = profiles.get(i);
			for(int j = i + 1; j < profiles.size(); j++) {
				Profile p2 = profiles.get(j);
				System.out.print(" "+distanceMeasure.compute(p1.getPoint(), p2.getPoint()));
			}
			System.out.println();
		}
			
		
		DBSCANClusterer<Profile> dbscan = new DBSCANClusterer<Profile>(0.95, 2, distanceMeasure);
		List<Cluster<Profile>> clusters = dbscan.cluster(profiles);
		
		System.out.println("Cluster size: "+clusters.size());
		for(Cluster<Profile> cluster : clusters) {
			for(Profile p : cluster.getPoints())
				System.out.print(" "+p.getUserId());
			System.out.println();
		}
		
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
				"		\"http://registry.easytv.eu/common/contrast\": 10" + 
				"    }}}}" + 
				"}"));
		
		Profile profile_12 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 12," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 10," + 
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
				"		\"http://registry.easytv.eu/common/volume\": 40," + 
				"		\"http://registry.easytv.eu/common/contrast\": 30" + 
				"    }}}}" + 
				"}"));
		
		Profile profile_22 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 22," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 30," + 
				"		\"http://registry.easytv.eu/common/contrast\": 40" + 
				"    }}}}" + 
				"}"));
		
		Profile profile_3 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 3," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 70," + 
				"		\"http://registry.easytv.eu/common/contrast\": 70" + 
				"    }}}}" + 
				"}"));
		
		Profile profile_31 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 31," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 70," + 
				"		\"http://registry.easytv.eu/common/contrast\": 80" + 
				"    }}}}" + 
				"}"));
		
		
		Profile profile_32 = new Profile( new JSONObject("{" + 
				"    \"user_id\": 32," + 
				"    \"user_profile\": {\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"		\"http://registry.easytv.eu/common/volume\": 80," + 
				"		\"http://registry.easytv.eu/common/contrast\": 70" + 
				"    }}}}" + 
				"}"));

		
		List<Profile> profiles = new ArrayList<Profile>();
		profiles.add(profile_1); profiles.add(profile_11); profiles.add(profile_12);
		//profiles.add(profile_2); profiles.add(profile_21); profiles.add(profile_22);
		profiles.add(profile_3); profiles.add(profile_31); profiles.add(profile_32);
		
		
		DBSCANClusterer<Profile> dbscan = new DBSCANClusterer<Profile>(0.99, 4, distanceMeasure);
		List<Cluster<Profile>> clusters = dbscan.cluster(profiles);
		
		System.out.println("Cluster size: "+clusters.size());
		for(Cluster<Profile> cluster : clusters) {
			List<Profile> aCluster = cluster.getPoints();
			for(int i = 0; i < aCluster.size(); i++) {
				Profile p1 = aCluster.get(i);
				for(int j = i + 1; j < aCluster.size(); j++) {
					Profile p2 = aCluster.get(j);
					System.out.print(" "+distanceMeasure.compute(p1.getPoint(), p2.getPoint()));
				}
				System.out.println();
			}
		}
		
	}
	
}
