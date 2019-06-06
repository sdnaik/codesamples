package sys;

import javax.servlet.*;
import java.util.Map;
import java.util.HashMap;
import org.apache.struts.util.ModuleException;
import org.apache.commons.logging.*;

public class ConfigFileContextListener implements ServletContextListener {

	public ConfigFileContextListener() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void contextInitialized (ServletContextEvent event){
		ServletContext sc = event.getServletContext();
		Map configFiles = new HashMap();
		try {
			configFiles.put("MarketingFile", ConfigFileReader.readConfigFile("MarketingFile"));
		}
		catch (ModuleException me){

		}
		sc.setAttribute("configFiles", configFiles);
	}

	public void contextDestroyed (ServletContextEvent event){
		ServletContext sc = event.getServletContext();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
