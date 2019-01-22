package com.certh.iti.easytv.stmm.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.certh.iti.easytv.stmm.preferences.EntryManager;
import com.certh.iti.easytv.stmm.preferences.Profile;

public class Parser {
	
	private static final String _TokenContext = "[context]";
	private static final String _TokenPreferences = "[preferences]";



	/**
	 * @brief Load profile from files
	 * 
	 * @param file
	 * @param profiles
	 * @throws IOException
	 */
	public static void ReadProfile(File file, List<Profile> profiles) throws IOException {
		System.out.println("Reading profile: " + file.getAbsolutePath() + "");
		Profile profile =  new Profile(file);
		
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
				parseMode = Parser.ParseMode.Context;
			} else if (line.toLowerCase().equals(_TokenPreferences)) {
                parseMode = Parser.ParseMode.Preferences;
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
							profile.ContextEntries.put(newEntry.get_Name(), newEntry);
							break;
						case Preferences:
							com.certh.iti.easytv.stmm.preferences.Entry newEntry1 = EntryManager.ToEntry(application, name, value);
							//if(newEntry1 == null) continue;
							profile.PreferenceEntries.put(newEntry1.get_Name(), newEntry1);
							break;
						default:
							break;
					}
				}
			}
		}
		reader.close();
		
		profiles.add(profile);
	}
	
	/*
	 * 
	 * @param file
	 * @param output output directory path
	 */
	public static void PreprocessProfile(File file, File output) throws IOException {
		
		class PreprocessorVars{
			public double start;
			public double stepInc;
			public PreprocessorVars(double start, double stepInc) {
				this.start = start;
				this.stepInc = stepInc;
			}
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String line;
		int steps = 0;
		StringBuilder result = new StringBuilder();
		HashMap<String, PreprocessorVars> vars = new HashMap<String, PreprocessorVars>();
		
		while((line = reader.readLine()) != null) {
			line = line.trim();
			if(line.equals(" ")) continue;
			if(line.startsWith("|") && line.endsWith("|")) {
                line = line.replace("|", "");
                String[] split = line.split("=");
                if(split.length == 2) {
                	String key = split[0].trim();
                	String value = split[1].trim();
                	if(key.equals("steps")) {
                		steps = Integer.parseInt(value);
                	} else {
                		String[] varParams = value.split(";");
                		if(varParams.length == 2) {
                			double start = 0.0, stepInc = 0.0;
                			start = Double.parseDouble(varParams[0]);
                			stepInc = Double.parseDouble(varParams[1]);
                			vars.put("|"+key+"|", new PreprocessorVars(start, stepInc));
                		}
                	}
                } 
			} else {
                result.append(line);
                result.append(System.lineSeparator());
			}
		}
		
		reader.close();
		
		String resultContent = result.toString();
		for(int i = 0 ; i < steps - 1; i++) {
			String curResult = resultContent;
			Iterator<Entry<String, PreprocessorVars>> varsIter = vars.entrySet().iterator();
			while(varsIter.hasNext()) {
				Entry<String, PreprocessorVars> var = varsIter.next();
				curResult = curResult.replace(var.getKey(), String.valueOf(var.getValue().start + var.getValue().stepInc * i));
			}
			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output.getAbsolutePath() + File.separator + file.getName().split("\\.")[0] + "_" + i + ".ini")));
            writer.write(curResult);
            writer.close();
		}
	}
	
	private enum ParseMode{
        None,
        Context,
        Preferences
	}

}
