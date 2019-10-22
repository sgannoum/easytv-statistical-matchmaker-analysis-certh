package com.certh.iti.easytv.stmm.io;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import com.certh.iti.easytv.user.UserProfile;
import com.certh.iti.easytv.user.preference.Preference;
import com.certh.iti.easytv.user.preference.attributes.AsymmetricBinaryAttribute;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.ColorAttribute;
import com.certh.iti.easytv.user.preference.attributes.DoubleAttribute;
import com.certh.iti.easytv.user.preference.attributes.IntegerAttribute;
import com.certh.iti.easytv.user.preference.attributes.NominalAttribute;
import com.certh.iti.easytv.user.preference.attributes.NumericAttribute;
import com.certh.iti.easytv.user.preference.attributes.OrdinalAttribute;
import com.certh.iti.easytv.user.preference.attributes.SymmetricBinaryAttribute;

public class StmmJSWriter implements ProfileWriter{
	
	protected List<UserProfile> clusters;
	protected File outputDirectory;
	protected Date now;
	
	public StmmJSWriter(File outputDirectory, List<UserProfile> clusters) {
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
			WriteJavaScript(clusters, outputDirectory);
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
        writer.println("EasyTV Statistical Matchmaker ");
        writer.println();
        writer.println("Copyright 2017-2020 Center for Research & Technology - HELLAS");
        writer.println();
        writer.println("Licensed under the New BSD License. You may not use this file except in");
        writer.println("compliance with this licence.");
        writer.println();
        writer.println("You may obtain a copy of the licence at");
        writer.println("https://github.com/REMEXLabs/GPII-Statistical-Matchmaker/blob/master/LICENSE.txt");
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
	 * Add JS import instruction to the created JS files
	 * 
	 * @param writer
	 * @throws IOException
	 */
	protected void AddJSImportDimensionsHandlers(PrintWriter writer) throws IOException {
        //import dimensions handlers classes
        writer.println();
        writer.println("var Numeric = require(\"./DimensionHandlers\").Numeric");
        writer.println("var IntegerNumeric = require(\"./DimensionHandlers\").IntegerNumeric");
        writer.println("var Nominal = require(\"./DimensionHandlers\").Nominal");
        writer.println("var Ordinal = require(\"./DimensionHandlers\").Ordinal");
        writer.println("var SymmetricBinary = require(\"./DimensionHandlers\").SymmetricBinary");
        writer.println("var Color = require(\"./DimensionHandlers\").Color");
        writer.println();
	}

	/**
	 * Write a javascript file that represent all the given clusters, the file is used by 
	 * the runtime component to answer matching requests.
	 * @param clusters
	 * @throws IOException
	 */
	private void WriteJavaScript(List<UserProfile> clusters, File outputDirectory) throws IOException {
		
		File _stmmOutputFile = new File(outputDirectory.getAbsolutePath()+ File.separatorChar + "StatisticalMatchMakerData.js");
		if(!_stmmOutputFile.exists())
			_stmmOutputFile.createNewFile();
				
		PrintWriter writer = new PrintWriter(_stmmOutputFile);
		
		//add JS comments
		AddJSHeaderComments(writer);

		//add import dimensions handlers comments
		AddJSImportDimensionsHandlers(writer);
		
        //entry count
        writer.println("var stat = {}");
        writer.println("stat.entryCount = " + clusters.size() + ";");
        writer.println("stat.dimensionsHandlers = new Map();");

        //add preference handlers
        for(Entry<String, Attribute> entry : Preference.preferencesAttributes.entrySet()) {
        	Attribute operand =  entry.getValue();
        	String handlerInstance = "";
        	
        	if(ColorAttribute.class.isInstance(operand)) {
				ColorAttribute colorAttribute = (ColorAttribute) operand;
				
				for (NumericAttribute attribte : colorAttribute.getDimensions()) 
					handlerInstance += "new IntegerNumeric("+String.valueOf(attribte.getMaxValue())+", "+ String.valueOf(attribte.getMinValue())+", "+String.valueOf(attribte.getOperandMissingValue())+" ),";
				
				handlerInstance =  "new Color("+handlerInstance.substring(0, handlerInstance.length() - 1 ) +")";
				
			}
        	else if (IntegerAttribute.class.isInstance(operand)) {
        		IntegerAttribute intNumeric = (IntegerAttribute) operand;
 				
 				handlerInstance = "new IntegerNumeric("+String.valueOf(intNumeric.getMaxValue())+", "+ String.valueOf(intNumeric.getMinValue())+", "+String.valueOf(intNumeric.getOperandMissingValue())+" )";
 				
 			}
        	else if (DoubleAttribute.class.isInstance(operand)) {
        		DoubleAttribute doubleNumeric = (DoubleAttribute) operand;
				
				handlerInstance = "new Numeric("+String.valueOf(doubleNumeric.getMaxValue())+", "+ String.valueOf(doubleNumeric.getMinValue())+", "+String.valueOf(doubleNumeric.getOperandMissingValue())+" )";
				
			} 
        	else if (OrdinalAttribute.class.isInstance(operand)) {
				OrdinalAttribute ordinal = (OrdinalAttribute) operand;
				
				String states = "";
				for(String state : ordinal.getStates())
					states += "\""+state+"\",";
				
				handlerInstance = "new Ordinal(["+states.substring(0, states.length() - 1).toLowerCase()+"], "+ String.valueOf(ordinal.getMaxValue())+", "+ String.valueOf(ordinal.getMinValue())+", "+String.valueOf(ordinal.getOperandMissingValue())+" )";
				
			} 
        	else if (NominalAttribute.class.isInstance(operand)) {
				NominalAttribute nominal = (NominalAttribute) operand;
				
				
				String states = "";
				for(String state : nominal.getStates())
					states += "\""+state+"\",";
				
				handlerInstance = "new Nominal(["+states.substring(0, states.length() - 1).toLowerCase()+"], "+String.valueOf(operand.getOperandMissingValue())+")";
				
			} 
        	else if (SymmetricBinaryAttribute.class.isInstance(operand)) {
				
				handlerInstance = "new SymmetricBinary("+String.valueOf(operand.getOperandMissingValue())+")";

			} 
        	else if (AsymmetricBinaryAttribute.class.isInstance(operand)) {
				//handlerInstance = new AsymmetricBinary();
			}
        	
        	 writer.println("stat.dimensionsHandlers.set(\"" +entry.getKey() + "\", "+ handlerInstance +");");
        }
        
        writer.println();

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


}
