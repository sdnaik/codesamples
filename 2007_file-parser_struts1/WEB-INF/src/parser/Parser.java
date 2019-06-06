package parser;

import java.util.*;

import sys.*;
import org.apache.struts.util.*;
import org.apache.commons.logging.*;

public class Parser {

	public Parser() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static Map parse(String dataString, ConfigFile configFile){
		Log log = LogFactory.getLog(Parser.class);
		Map dataMap = new HashMap();
		
		//Set variables required to read parse-file
		ArrayList fields = configFile.getFields();
		ArrayList startPositions = configFile.getStartPositions();
		ArrayList lengths = configFile.getLengths();				 			

		//Set number of fields
		int numOfFields = fields.size();

		Iterator field = fields.iterator();
		Iterator pos = startPositions.iterator();
		Iterator len = lengths.iterator();
		
		String fieldName = "";
		String fieldValue = "";
		
					
		while (field.hasNext()){
			fieldName = (String) field.next(); 
			fieldValue = dataString.substring(((Integer)pos.next()).intValue(),((Integer)len.next()).intValue());
			dataMap.put(fieldName,fieldValue);
			} 			
		
		return dataMap;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
