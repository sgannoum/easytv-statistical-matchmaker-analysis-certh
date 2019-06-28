package com.certh.iti.easytv.stmm.clustering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.ml.clustering.Clusterer;

import com.certh.iti.easytv.user.UserProfile;

public class Config {
	
	private static Config instance = null;
	public List<Clusterer<UserProfile>> Config;
	private final List<iCluster> _Clusterers;
	
	private enum ParseMode{
		None,
		Clusterer
	};
	
	private Config() {
		Config = new ArrayList<Clusterer<UserProfile>>();
		_Clusterers = new ArrayList<iCluster>();
		_Clusterers.add(new DBScanWrapper());
		_Clusterers.add(new KMeansPlusPlusWrapper());
		_Clusterers.add(new MultiKMeansPlusPlusWrapper());
	}
	
	public static Config getInstance() {
		if(instance == null) {
			instance = new Config();
		}
		return instance;
	}
	
	/**
	 * Load clusters configurations from the given file.
	 * 
	 * @param file
	 * @throws IOException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws NumberFormatException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */	
	public void ReadConfiguration(File file) throws IOException, NumberFormatException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		ParseMode parseMode = ParseMode.None;
		String line, name, value;
		iCluster _CurrentClusterer = null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		
		while((line = reader.readLine()) != null) {
			line = line.trim();
			
			//Specify clustering algorithm
			if(line.startsWith("[")) {
				parseMode = ParseMode.None;
				
				//Clone the previous algorithm
				if(_CurrentClusterer != null) 
					instance.Config.add(_CurrentClusterer.Clone());
				
				Iterator<iCluster> clustersIter = instance._Clusterers.iterator();
				while(clustersIter.hasNext()) {
					iCluster clusterer = clustersIter.next();
					if(line.toLowerCase().equals("["+clusterer.get_Name().toLowerCase()+"]")) {
						parseMode = ParseMode.Clusterer;
						_CurrentClusterer = clusterer;
					}
				}
				
			} else if(parseMode == ParseMode.Clusterer) {
				
				//Set parameters
                name = line.split("=")[0].trim();
                value = line.split("=")[1].trim();
                
                Field field = _CurrentClusterer.getClass().getDeclaredField(name);
                
                if(field.getType().isArray()) {
                    String[] values = null;
                    
                    //Specify one or multiple values
                    if(value.contains(",")) 
                    	values = value.split(",");
                    else 
                    	values = new String[]{value};
                    
                    field.setAccessible(true);
                    field.set(_CurrentClusterer, values);
                } else if(field.getType().equals(int.class) || field.getType().equals(Integer.class) ) {
                    field.setAccessible(true);
                    field.set(_CurrentClusterer, Integer.valueOf(value));
                } else if(field.getType().equals(double.class) || field.getType().equals(Double.class)) {
                    field.setAccessible(true);
                    field.set(_CurrentClusterer, Double.valueOf(value));
                } else if(field.getType().equals(String.class)) {
                    field.setAccessible(true);
                    field.set(_CurrentClusterer, value);
                }

			}
		}
		
        //clone
		instance.Config.add(_CurrentClusterer.Clone());
		
		reader.close();
	}
}
