package com.certh.iti.easytv.stmm.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.commons.math3.ml.clustering.Cluster;

import com.certh.iti.easytv.user.UserProfile;

public class DirectoryProfileReader implements ProfileReader {
	
	private File profilesDirector;
	
	public DirectoryProfileReader(File directory) {
		this.profilesDirector = directory;
	}
	
	
	public Cluster<UserProfile> readProfiles() {
		Cluster<UserProfile> profiles = new Cluster<UserProfile>();
		
		//preprocess
/*		try {
			PreprocessFrom(profilesDirector, generatedDirectory);
		} catch (IOException e) {
			e.printStackTrace();
		}
*/
        System.out.println("--------");
        System.out.println("Reading files from:" + profilesDirector.getAbsolutePath());
        
		//read profiles
		try {
			ReadProfilesFrom(profilesDirector, profiles);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return profiles;
	}
	
	/**
	 * @brief Process files from the given directory
	 * 
	 * @param directory
	 * @param generatedDirectory
	 * @throws IOException
	 */
	private void PreprocessFrom(File directory, File generatedDirectory) throws IOException {
		if (directory == null || !directory.exists())
			return;

		class dirFileFilter implements FileFilter {
			public boolean accept(File dir) {
				return dir.isDirectory();
			}
		}

		File[] directories = directory.listFiles(new dirFileFilter());
		for (int i = 0; i < directories.length; i++)
			PreprocessFrom(directories[i], generatedDirectory);
	}

	/**
	 * @brief Recursively process ".ini" files contained in the given directory and all its directories.
	 * 
	 * @param directory
	 * @throws IOException
	 */
	private void ReadProfilesFrom(File directory, Cluster<UserProfile> profiles) throws IOException {
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
			System.out.println("Reading file: " +iniFiles[i].getPath());
			profiles.addPoint(new UserProfile(iniFiles[i]));
		}

		class dirFileFilter implements FileFilter {
			public boolean accept(File dir) {
				return dir.isDirectory();
			}
		}

		File[] directories = directory.listFiles(new dirFileFilter());
		for (int i = 0; i < directories.length; i++)
			ReadProfilesFrom(directories[i], profiles);
	}

}
