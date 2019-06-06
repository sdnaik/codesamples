package com.ovitas.interview.sid;

import java.util.List;
import java.util.ArrayList;

/**
 * This is the main class that process the content. 
 * Based on the selection criteria encapsulated in selector object, the program:
 * (1) validates input directory
 * (2) identifies the files in the source folder
 * (3) searches for the specific text
 * (4) replaces the text
 * (5) saves the output files in a new folder
 * (6) generates a report of what was found and replaced 
 * 
 * @author sid naik
 *
 */

public class ContentProcessor {
	
	/**
	 * Process the content from the files in the input folder
	 * 
	 * @param selector a Selector object that encapsulates the search criteria
	 */
	public static void processContent(Selector selector){
		if(selector !=null && selector.getInputPath() != null){
			//Pull all xml file names in an array
			String[] xmlFileNames = FileUtils.getXmlFileNames(selector.getInputPath());
			if(xmlFileNames!=null && xmlFileNames.length > 0){
				//Initialize a list of responses that will be used to print the report
				List<Responder> responders = new ArrayList<Responder>();
				//Iterate through each file in the input folder
				for(String xmlFileName : xmlFileNames){
					if(xmlFileName != null) {
						//System.out.println("");
						//System.out.println("Now processing " + xmlFileName);
						Responder responder = new Responder();
						responder.setFileName(xmlFileName);
						//Process each xml document
						DocumentProcessor documentProcessor = new DocumentProcessor(selector, responder);
						documentProcessor.processDocument();
						//Add the output to the ArrayList initialized earlier
						responders.add(responder);						
					}
				}
				//Iterate through each response and print the report
				System.out.println(" File Name  :  Total Count Of Found Word ");
				System.out.println("=========================================");
				System.out.println("");
				for (Responder responder : responders){
					if(responder.getFoundCount() > 0 ){
						System.out.println(responder.getFileName() + " : " + responder.getFoundCount());
					}
				}			
			} else {
				System.out.println("Xml files could not be found at the given location.");
			}			
		} else {
			System.out.println("Selection criteria not defined");
		}
		
	}
	
	
	//Unit Test
	public static void main(String[] args){
		Selector selector = new Selector("c:/Ovitas/input", "c:/Ovitas/output", "LINE", "[vV][iI][rR][tT][uU][eE]", "REPLACEMENT_STRING"); 
		processContent(selector);
	}
	
}
