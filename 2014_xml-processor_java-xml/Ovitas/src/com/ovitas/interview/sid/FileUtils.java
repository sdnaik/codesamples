package com.ovitas.interview.sid;

import java.io.File;

/**
 * A utility class that provides utility methods for working with files
 * 
 * @author sid naik
 *
 */
public class FileUtils {


	/**
	 * Returns a valid directory that is readable
	 * 
	 * @param directoryPath		a String representing the path and name of the directory
	 * @return					a File object that's a directory
	 */
	public static File getValidDirectory(String directoryPath) {
		File validDirectory = null;
		if(directoryPath != null && !directoryPath.trim().isEmpty()) {
			File directory = new File(directoryPath);
			if(isValidDirectory(directory)){
				validDirectory = directory;
			} 
		}	
		return validDirectory;
	}

	
	/**
	 * Returns a valid file that is readable
	 * 
	 * @param filePath	a String representing the file name and path
	 * @return			a File object
	 */
	public static File getValidFile(String filePath){
		File validFile = null;
		if(filePath != null && !filePath.trim().isEmpty()) {
			File file = new File(filePath);
			if(isValidFile(file)){
				validFile = file;
			} 
		}
		return validFile;
	}


	/**
	 * Validates a directory for existence and readability
	 * 
	 * @param directory		a File object that's a directory
	 * @return				a boolean which is true if the directory exists and is readable
	 */
	public static boolean isValidDirectory (File directory){		
		boolean valid = false;				
		if (directory!= null && directory.exists() && directory.isDirectory()) {
			//System.out.println("Directory exists");
			if(directory.canRead()) {
				valid = true;
				//System.out.println("Directory is readable");				
			}
			else {
				System.out.println("Directory is not readable");
			}
		}
		else {
			System.out.println("Directory does not exist");
		}	
		return valid;
	}
	

	/**
	 * Validates a file for existence and readability
	 * 
	 * @param file		a File object that's a file
	 * @return			a boolean which is true if the file exists and is readable
	 */	
	public static boolean isValidFile(File file){
		boolean valid = false;
		if(file !=null && file.exists() && file.isFile()){
			//System.out.println("File exists");
			if(file.canRead()) {
				valid = true;
				//System.out.println("File is readable");				
			}
			else {
				System.out.println("File is not readable");
			}
		}		
		else {
			System.out.println("File does not exist");
		}				
		return valid;
	}

	
	/**
	 * Returns an array of xml file name strings (doesn't include the path).
	 * This method filters out any files other than the xml files using XmlFilenameFilter.
	 * 
	 * @param directoryPath		a String representing the directory path
	 * @return					an array of String with xml file names 
	 */
	public static String[] getXmlFileNames (String directoryPath){
		String[] xmlFileNames = null;
		File directory = getValidDirectory(directoryPath);
		if(directory != null){			
			xmlFileNames = directory.list(new XmlFilenameFilter());
		}
		return xmlFileNames;
	}	
	
}
