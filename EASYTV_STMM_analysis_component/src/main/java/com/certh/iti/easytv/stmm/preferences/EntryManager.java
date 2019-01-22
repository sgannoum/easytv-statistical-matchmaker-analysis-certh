package com.certh.iti.easytv.stmm.preferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class EntryManager {

	public enum EntryType {
		Undefined, Number, Enumeration;
	};

	private static HashMap<String, List<String>> _EnumerationValues = new HashMap<String, List<String>>();
	private static HashMap<String, EntryType> _EntryTypes =  new HashMap<String, EntryType>();
	private static HashMap<String, Double> _EntryMins = new HashMap<String, Double>();
	private static HashMap<String, Double> _EntryMaxs = new HashMap<String, Double>();
	private static HashMap<String, String> _EntryApps = new HashMap<String, String>();
	private static HashMap<String, String> _EntryPrintName = new HashMap<String, String>();

	public static double EntryMin(String name) {
		if (!_EntryMins.containsKey(name)) {
			return (double) 0;
		} else
			return _EntryMins.get(name);
	}

	public static double EntryMax(String name) {
		if (!_EntryMaxs.containsKey(name)) {
			return (double) 0;
		} else
			return _EntryMaxs.get(name);
	}

	public static int EnumerationSize(String name) {
		return _EnumerationValues.get(name).size();
	}

	public static Set<String> EntryNames() {
		return _EntryTypes.keySet();
	}

	public static String EntryApp(String name) {
		if (!_EntryApps.containsKey(name)) {
			return "";
		} else
			return _EntryApps.get(name);
	}

	public static String EntryPrintName(String name) {
		return _EntryPrintName.get(name);
	}

	public static String ToEnumeration(String name, Integer value) {
		return _EnumerationValues.get(name).get(value);
	}

	/**
	 * @brief Create an entry that represents the application name and associated value.
	 * 
	 * @param application
	 * @param name
	 * @param value
	 * @return
	 */
	public static Entry ToEntry(String application, String name, String value) {
		
		name = name.trim().replace("\"", "").replace("\t", "").replace("\\.", "");
		
		String internalName = application + "||" + name;
		_EntryApps.put(internalName, application);
		_EntryPrintName.put(internalName, name);
		Double number = null;
		EntryType entryType = EntryType.Undefined;
		
		try {
			number = Double.parseDouble(value);
		} catch(NumberFormatException ex) {
			number = null;
		}
		
		if(number != null) {
			if((entryType = _EntryTypes.get(internalName)) == null) {
                entryType = EntryType.Number;
                _EntryTypes.put(internalName, EntryType.Number);
			}
			
			if(entryType != EntryType.Number) {
			  //throw new IllegalStateException("Profile entry type \"" + internalName + "\" is not registered as a number, but value passed was \"" + number + "\"");
				//return null;
			}
			
			return new Entry(application, internalName, number);
			
		} else {
			
			value = value.trim().replace("\t", "").replace("\"", "").toLowerCase();
			if((entryType = _EntryTypes.get(internalName)) == null) {
                entryType = EntryType.Enumeration;
                _EntryTypes.put(internalName, EntryType.Enumeration);
			}
			
			if(entryType != EntryType.Enumeration) {
				 //throw new IllegalStateException("Profile entry type \"" + internalName + "\" is not registered as a enumeration, but value passed was \"" + value + "\"");
				//return null;
			}
			
			List<String> enumerationValues = null;
			if((enumerationValues = _EnumerationValues.get(internalName)) == null) {
				enumerationValues = new ArrayList<String>();
				_EnumerationValues.put(internalName, enumerationValues);
			}
			
			int index = 0;
			Iterator<String> enumerationIter = enumerationValues.iterator();
			for(; enumerationIter.hasNext() ; index++) 
				if(enumerationIter.next().equals(value)) 
					return new Entry(application, internalName, index);
			
			enumerationValues.add(value);

			return new Entry(application, internalName, index);
		}
		
	}

	public static boolean IsEnumeration(Entry entry) {
		return IsEnumeration(entry.get_Name());
	}

	public static boolean IsEnumeration(String name) {
		return _EntryTypes.get(name) == EntryType.Enumeration;
	}
	
	/**
	 * @brief Get the minimum and maximum value of each profile attribute. 
	 * 
	 * @param profiles
	 */
	public static void GetMinsAndMaxs(List<Profile> profiles) {
		Double value;
		
		Iterator<Profile> iterator = profiles.iterator();
		while (iterator.hasNext()) {
			Profile profile = iterator.next();

			//Analyze profile's preference set 
			Iterator<java.util.Map.Entry<String, Entry>> preferenceIterator = profile.PreferenceEntries.entrySet().iterator();
			while (preferenceIterator.hasNext()) {
				java.util.Map.Entry<String, Entry> pair = preferenceIterator.next();
				
				if (IsEnumeration(pair.getKey())) 
					continue;
				
				if ((value = _EntryMins.get(pair.getKey())) == null || value > ((Entry) pair.getValue()).get_Value()) {
					_EntryMins.put(pair.getKey(), ((Entry) pair.getValue()).get_Value());
				}
				if ((value = _EntryMaxs.get(pair.getKey())) == null || value < ((Entry) pair.getValue()).get_Value()) {
					_EntryMaxs.put(pair.getKey(), ((Entry) pair.getValue()).get_Value());
				}
			}

			//Analyze profile's context set 
			Iterator<java.util.Map.Entry<String, Entry>> contextIterator = profile.ContextEntries.entrySet().iterator();
			while (contextIterator.hasNext()) {
				java.util.Map.Entry<String, Entry> pair = contextIterator.next();
				
				if (IsEnumeration(pair.getKey())) 
					continue;
				
				if ((value = _EntryMins.get(pair.getKey())) == null || value > ((Entry) pair.getValue()).get_Value()) {
					_EntryMins.put(pair.getKey(), ((Entry) pair.getValue()).get_Value());
				}
				if ((value = _EntryMaxs.get(pair.getKey())) == null || value < ((Entry) pair.getValue()).get_Value()) {
					_EntryMaxs.put(pair.getKey(), ((Entry) pair.getValue()).get_Value());
				}
			}
		}
	}

	/**
	 * @brief Normalize the value of the given entry.
	 * 
	 * @param entry
	 * @return A new entry that holds the normalized value
	 */
	public static Entry NormalizeNumber(Entry entry) {
		if (IsEnumeration(entry))
			return entry;
		
		double max = _EntryMaxs.get(entry.get_Name()).doubleValue();
		double min = _EntryMins.get(entry.get_Name()).doubleValue();

		if (max == min) {
			return new Entry(entry.get_Application(), entry.get_Name(), 0.0);
		} else {
			return new Entry(entry.get_Application(), entry.get_Name(),
					 (entry.get_Value() - min) / (max - min));
		}
	}

}
