package com.certh.iti.easytv.stmm.preferences;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONObject;


public class Profile {
	
	private enum ParseMode{
        None,
        Context,
        Preferences
	}
	
	private static final String _TokenContext = "[context]";
	private static final String _TokenPreferences = "[preferences]";
	private final double NoiseReduction = 0.2;
	private Boolean _IsAbstract = true;
	private File _File;
	public HashMap<String, Entry> ContextEntries;
	public HashMap<String, Entry> PreferenceEntries;
	
	
	public Profile() {
		_IsAbstract = true;
		ContextEntries = new HashMap<String, Entry>();
		PreferenceEntries = new HashMap<String, Entry>();
	}
	
	public Profile(File file) throws IOException {
		_IsAbstract = false;
		_File = file;
		ContextEntries = new HashMap<String, Entry>();
		PreferenceEntries = new HashMap<String, Entry>();
		ReadProfile(file);
	}
	
	public Boolean IsAbstract() {
		return _IsAbstract;
	}

	public File get_File() {
		return _File;
	}

	public double DistanceTo(Profile profile) {
		return EuclideanDistanceTo(profile);
	}
	
	@Override
	public String toString() {
		return _File.getAbsolutePath();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!Profile.class.isInstance(obj)) return false;
		
		Profile other = (Profile) obj;
		if(!other._File.equals(this._File)) return false;
		if(!other._IsAbstract != this._IsAbstract) return false;
		if(!other.ContextEntries.equals(this.ContextEntries)) return false;
		if(!other.PreferenceEntries.equals(this.PreferenceEntries)) return false;
		
		return true;	
	}
	
	public void copy(Profile other) {	
		this._IsAbstract = other._IsAbstract;
		this._File = other._File;
		this.PreferenceEntries.putAll(other.PreferenceEntries);
		this.ContextEntries.putAll(other.ContextEntries);
	}
	
	public void write(String file) throws IOException {	
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file + ".ini"));
		write(out);
	}
	
	public void write(File outputFile) throws IOException {	
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(outputFile));
		write(out);
	}
	
	public void write(PrintStream outputFile) throws IOException {	
		OutputStreamWriter out = new OutputStreamWriter(outputFile);
		write(out);
	}
	
	public void write(OutputStreamWriter out) throws IOException {
		
		BufferedWriter writer = new BufferedWriter(out);
		writer.write(_File.getAbsolutePath());
		writer.newLine();
		Iterator<java.util.Map.Entry<String, Entry>> preferenceIterator = ContextEntries.entrySet().iterator();
		writer.write("[context]");
		writer.newLine();
		while(preferenceIterator.hasNext()) {
			java.util.Map.Entry<String, Entry> pair =  preferenceIterator.next();
			writer.write(pair.toString());
			writer.newLine();
		}
		
		preferenceIterator = PreferenceEntries.entrySet().iterator();
		writer.write("[preference]");
		writer.newLine();
		while(preferenceIterator.hasNext()) {
			java.util.Map.Entry<String, Entry> pair =  preferenceIterator.next();
			writer.write(pair.toString());
			writer.newLine();
		}
		writer.flush();
				
	}
	
	public void print() throws IOException {
		write(System.out);
	}
	
	/**
	 * @brief Calculate the Euclidean distance between two profiles.
	 * 
	 * @param other
	 * @return
	 */
	private double EuclideanDistanceTo(Profile other) {
		double result =  0.0, difference;
		
		//common preferences
		Iterator<java.util.Map.Entry<String, Entry>> preferenceIterator = PreferenceEntries.entrySet().iterator();
		while(preferenceIterator.hasNext()) {
			difference = 0.0;
			java.util.Map.Entry<String, Entry> preferencePair = preferenceIterator.next();
			if(other.PreferenceEntries.containsKey(preferencePair.getKey())) {
				if(EntryManager.IsEnumeration(preferencePair.getKey())) {
					if(preferencePair.getValue().get_Value() != other.PreferenceEntries.get(preferencePair.getKey()).get_Value()) 
						difference = 1.0 / EntryManager.EnumerationSize(preferencePair.getKey());
				} else {
						difference = Math.abs(other.PreferenceEntries.get(preferencePair.getKey()).NormalizedValue() - preferencePair.getValue().NormalizedValue());
				}		
			} else {
				if(EntryManager.IsEnumeration(preferencePair.getKey())) { 
					difference = 1.0 / EntryManager.EnumerationSize(preferencePair.getKey());
				} else {
					difference = Math.abs(0.5 - preferencePair.getValue().NormalizedValue());
				}
                difference *= NoiseReduction;	
			}
			
			result += difference;
		}
		
		//non-common preferences
		preferenceIterator = other.PreferenceEntries.entrySet().iterator();
		while(preferenceIterator.hasNext()) {
			difference = 0.0;
			java.util.Map.Entry<String, Entry> preferencePair = preferenceIterator.next();
			if(!PreferenceEntries.containsKey(preferencePair.getKey())) {
				if(EntryManager.IsEnumeration(preferencePair.getKey())) {
					difference = 1.0 / EntryManager.EnumerationSize(preferencePair.getKey());
				} else {
					difference = Math.abs(preferencePair.getValue().NormalizedValue() - 0.5);
				}			
				difference *= NoiseReduction;
			}
			result += difference;
		}
		return result;
	}
	
	/**
	 * @brief Load profile from files
	 * 
	 * @param file
	 * @param profiles
	 * @throws IOException
	 */
	private void ReadProfile(File file) throws IOException {
		System.out.println("Reading profile: " + file.getAbsolutePath() + "");
		
		//Read file
		ParseMode parseMode = ParseMode.None;
		String line, name, value, application = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		while((line = reader.readLine()) != null) {
			line = line.trim();
			
			//Remove Comments
			int commentStart = line.indexOf(";");
			if(commentStart >= 0) line = line.substring(0, commentStart);
			
			//Parse line
			//line = line.toLowerCase();
			if(line.toLowerCase().equals(_TokenContext)) {
				parseMode = ParseMode.Context;
			} else if (line.toLowerCase().equals(_TokenPreferences)) {
                parseMode = ParseMode.Preferences;
                application = "";
			} else {
				if(line.endsWith(","))
					line = line.substring(0, line.length() - 1);
				
				//Splitting
				if(line.contains("=")) {
					String tmp = line.trim().replace("\"", "").replace("\\", "");
                    name = tmp.split("=")[0].trim();
                    value = tmp.split("=")[1].trim();
				} else if (line.contains(":")) {
					String tmp = line.trim().replace("\"", "").replace("\\", "");
                    name = tmp.split(":")[0].trim();
                    value = tmp.split(":")[1].trim();
				} else {
					continue;
				}
				
				//Parsing
				if(name.equals("app")) {
					application = value.trim().replace(" ", "").replace("\"", "");
				} else {
					switch(parseMode) {
						case Context:
							com.certh.iti.easytv.stmm.preferences.Entry newEntry = EntryManager.ToEntry("context", name, value);
							//if(newEntry == null) continue;
							ContextEntries.put(newEntry.get_Name(), newEntry);
							break;
						case Preferences:
							com.certh.iti.easytv.stmm.preferences.Entry newEntry1 = EntryManager.ToEntry(application, name, value);
							//if(newEntry1 == null) continue;
							PreferenceEntries.put(newEntry1.get_Name(), newEntry1);
							break;
						default:
							break;
					}
				}
			}
		}
		reader.close();
	}
	

}
