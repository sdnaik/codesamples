/**
 * Purpose:
 * This file represents the configuration file
 * ConfigFileReader will read the configuration file and save the data to this object
 * 
 * Author:
 * Sid Naik
*/

package sys;

import java.util.*;
import sys.*;

public class ConfigFile {

	private String fileStart;
	private String fieldStart;
	private String fileEnd;
	private String fieldEnd;
	private int numOfHeaderLines;
	private int numOfFooterLines;
	private String delimitor;
	private ArrayList fields = new ArrayList();
	private ArrayList startPositions = new ArrayList();
	private ArrayList lengths = new ArrayList();

	public ConfigFile	(String fileStart,String fieldStart,
							 String fileEnd, String fieldEnd,
							 int numOfHeaderLines,int numOfFooterLines,
							 String delimitor, ArrayList fields,
							 ArrayList startPositions, 
							 ArrayList lengths
							){
		this.fileStart = fileStart;
		this.fieldStart = fieldStart;
		this.fileEnd = fileEnd;
		this.fieldEnd = fieldEnd;
		this.numOfHeaderLines = numOfHeaderLines;
		this.numOfFooterLines = numOfFooterLines;
		this.delimitor = delimitor;
		this.fields = fields;
		this.startPositions = startPositions;
		this.lengths = lengths;
	}
	
	public ConfigFile(){

	}

	public String getFileStart() {
		return fileStart;
	}

	public void setFileStart(String string){
		fileStart = string;
	}
	
	public String getFieldStart() {
		return fieldStart;
	}

	public void setFieldStart(String string){
		fieldStart = string;
	}
	
	public String getFileEnd() {
		return fileEnd;
	}

	public void setFileEnd(String string){
		fileEnd = string;
	}
	
	public String getFieldEnd() {
		return fieldEnd;
	}

	public void setFieldEnd(String string){
		fieldEnd = string;
	}

	public int getNumOfHeaderLines() {
		return numOfHeaderLines;
	}

	public void setNumOfHeaderLines(int i){
		numOfHeaderLines = i;
	}
	
	public int getNumOfFooterLines() {
		return numOfFooterLines;
	}

	public void setNumOfFooterLines(int i){
		numOfFooterLines = i;
	}
	
	public String getDelimitor() {
		return delimitor;
	}

	public void setDelimitor(String string){
		delimitor = string;
	}
	
	public ArrayList getFields (){
		return fields;
	}
	
	public void addField(String string){
		fields.add(string);
	}
	
	public ArrayList getStartPositions (){
		return startPositions;
	}
	
	public void addStartPosition(Integer i){
		startPositions.add(i);
	}

	public ArrayList getLengths (){
		return lengths;
	}
	
	public void addLength(Integer i){
		lengths.add(i);
	}
	
}
