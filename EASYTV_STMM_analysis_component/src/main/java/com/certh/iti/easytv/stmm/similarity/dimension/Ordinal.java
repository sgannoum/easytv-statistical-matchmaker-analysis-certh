package com.certh.iti.easytv.stmm.similarity.dimension;

public class Ordinal extends Numeric{

	private int m;
	
	public Ordinal(int m, double max, double min) {
		super(max, min);
		this.m = m;
	}

	
	@Override
	public double[] dissimilarity(double a, double b) {	
		
		double za = (a - 1) / (m - 1);
		double zb = (b - 1) / (m - 1);

		return super.dissimilarity(za, zb);
	}

}
