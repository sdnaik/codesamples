package com.ovitas.interview.sid;

import org.jdom.Document;
import org.jdom.Element;
import java.util.List;
import java.util.regex.Pattern;
import java.io.File;

/**
 * This class processes the content in an xml document 
 * Based on the selection criteria encapsulated in selector object, the program:
 * (1) searches for the specific text in a recursive manner 
 * (2) stores the count in a responder object
 * (3) replaces the text
 * (4) saves the output file in a new folder
 *
 * @author sid naik
 *
 */
public class DocumentProcessor {
	
	private Selector selector;
	private Responder responder;
	private Document readDoc;
	private Document writeDoc;
	
	DocumentProcessor(Selector selector, Responder responder){
		setSelector(selector);
		setResponder(responder);
		setReadDoc(responder.getFileName());
		setWriteDoc(getReadDoc());
	}
	
	
	/**
	 * Processes the document
	 * Once done, save the output files in an output folder
	 */
	public void processDocument(){
		processElement(getWriteDoc().getRootElement());
		String fileNameAndPath = selector.getOutputPath() + "/" + responder.getFileName();
		JdomUtils.printDocument(this.writeDoc, fileNameAndPath );
	}
	
	
	
	/**
	 * Processes an element of the document in a recursive manner
	 * 
	 * @param element
	 */
	public void processElement(Element element){
		//Earlier version of Jdom didn't support generics 
		List list = element.getChildren();
		//iterate through each child of the element
		for (int i=0; i<list.size(); i++){
			Element currentElement = (Element)list.get(i);
			if(currentElement != null && currentElement.getName().equals(selector.getTagName())){
				String currentText = currentElement.getText();
				//use regex to search the word
				Pattern searchPattern = Pattern.compile(selector.getSearchRegex());
				if(searchPattern!= null && currentText != null && !currentText.trim().isEmpty() && searchPattern.matcher(currentText).find()){
					//System.out.println("Word matched: " + currentText);
					//If the word is found, increment the count
					responder.addFoundCount();
					String newText = searchPattern.matcher(currentText).replaceAll(selector.getReplacementString());
					currentElement.setText(newText);					
				}
			}
			//process other elements in a recursive manner
			processElement(currentElement);
		}
		
	}
	
	
	public Selector getSelector() {
		return selector;
	}


	public void setSelector(Selector selector) {
		this.selector = selector;
	}


	public Responder getResponder() {
		return responder;
	}


	public void setResponder(Responder responder) {
		this.responder = responder;
	}

	
	public Document getReadDoc() {
		return readDoc;
	}


	public void setReadDoc(String xmlFileName){
		File xmlFile = FileUtils.getValidFile(getSelector().getInputPath() + "/" + xmlFileName);
		this.readDoc = JdomUtils.getDocument(xmlFile);
	}

		
	public Document getWriteDoc(){
		return this.writeDoc;
	}

	
	public void setWriteDoc(Document readDoc) {
		this.writeDoc =  (Document) readDoc.clone();
	}
	

}
