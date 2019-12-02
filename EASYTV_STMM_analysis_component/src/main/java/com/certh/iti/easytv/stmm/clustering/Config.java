package com.certh.iti.easytv.stmm.clustering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Config {
	
	private final static Logger logger = Logger.getLogger(Config.class.getName());

	
	private static Config instance = null;
	public final List<iCluster> Clusteres;
	private final List<iCluster> _Clusterers;
	
	private enum ParseMode{
		None,
		Clusterer
	};
	
	private Config() {
		Clusteres = new ArrayList<iCluster>();
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
					instance.Clusteres.add(_CurrentClusterer.Clone());
				
				for(iCluster clusterer : instance._Clusterers) {
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
		instance.Clusteres.add(_CurrentClusterer.Clone());
		
		reader.close();
		
		String msg = "";
		for(iCluster clusterer: instance.Clusteres) 
			msg += "["+clusterer.get_Name()+"] "+ clusterer.toString();
		
		logger.info("Clusterers...\n"+msg);
		
	}
}
