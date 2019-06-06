package com.ovitas.interview.sid;

import java.io.File;
import java.io.FilenameFilter;

/**
 * This class filters out any files other than xml files
 * 
 * @author sid naik
 *
 */
public class XmlFilenameFilter implements FilenameFilter {
	
	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File directory, String fileName){		
		boolean valid = false;
		String lowerCaseFileName = fileName.toLowerCase();
		if (lowerCaseFileName.endsWith(".xml")){
			valid = true;
		}
		return valid;			
	}

}
