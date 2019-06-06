/**
 * Purpose:
 * This file serves as a controller
 * It receives the request, calls business methods and forwards the output
 * Extending Action class is required for application based on struts framework
 *
 * Author:
 * Sid Naik
 */

package parser;

import sys.*;
import java.io.*;
import java.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.*;
import org.apache.commons.logging.*;

public class ParseAction extends Action {
	Log log = LogFactory.getLog(ParseAction.class);


	public ActionForward execute (	ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response
									)throws Exception {

		ActionErrors errors = new ActionErrors();


	//public void doPost (HttpServletRequest request,HttpServletResponse response) throws ServletException {
		ServletConfig config = getServlet().getServletConfig();
		ServletContext sc = config.getServletContext();

		//Get form values
		FormFile parseFile = (FormFile)((DynaValidatorForm)form).get("parsefile");
		String parseFileType = (String)((DynaValidatorForm)form).get("parsefiletype");
		String parseData = (String)((DynaValidatorForm)form).get("parsedata");

		//Server side validation in addition to validation.xml
		if ( parseFile.getFileName()== "" & parseData == "" ) {
			ActionError error = new ActionError("error.parse.required");
			errors.add("parse", error);
			saveErrors(request, errors);
		}

		//Get the data to be parsed in an ArrayList
		ArrayList parseStrings = new ArrayList();
		if (parseFile.getFileName().length()> 1) {
			InputStream inStream = parseFile.getInputStream();
			InputStreamReader inStrReader = new InputStreamReader (inStream);
			BufferedReader br = new BufferedReader(inStrReader);
			parseStrings = (ArrayList) Converter.toList(br);
		}
 		else if (parseData.length()> 1) {
 			log.error("Reached for processing");
 			parseStrings = (ArrayList)Converter.toList(parseData);
 		}
		else {
			ActionError error = new ActionError("error.parse.invalid");
			errors.add("parse", error);
			saveErrors(request, errors);
			return mapping.findForward("failure");
		}
		ArrayList arlParsedFile = new ArrayList();

		ConfigFile configFile = (ConfigFile)((HashMap)sc.getAttribute("configFiles")).get("MarketingFile");
		ArrayList fieldNames = new ArrayList();
		fieldNames = configFile.getFields();
		arlParsedFile.add(fieldNames);
		Iterator dataString = parseStrings.iterator();
		//Call business class method

		while (dataString.hasNext()) {
			//Instantiate business classes
			//ParseFileReader pfr = new ParseFileReader();
			//ParseFileWriter pfw = new ParseFileWriter();

			//Call business class methods
			//Read the file and get the ArrayList object
			HashMap hm = (HashMap)Parser.parse((String)dataString.next(),configFile );
			Iterator fld = fieldNames.iterator();
			ArrayList filedValues = new ArrayList();
			while (fld.hasNext()){
				filedValues.add(hm.get(fld.next()));
			}
			arlParsedFile.add(filedValues);
			//ArrayList arlParsedFile = pfr.parseFile(parseFileStream,parseFileType);

			//Pass the ArrayList object and write to a CSV file
			//String strParsedFile = pfw.writeToFile(arlParsedFile);

			//Set output variables
			//ArrayList object for displaying results on web

			//File name for users to download the parsed file in CSV
			//request.setAttribute("strParsedFile", strParsedFile);

			//Forward the request
		}

		//log.error("Reached to end");
		//log.error("The size =" +  parseStrings.size() + "Array Data = " + parseStrings.get(1) + parseStrings.get(2));
		//request.setAttribute("strParsedFile", parseStrings);
		request.setAttribute("arlParsedFile", arlParsedFile);
		//response.setContentType("text/html");
		//PrintWriter out = response.getWriter();
		//out.println("Here is the output from struts action");
		return mapping.findForward("success");
		//return null;
	}
}