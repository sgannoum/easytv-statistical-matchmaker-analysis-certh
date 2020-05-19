package com.certh.iti.easytv.stmm.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.json.JSONException;
import org.json.JSONObject;

import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

public class DirectoryProfileReader implements ProfileReader {
	
	private final static Logger logger = Logger.getLogger(DirectoryProfileReader.class.getName());

	//files filter
	class JSONFileFilters implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return name.endsWith(".json");
		}
	}
	
	class dirFileFilter implements FileFilter {
		public boolean accept(File dir) {
			return dir.isDirectory();
		}
	}
	
	private File profilesDirector;
	
	public DirectoryProfileReader(File directory) {
		this.profilesDirector = directory;
	}
	
	
	public Cluster<Profile> readProfiles() {
		Cluster<Profile> profiles = new Cluster<Profile>();
		
       // logger.info("--------");
        logger.info("Reading files from:" + profilesDirector.getAbsolutePath());
        
		//read profiles
		try {
			readProfiles(profilesDirector, profiles);
		} catch (IOException | JSONException | UserProfileParsingException e) {
			e.printStackTrace();
		}

		return profiles;
	}

	/**
	 * @brief Recursively process ".ini" files contained in the given directory and all its directories.
	 * 
	 * @param directory
	 * @throws IOException
	 * @throws UserProfileParsingException 
	 * @throws JSONException 
	 */
	private void readProfiles(File directory, Cluster<Profile> profiles) throws IOException, JSONException, UserProfileParsingException {
		if (directory == null || !directory.exists())
			return;

		File[] iniFiles = directory.listFiles(new JSONFileFilters());
		for (int i = 0; i < iniFiles.length; i++) {
			logger.info("Reading file: " +iniFiles[i].getPath());
			
			try {
				profiles.addPoint(new Profile(iniFiles[i]));
			} catch(UserProfileParsingException e) {
				logger.warning(e.getMessage());
			}
		}

		File[] directories = directory.listFiles(new dirFileFilter());
		for (int i = 0; i < directories.length; i++)
			readProfiles(directories[i], profiles);
	}


	@Override
	public Cluster<Profile> readUserHisotryOfInteraction(int id, long timeInterval) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Integer> getUsersIds() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void writeUserModificationSuggestions(int id, JSONObject Suggestions) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void clearHisotryOfInteraction() {
		// TODO Auto-generated method stub
		
	}

}
