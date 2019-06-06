/**
 * Purpose:
 * This file reads configuration file from ConfigFiles directory
 * Configuration is saved in ConfigFileBean
 *
 *  Author:
 *  Sid Naik
 */

package sys;

import java.io.*;
import java.util.*;
import org.apache.struts.util.ModuleException;
import org.apache.commons.logging.*;

public class ConfigFileReader {

	private String parseFileType;

	public ConfigFileReader (String parseFileType){
		this.parseFileType = parseFileType;
	}

	public ConfigFileReader (){
	}

	public static ConfigFile readConfigFile(String parseFileType) throws ModuleException {

		Log log = LogFactory.getLog(ConfigFileReader.class);

		//Initialize variables
		String configFileName = "";
		BufferedReader in = null;
		String lineread = "";
		ConfigFile cfb = new ConfigFile();
		ArrayList fields = new ArrayList();
		ArrayList startPositions = new ArrayList();
		ArrayList lengths = new ArrayList();

		//Get the config file for the given parseFileType
		/** To Do: Validate if config file name and path exists **/
		configFileName="D:/webapps/PARSER/ConfigFiles/Marketing.txt";
		File configFile = new File(configFileName);
		try {
			if (configFile.exists() && configFile.canRead()){
				in = new BufferedReader(new FileReader(configFileName));
				//Loop through config file reader and set variables
				int line = 0;
				lines:
				while ((lineread = in.readLine()) != null){
					line = line + 1;
					//Skip comments in config file
					if (lineread.contains("#"))continue lines;
					//Key and value are separated by "="
					//If there is no key-value pair available, throw error
					if (lineread.contains("=")){
						int index = lineread.indexOf("=");
						String param = lineread.substring(0,index);
						String value = lineread.substring(index+1,lineread.length()).trim();
						//Get values of all parameters for parsing the file
						if(param.equals("FileStart")){cfb.setFileStart(value);continue lines;}
						if(param.equals("FieldStart")){cfb.setFieldStart(value);continue lines;}
						if(param.equals("FileEnd")){cfb.setFileEnd(value);continue lines; }
						if(param.equals("FieldEnd")){cfb.setFieldEnd(value);continue lines;}
						if(param.equals("NumOfHeaderLines")){cfb.setNumOfHeaderLines(Integer.parseInt(value));continue lines;}
						if(param.equals("NumOfFooterLines")){cfb.setNumOfFooterLines(Integer.parseInt(value));continue lines;}
						if(param.equals("Delimitor")){cfb.setDelimitor(value); continue lines;}
						//There are many fields and each field has multiple attributes separated by comma
						if(param.equals("Field")){
							 StringTokenizer tokens = new StringTokenizer(value, ",");
							 //Make sure we have all attributes for the field
							 if (tokens.countTokens()!= 3) {
									log.error("All attributes for the field could not be retrieved at line" + line);
									ModuleException me = new ModuleException("error.configfile.module.missingvalues");
									throw me;
							 }
							 else {
								 String field = tokens.nextToken();
								 cfb.addField(field);
								 String startPosition = tokens.nextToken();
								 cfb.addStartPosition(Integer.valueOf(startPosition));
								 String length = tokens.nextToken();
								 cfb.addLength(Integer.valueOf(length));
								 continue lines;
							 }
						}
					}
					else {
						log.error("No key-value pair found in the config file at line" + line);
						ModuleException me = new ModuleException("error.configfile.module.nokeyvaluepair");
						throw me;
					}
				}
			}
			else {
				log.error("Could not find config file");
				ModuleException me = new ModuleException("error.configfile.module.filenotfound");
				throw me;
			}
		}
		catch (IOException e){
			log.error("File input or output error while reading config file");
			e.printStackTrace();
			ModuleException me = new ModuleException("error.configfile.module.ioparse");
			throw me;
		}
		finally {
			try {
				if (in!=null)in.close();
			}
			catch (IOException e1){
				log.error("Error while closing config file BuferredReader");
				e1.printStackTrace();
				ModuleException me = new ModuleException("error.configfile.module.ioreader");
				throw me;
			}
		}
		return cfb;
	}

	//Unit testing
	/*
	public static void main(String[] args)throws ModuleException {
		ConfigFileReader fcr = new ConfigFileReader();
		ConfigFileBean cfb = fcr.readConfigFile("USAAMarketing");
		 System.out.println(cfb.getFileStart());
		 System.out.println(cfb.getFieldStart());
		 System.out.println(cfb.getFileEnd());
		 System.out.println(cfb.getFieldEnd());
		 System.out.println(cfb.getNumOfHeaderLines());
		 System.out.println(cfb.getNumOfFooterLines());
		 System.out.println(cfb.getDelimitor());
		 System.out.println(cfb.getFields());
		 System.out.println(cfb.getStartPositions());
		 System.out.println(cfb.getLengths());
	}
	*/
}
