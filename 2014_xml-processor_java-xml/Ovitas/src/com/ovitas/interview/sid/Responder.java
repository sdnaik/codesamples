package com.ovitas.interview.sid;

/**
 * This class encapsulates all the data required in the output report
 * 
 * @author sid naik
 *
 */
public class Responder {

	private String fileName;
	private int foundCount;
	
	Responder(){
		super();
	}
	
	/**
	 * @param fileName		a String representing the file name
	 * @param foundCount	an int representing the count of found word in given file
	 */
	Responder(String fileName, int foundCount){
		super();
		setFileName(fileName);
		setFoundCount(foundCount);
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getFoundCount() {
		return foundCount;
	}
	public void setFoundCount(int foundCount) {
		this.foundCount = foundCount;
	}
	
	public void addFoundCount(){
		foundCount++;
	}

	
	
	
}
