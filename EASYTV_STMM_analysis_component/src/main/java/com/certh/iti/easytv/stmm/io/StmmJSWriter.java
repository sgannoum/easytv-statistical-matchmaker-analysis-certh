package com.certh.iti.easytv.stmm.io;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.UserContent;
import com.certh.iti.easytv.user.UserContext;
import com.certh.iti.easytv.user.preference.Preference;
import com.certh.iti.easytv.user.preference.attributes.AsymmetricBinaryAttribute;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.ColorAttribute;
import com.certh.iti.easytv.user.preference.attributes.DoubleAttribute;
import com.certh.iti.easytv.user.preference.attributes.IntegerAttribute;
import com.certh.iti.easytv.user.preference.attributes.MultiNominalAttribute;
import com.certh.iti.easytv.user.preference.attributes.NominalAttribute;
import com.certh.iti.easytv.user.preference.attributes.NumericAttribute;
import com.certh.iti.easytv.user.preference.attributes.OrdinalAttribute;
import com.certh.iti.easytv.user.preference.attributes.SymmetricBinaryAttribute;
import com.certh.iti.easytv.user.preference.attributes.TimeAttribute;

public class StmmJSWriter implements ProfileWriter{
	
	protected List<Profile> clusters;
	protected File outputDirectory;
	protected Date now;
	
	public StmmJSWriter(File outputDirectory, List<Profile> clusters) {
		this.clusters = clusters;
		this.outputDirectory = outputDirectory;
		now = new Date();
	}
	
	/**
	 * Write a javascript file that represent all the given clusters, the file is used by 
	 * the runtime component to answer matching requests.
	 * @param clusters
	 * @throws IOException
	 */
	public void write() {
        System.out.println("--------");
		System.out.println("write JS file to "+outputDirectory.getAbsolutePath());
		
		try 
		{
			writeDimensionHandler(outputDirectory);
			writeClusterProfile(clusters, outputDirectory);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Add JS header comments to the generated JS files
	 * 
	 * @param writer
	 * @throws IOException
	 */
	protected void AddJSHeaderComments(PrintWriter writer) throws IOException {

		DateFormat df = new SimpleDateFormat("dd MMM HH:mm:ss");
		
        writer.println("/*!");
        writer.println();
        writer.println("EasyTV Matchmaker ");
        writer.println();
        writer.println("Copyright 2017-2020 Center for Research & Technology - HELLAS");
        writer.println();
//        writer.println("Licensed under the New BSD License. You may not use this file except in");
//        writer.println("compliance with this licence.");
//        writer.println();
//        writer.println("You may obtain a copy of the licence at");
//        writer.println("https://github.com/REMEXLabs/GPII-Statistical-Matchmaker/blob/master/LICENSE.txt");
        writer.println();
        writer.println("The research leading to these results has received funding from");
        writer.println("the European Union's H2020-ICT-2016-2, ICT-19-2017 Media and content convergence");
        writer.println("under grant agreement no. 761999.");
        writer.println("*/");
        writer.println();
        writer.println("//Generated " +  df.format(now));
        writer.println();
	}
	

	/**
	 * Write a javascript file that represent all the given clusters, the file is used by 
	 * the runtime component to answer matching requests.
	 * @param clusters
	 * @throws IOException
	 */
	private void writeDimensionHandler(File outputDirectory) throws IOException {
		
		File _stmmOutputFile = new File(outputDirectory.getAbsolutePath()+ File.separatorChar + "GENERATED_dimensions_handlers.js");
		if(!_stmmOutputFile.exists())
			_stmmOutputFile.createNewFile();
				
		PrintWriter writer = new PrintWriter(_stmmOutputFile);
		
		//add JS comments
		AddJSHeaderComments(writer);

		//add import dimensions handlers comments
        writer.println();
        writer.println("var Numeric = require(\"./DimensionHandlers\").Numeric");
        writer.println("var IntegerNumeric = require(\"./DimensionHandlers\").IntegerNumeric");
        writer.println("var Nominal = require(\"./DimensionHandlers\").Nominal");
        writer.println("var Ordinal = require(\"./DimensionHandlers\").Ordinal");
        writer.println("var SymmetricBinary = require(\"./DimensionHandlers\").SymmetricBinary");
        writer.println("var Color = require(\"./DimensionHandlers\").Color");
        writer.println();
		
        //entry count
        writer.println("var stat = {}");
        writer.println("stat.preferenceHandlers = new Map();");
      
        //add preference handlers
        for(Entry<String, Attribute> entry : Preference.preferencesAttributes.entrySet()) {
        	Attribute operand =  entry.getValue();
        	String handlerInstance = "";
        	
        	 handlerInstance = handle(operand);
        	 writer.println(String.format("stat.preferenceHandlers.set(\"%s\", %s)", entry.getKey(), handlerInstance));
        }
        
   	 	writer.println();
   	 	writer.println();
        
   	 	//add contextual handlers
        writer.println("stat.contextHandlers = new Map();");
        for(Entry<String, Attribute> entry : UserContext.contextAttributes.entrySet()) {
        	Attribute operand =  entry.getValue();
        	String handlerInstance = "";
        	
        	 handlerInstance = handle(operand);
        	 writer.println(String.format("stat.contextHandlers.set(\"%s\", %s)", entry.getKey(), handlerInstance));
        }
        
   	 	writer.println();
   	 	writer.println();
        
        //add content handlers
   	 	writer.println("stat.contentHandlers = new Map();");
        for(Entry<String, Attribute> entry : UserContent.content_attributes.entrySet()) {
        	Attribute operand =  entry.getValue();
        	String handlerInstance = "";
        	
        	 handlerInstance = handle(operand);
        	 writer.println(String.format("stat.contentHandlers.set(\"%s\", %s)", entry.getKey(), handlerInstance));
        }
        
        writer.println();

        //clusters   
        writer.println();
        writer.println("module.exports = stat;");
        writer.close();
	}
	
	/**
	 * Write a javascript file that represent all the given clusters, the file is used by 
	 * the runtime component to answer matching requests.
	 * @param clusters
	 * @throws IOException
	 */
	private void writeClusterProfile(List<Profile> clusters, File outputDirectory) throws IOException {
		
		File _stmmOutputFile = new File(outputDirectory.getAbsolutePath()+ File.separatorChar + "GENERATED_clusters_data.js");
		if(!_stmmOutputFile.exists())
			_stmmOutputFile.createNewFile();
				
		PrintWriter writer = new PrintWriter(_stmmOutputFile);
		
		//add JS comments
		AddJSHeaderComments(writer);
		
        //entry count
        writer.println("var stat = {}");
        writer.println("stat.entryCount = " + clusters.size() + ";");

        //clusters
        writer.println("stat.clusters = [");
        
        for(int i = 0 ; i < clusters.size() - 1; i++) {
        	writer.println(clusters.get(i).getJSONObject().toString(4)+",");
        }
        
        if(clusters.size() > 0) {
        	writer.println(clusters.get(clusters.size() - 1).getJSONObject().toString(4));
        }
        
        writer.println("];");    
        writer.println();
        writer.println("module.exports = stat;");
        writer.close();
	}
	
	protected String handle(Attribute operand) {
		String handlerInstance = "";
		
    	if(ColorAttribute.class.isInstance(operand)) {
    		
			ColorAttribute colorAttribute = (ColorAttribute) operand;
			
			for (NumericAttribute attribte : colorAttribute.getDimensions()) 
				handlerInstance += String.format("new IntegerNumeric(%.1f, %.1f, %.1f), ", attribte.getMaxValue(), 
																					 	   attribte.getMinValue(), 
																					 	   attribte.getOperandMissingValue());
			handlerInstance = handlerInstance.substring(0, handlerInstance.length() - 1 );
			handlerInstance = String.format("new Color(%s)", handlerInstance);
			
		} else if (IntegerAttribute.class.isInstance(operand)) {
			
    		IntegerAttribute intNumeric = (IntegerAttribute) operand;
				
    		handlerInstance = String.format("new IntegerNumeric(%.1f, %.1f, %.1f)", intNumeric.getMaxValue(), 
    																		  		intNumeric.getMinValue(), 
    																		  		intNumeric.getOperandMissingValue());
				
		} else if (DoubleAttribute.class.isInstance(operand)) {
			
    		DoubleAttribute doubleNumeric = (DoubleAttribute) operand;
			
    		handlerInstance = String.format("new DoubleNumeric(%.1f, %.1f, %.1f)", doubleNumeric.getMaxValue(), 
    																		  	   doubleNumeric.getMinValue(), 
    																		       doubleNumeric.getOperandMissingValue());

		} else if (OrdinalAttribute.class.isInstance(operand)) {
			
			OrdinalAttribute ordinal = (OrdinalAttribute) operand;
			
			String states = "";
			for(String state : ordinal.getStates())
				states += "\""+state+"\",";
			
			states = states.substring(0, states.length() - 1).toLowerCase();
			
    		handlerInstance = String.format("new Ordinal([%s], %.1f, %.1f, %.1f)", states, 
    																				ordinal.getMaxValue(), 
    																				ordinal.getMinValue(), 
    																				ordinal.getOperandMissingValue());
			
		} else if (MultiNominalAttribute.class.isInstance(operand)) {
			
    		MultiNominalAttribute nominal = (MultiNominalAttribute) operand;
			
			String states = "";
			for(String state : nominal.getStates())
				states += "\""+state+"\",";
			
			states = states.substring(0, states.length() - 1).toLowerCase();
			
    		handlerInstance = String.format("new MultiNominal([%s], %.1f)", states, 
    																		operand.getOperandMissingValue());
		} else if (NominalAttribute.class.isInstance(operand)) {
			
			NominalAttribute nominal = (NominalAttribute) operand;
			
			String states = "";
			for(String state : nominal.getStates())
				states += "\""+state+"\",";
			
			states = states.substring(0, states.length() - 1).toLowerCase();
			
    		handlerInstance = String.format("new Nominal([%s], %.1f)", states, 
    																	operand.getOperandMissingValue());
			
		} else if (SymmetricBinaryAttribute.class.isInstance(operand)) {
    		
    		handlerInstance = String.format("new SymmetricBinary(%.1f)", operand.getOperandMissingValue());
		} else if (AsymmetricBinaryAttribute.class.isInstance(operand)) {
			//TODO
    		//handlerInstance = new AsymmetricBinary();
		} else if(TimeAttribute.class.isInstance(operand)) {
    		//TODO
    	}
    	
    	return handlerInstance;
	}


}
