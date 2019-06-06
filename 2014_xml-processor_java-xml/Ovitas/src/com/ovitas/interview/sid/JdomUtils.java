package com.ovitas.interview.sid;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format;


/**
 * A utility class that provides utility methods to easily work with Jdom library 
 * 
 * @author sid naik
 *
 */
public class JdomUtils {

	/**
	 * Builds an xml Document object from an xml file
	 * 
	 * @param file	a File object
	 * @return		an xml Document object
	 */
	public static Document getDocument(File file) {
		SAXBuilder builder = new SAXBuilder();
		Document xmlDoc = null;
		if(file != null) {
			try {
				xmlDoc = builder.build(file);
			} catch (JDOMException | IOException e){
				e.printStackTrace();
			}			
		}
		return xmlDoc;
	}
	
	
	/**
	 * Builds an xml Document object given the file name and path
	 * No need to handle errors here since it's already handled in the methods that are called here
	 * 
	 * @param fileNameAndPath	a String representing xml file name and path
	 * @return					an xml Document object
	 */
	public static Document getDocument(String fileNameAndPath){
		Document document = null;
		File file = FileUtils.getValidFile(fileNameAndPath);
		if (file != null){
			document = getDocument(file);
		}
		return document;
	}
	
	
	
	/**
	 * Generates xml files 
	 * 
	 * @param xmlDoc			an xml Document object
	 * @param fileNameAndPath   a String representing output file name and path
	 */
	public static void printDocument(Document xmlDoc, String fileNameAndPath) {
		XMLOutputter xmlOutput = new XMLOutputter();		 
		xmlOutput.setFormat(Format.getPrettyFormat());
		try {
			xmlOutput.output(xmlDoc, new FileWriter(fileNameAndPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}
