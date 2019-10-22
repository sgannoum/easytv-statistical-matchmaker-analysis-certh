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

public class HbmmJSWriter extends StmmJSWriter{
	
	
	public HbmmJSWriter(File outputDirectory, List<UserProfile> clusters) {
		super(outputDirectory, clusters);
	}
	
	/**
	 * Write a javascript file that represent all the given clusters, the file is used by 
	 * the runtime component to answer matching requests.
	 * @param clusters
	 * @throws IOException
	 */
	@Override
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
	 * Write a javascript file that has maps preferences URL to proper dimension handler
	 * @param clusters
	 * @throws IOException
	 */
	private void WriteJavaScript(List<UserProfile> clusters, File outputDirectory) throws IOException {
		
		File _hbmmOutputFile = new File(outputDirectory.getAbsolutePath()+ File.separatorChar + "HybridMatchMakerData.js");
		if(!_hbmmOutputFile.exists())
			_hbmmOutputFile.createNewFile();
				
		PrintWriter writer = new PrintWriter(_hbmmOutputFile);

		//add JS comments
		AddJSHeaderComments(writer);

		//add import dimensions handlers comments
		AddJSImportDimensionsHandlers(writer);
		writer.println("var hybrid = {}");
        writer.println("hybrid.dimensionsHandlers = new Map();");

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
        	
        	 writer.println("hybrid.dimensionsHandlers.set(\"" +entry.getKey() + "\", "+ handlerInstance +");");
        }
        
        writer.println();
        writer.println("module.exports = hybrid;");
        writer.close();
	}


}
