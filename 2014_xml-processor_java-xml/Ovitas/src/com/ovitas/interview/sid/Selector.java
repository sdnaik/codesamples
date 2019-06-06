package com.ovitas.interview.sid;

/**
 * This class encapsulates all the request criteria for processing xml files
 * 
 * @author sid naik
 *
 */
public class Selector {

	private String inputPath;
	private String outputPath; 
	private String tagName;
	private String searchRegex; 
	private String replacementString;
	

	Selector(){
		super();
	}
	
	/**
	 * @param inputPath 			a String specifying the path and name of the directory where all input xml files reside 
	 * @param outputPath			a String specifying the path and name of the directory where all output xml files reside
	 * @param tagName				a String specifying the xml tag under which the word should be searched
	 * @param searchRegex			a regular expression specifying the search string
	 * @param replacementString		a String specifying the replacement word
	 * @return						an object of the Selector class that encacpsulates all search criteria
	 */
	Selector (String inputPath, String outputPath, String tagName, String searchRegex, String replacementString){
		super();
		setInputPath(inputPath);
		setOutputPath(outputPath);
		setTagName(tagName);
		setSearchRegex(searchRegex);
		setReplacementString(replacementString);		
	}
	
	public String getOutputPath() {
		return outputPath;
	}
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getSearchRegex() {
		return searchRegex;
	}
	public void setSearchRegex(String searchRegex) {
		this.searchRegex = searchRegex;
	}
	public String getReplacementString() {
		return replacementString;
	}
	public void setReplacementString(String replacementString) {
		this.replacementString = replacementString;
	}
	public String getInputPath() {
		return inputPath;
	}
	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}
	
}
