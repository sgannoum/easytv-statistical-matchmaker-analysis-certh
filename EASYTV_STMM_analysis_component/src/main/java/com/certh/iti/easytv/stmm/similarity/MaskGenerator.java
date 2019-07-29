package com.certh.iti.easytv.stmm.similarity;

import com.certh.iti.easytv.user.preference.attributes.Attribute;

public class MaskGenerator {
		
	/**
	 * 
	 * @param uris
	 * @param categories
	 * @return a mask of the specified categories
	 */
	public static long getMask(final String[] uris, final Attribute[] attributes, String[] categories) {
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
	
	/**
	 * @param mask
	 * @return the number of ones in the mask
	 */
	public static int getBits(long mask) {
		int cindex = 0;
		long tmpMask = mask;
		
		do {
			if((tmpMask & 1L) == 1) 
				cindex++;
			
		} while((tmpMask /=2) != 0.0);
		
		return cindex;
	}

}
