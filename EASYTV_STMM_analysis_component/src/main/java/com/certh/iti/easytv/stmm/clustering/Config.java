package com.certh.iti.easytv.stmm.clustering;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Config {
	
	private static Config instance = null;
	public List<iClustering> Config;
	private List<iClustering> _Clusterers;
	
	private enum ParseMode{
		None,
		Clusterer
	};
	
	private Config() {
		Config = new ArrayList<iClustering>();
		_Clusterers = new ArrayList<iClustering>();
		_Clusterers.add(new DBScan());
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
		iClustering _CurrentClusterer = null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		
		while((line = reader.readLine()) != null) {
			line = line.trim();
			if(line.startsWith("[")) {
				parseMode = ParseMode.None;
				
				Iterator<iClustering> clustersIter = instance._Clusterers.iterator();
				while(clustersIter.hasNext()) {
					iClustering clusterer = clustersIter.next();
					if(line.toLowerCase().equals("["+clusterer.get_Name().toLowerCase()+"]")) {
						parseMode = ParseMode.Clusterer;
						instance.Config.add(clusterer.Clone());
						_CurrentClusterer = instance.Config.get(instance.Config.size() - 1);
					}
				}
				
			} else {
				if(parseMode == ParseMode.Clusterer) {
                    name = line.split("=")[0].trim();
                    value = line.split("=")[1].trim();
                    Field field = _CurrentClusterer.getClass().getField(name);
                    field.set(_CurrentClusterer, Double.valueOf(value));
				}
			}
		}
		
		reader.close();
	}
}
