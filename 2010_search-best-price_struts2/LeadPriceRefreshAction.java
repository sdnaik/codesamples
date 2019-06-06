package com.nlg.web.action.promo;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.nlg.common.Tools;
import com.nlg.common.logging.Log;
import com.nlg.web.common.util.SystemInfo;
import com.nlg.common.util.HttpTransportClient;
import com.nlg.common.util.WthHttpRequest;
import com.nlg.common.util.WthHttpResponse;

import com.nlg.web.action.GenericAction;
import com.nlg.web.content.CmsContentManager;
import com.nlg.services.siteconfig.dto.SiteTemplateDTO;

/**
 * This class is a swap job for refreshing lead prices in table tlcs.leadprice
 * 
 * It does the following things:
 * (1) Verify that the request is not a duplicate
 * (2) Pull the list of promo pages where lead prices are defined from Magnolia
 * (3) For each promo page, call LeadPriceProcessor to refresh lead prices 
 * 
 * @author snaik
 *
 */
public class LeadPriceRefreshAction extends GenericAction {

	private static final long serialVersionUID = 1L;
	private static final Log log = Log.getLog(LeadPriceRefreshAction.class);
	//Set constants for Magnolia related items
	private static final String ROOTNODE =  "Creative";
	private static final String LEAD_PRICE_START_FLAG = "lead-price-start:";
	private static final String LEAD_PRICE_END_FLAG = ":lead-price-end";
	private static final String LEAD_PRICE_NODE = "/leadprice/";
	//static variable to determine that the request is not duplicate
	private static Date lastRun = null;
	//the job won't run until these many manutes after last run
	
	private List<String> promoResponses = new ArrayList<String>();//holds the response code and promo page url
	private String message = "";//holds the message about the success or failure of promo page processing
	private static boolean overrideTimeout = false;
	
	public void doPageAction(SiteTemplateDTO data){
		if(!LeadPriceRefreshAction.isDuplicateRequest()){
			try{
				CmsContentManager cmsContentManager = new CmsContentManager();
				//The list of promo pages to be refreshed for lead price must be at this path in Magnolia
				String cmsContent = (String)cmsContentManager.getValue(ROOTNODE + LEAD_PRICE_NODE);
				//Pick up data only between start and end mark ups to avoid global content if any from Magnolia
				cmsContent = StringUtils.substringBetween(cmsContent, LEAD_PRICE_START_FLAG, LEAD_PRICE_END_FLAG);
				cmsContent = StringUtils.deleteWhitespace(cmsContent);
				log.debug("Lead price will be pulled for pages : " + cmsContent);
				String[] promoPages =  StringUtils.split(cmsContent, ",");
				if(promoPages!= null && promoPages.length > 0){
					/*
					 * Lead prices are calculated during http request of promo page 
					 * So create a http request for calling the promo page 
					 */
					HttpTransportClient httpClient = new HttpTransportClient(SystemInfo.getProperty("wth.swap.job.server.ip"), Integer.parseInt(SystemInfo.getProperty("wth.swap.job.server.port")));
					WthHttpRequest request = new WthHttpRequest();
					/*
					 * Since we have the same class (LeadPriceProcessor) and methods for pulling lead price
					 * from database and inserting into database, we need a flag to determine 
					 * when to pull lead price and when to actually calculate it. 
					 * This way, we won't calculate lead prices during promo page requests by web users.
					 */ 
					Map<String,String> queryString = new HashMap<String,String>();
					queryString.put("refreshLeadPrice", "true");
					request.setQueryString(queryString);
					/*
					 * Since this action will be called through a background job, 
					 * we can allow it to run longer for processing many lead prices
					 */
					httpClient.setTimeout(SystemInfo.getIntProperty("wth.swap.job.leadprice.timeout",3600000));//default an hour
					httpClient.setSocketTimeout(SystemInfo.getIntProperty("wth.swap.job.leadprice.socketTimeout",3600000));//default an hour
					request.setHttpMethod("GET");
					for(String page : promoPages){
						if(!Tools.isEmpty(page)){
							log.debug("Now processing promo page: " + page);
							//Since the product collection varies by domain, we need entire URL
							String virtualHost = StringUtils.substringBetween(page, "http://","/");
							log.debug("lead price: virtual host for pulling promo page: " + virtualHost);
							request.setVirtualHost(virtualHost);
							/*
							 * As an additional security, we will call promo pages for lead price refresh 
							 * only through /job/promotion. This way lead price will not be acidently 
							 * refreshed during normal promo execution through /promotion
							 */
							String targetFile = SystemInfo.getProperty("wth.swap.job.path") + StringUtils.remove(page, "http://" + virtualHost);
							log.debug("lead price: target file for pulling promo page: " + targetFile);
							request.setTargetFile(targetFile);
							log.debug("lead price: promo page host: " + httpClient.getHost());
							log.debug("lead price: promo page port: " + httpClient.getPort());
							WthHttpResponse response = httpClient.execute(request);
							int statusCode = response.getStatusCode();
							String responseMessage = statusCode + " : " + page;	
							promoResponses.add(responseMessage);
						}
					}				
				}
				/*
				 * Since we don't allow promo pages to fail because of lead prices,
				 * the status code will be mostly 200 unless promo page is not reachable
				 */
				setMessage("Lead price refresh job completed! " +
						"Here is the HTTP status code of the promo pages for which lead price was updated:");
				setPromoResponses(promoResponses);
				log.info(this.message);
				for(String infoResponse : promoResponses){
					log.info(infoResponse);
				}
			}
			catch(Exception e){
				setMessage("Error occurred in lead price refresh job! " +
						"Here is the HTTP status code of the promo pages for which lead price was updated:");
				log.error("Lead Price Error: lead price could not be refreshed via background job! " + 
						"Here is the HTTP status code of the promo pages for which lead price was updated:",e);
				for(String infoResponse : promoResponses){
					log.error(infoResponse);
				}				
			}	 
			LeadPriceRefreshAction.lastRun = new Date();
		}
		else{
			setMessage("The job is already in process. Please try after " + SystemInfo.getProperty("wth.swap.job.leadprice.dupReqMinutes") + " minutes.");
			log.error("Lead Price Error: duplicate request was initiated");
		}
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getPromoResponses() {
		return promoResponses;
	}

	public void setPromoResponses(List<String> promoResponses) {
		this.promoResponses = promoResponses;
	}
	
	public static boolean isOverrideTimeout() {
		return overrideTimeout;
	}

	public static void setOverrideTimeout(boolean overrideTimeout) {
		LeadPriceRefreshAction.overrideTimeout = overrideTimeout;
	}
	
	public static boolean isDuplicateRequest(){
		boolean isDup = false;
		if(!isOverrideTimeout()){
			Date now = new Date();
			if(lastRun!=null){
				long diff = now.getTime() - lastRun.getTime();//difference of time in millisecond
				long dupReqMinutes = Integer.valueOf(SystemInfo.getProperty("wth.swap.job.leadprice.dupReqMinutes")) * 60 * 1000;
				if(diff < dupReqMinutes){
					isDup = true;
				}
			}			
		}
		setOverrideTimeout(false);
		return isDup;
	}
	
}
