package com.certh.iti.easytv.stmm.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.json.JSONException;

import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

public class DirectoryProfileReader implements ProfileReader {
	
	private final static Logger logger = Logger.getLogger(DirectoryProfileReader.class.getName());

	
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

		//files filter
		class IniFileFilter implements FilenameFilter {
			public boolean accept(File dir, String name) {
				return name.endsWith(".json");
			}
		}

		File[] iniFiles = directory.listFiles(new IniFileFilter());
		for (int i = 0; i < iniFiles.length; i++) {
			logger.info("Reading file: " +iniFiles[i].getPath());
			profiles.addPoint(new Profile(iniFiles[i]));
		}

		class dirFileFilter implements FileFilter {
			public boolean accept(File dir) {
				return dir.isDirectory();
			}
		}

		File[] directories = directory.listFiles(new dirFileFilter());
		for (int i = 0; i < directories.length; i++)
			readProfiles(directories[i], profiles);
	}

}
