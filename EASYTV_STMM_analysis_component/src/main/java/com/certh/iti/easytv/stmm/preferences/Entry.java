package com.certh.iti.easytv.stmm.preferences;

public class Entry {
	
	private String _Name;
	private String _Application;
	private double _Value;
	
	public Entry(String application, String name, double value) {
		set_Name(name);
		set_Application(application);
		set_Value(value);
	}

	public String get_Name() {
		return _Name;
	}

	public void set_Name(String _Name) {
		this._Name = _Name;
	}

	public String get_Application() {
		return _Application;
	}

	public void set_Application(String _Application) {
		this._Application = _Application;
	}

	public double get_Value() {
		return _Value;
	}

	public void set_Value(double _Value) {
		this._Value = _Value;
	}
	
	public double NormalizedValue() {
		return EntryManager.NormalizeNumber(this).get_Value();
	}
	
	@Override
	public String toString() {
		return _Name+" : "+ _Value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!Entry.class.isInstance(obj)) return false;
		
		Entry other = (Entry) obj;
		if(!other._Application.equals(this._Application)) return false;
		if(!other._Name.equals(this._Name)) return false;
		if(other._Value != this._Value) return false;
		
		return true;	
	}
}
