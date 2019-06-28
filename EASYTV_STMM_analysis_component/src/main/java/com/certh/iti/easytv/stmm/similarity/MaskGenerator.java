package com.certh.iti.easytv.stmm.similarity;

public class MaskGenerator {
	
	public static long getMask(final String[] uris, String category) {
		long mask = 0;
		for(int i = 0; i < uris.length; i++) {
			String uri = uris[i];
			if(uri.contains(category)) 
				mask |= (long) Math.pow(2, i);
		}
		return mask;
	}
	
	public static long getMask(final String[] uris, String[] categories) {
		long mask = 0;
		for(int i = 0; i < uris.length; i++) {
			String uri = uris[i];
			for(String category : categories)
				if(uri.contains(category)) {
					mask |= (long) Math.pow(2, i);
					break;
			}
		}
		return mask;
	}

}
