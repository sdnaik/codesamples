package parser;

import java.util.*;
import java.io.*;

public class Converter {

	public Converter() {
		super();
		// TODO Auto-generated constructor stub
	}

	//Converts BufferReader input to ArrayList output
	public static List toList(BufferedReader brData)throws IOException {
		List dataLines = new ArrayList();
		String dataLine;
		while (( dataLine = brData.readLine()) != null){
			dataLines.add(dataLine);
		}
		return dataLines;
	}
	
	public static List toList(String strData){
		List dataLines = new ArrayList();
		String dataLine;
		while (strData.length()>1 ){
			int i = strData.indexOf("\n");
			if (i > 0) {
				dataLines.add(strData.substring(0,i));
				strData = strData.substring(i+1);
			}
			else {
				dataLines.add(strData);
				strData = "";
			}	
		}
		return dataLines;
	}
	
	/**
	 * @param args
	 */
	/*
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
*/
}
