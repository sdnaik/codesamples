package com.nlg.web.action.promo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Collections;
import java.util.Date;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.ext.beans.StringModel;
import freemarker.ext.beans.BeansWrapper;

import org.apache.commons.lang.StringUtils;

import com.nlg.common.logging.Log;
import com.nlg.common.Tools;
import com.nlg.web.common.util.SystemInfo;
import com.nlg.services.promotion.dto.LeadPriceDTO;
import com.nlg.services.cruise.dto.CruiseOutputSelectorDTO;
import com.nlg.services.cruise.dto.CruiseResponseDTO;
import com.nlg.services.cruise.dto.CruiseDTO;
import com.nlg.services.promotion.dto.LeadPriceRequestDTO;
import com.nlg.services.cruise.dto.CruiseSearchRequestDTO;
import com.nlg.services.cruise.dto.CruiseSearchDTO;
import com.nlg.services.siteconfig.dto.SiteConfigDTO;
import com.nlg.services.store.dto.StoreDTO;
import com.nlg.web.delegate.CruiseServiceDelegate;
import com.nlg.web.delegate.LeadPriceServiceDelegate;
import com.nlg.web.action.shop.helper.SearchCruiseHelper;
import com.nlg.services.cruise.dto.CruiseSearchSortItemDTO;
import com.nlg.services.currency.CurrencyExchange;
import com.nlg.web.model.SortItemGroup;
import com.nlg.web.model.CustomerPref;

/**
 * This class provides the lead price for the given link from table
 * tlcs.leadprice This same class is also used to refresh lead prices for search
 * links in the table by reusing cruise search related services. This class is
 * NOT used to refresh page-level lead prices for the promo page. promo
 * page-level lead prices are determined and refreshed in PromoAction.
 * 
 * The following conditions must be met in order to refresh the lead price (1)
 * refreshLeadPrice flag must be turned on (2) the call for refresh must be on
 * the path /job/ (handled through PromoAction) (3) the request shouldn't be
 * duplicate (handled through LeadPriceRefreshAction) (4) the search link
 * shouldn't be duplicate (5) the request must have come on designated server
 * (controlled through vixen webscheduler and NetScaler)
 * 
 * @author snaik
 */

public class LeadPriceProcessor implements TemplateMethodModel {

	private static final Log log = Log.getLog(LeadPriceProcessor.class);
	private static final String URL_DELIMITER = "&";
	private static final String PARAM_DELIMITER = "=";
	private SiteConfigDTO siteConfig;
	private boolean refreshLeadPrice;
	private int groupId;
	private List<BigDecimal> leadPrices = new ArrayList<BigDecimal>();

	public LeadPriceProcessor(SiteConfigDTO siteConfig, boolean refreshLeadPrice) {
		this.siteConfig = siteConfig;
		this.refreshLeadPrice = refreshLeadPrice;
		this.groupId = this.siteConfig.getStoreDTO().getGroupId();

	}

	// Top level method that will be called by Freemarker macro
	public TemplateModel exec(List args) {
		// Returning a blank String instead of a null value when lead price is
		// not found
		String result = "";
		BigDecimal leadPriceValue = getLeadPrice(args.get(0).toString());
		if (leadPriceValue != null) {
			result = leadPriceValue.toString();
		}
		BeansWrapper bw = new BeansWrapper();
		// No need to expose any methods of result object to Freemarker
		bw.setExposureLevel(BeansWrapper.EXPOSE_NOTHING);
		return new StringModel(result, bw);
	}

	public BigDecimal getLeadPrice(String link) {
		BigDecimal leadPriceValue = null;
		try {
			LeadPriceRequestDTO leadPriceRequest = createLeadPriceRequestDTO(link);
			String leadPriceKey = "";
			if (Tools.isEmpty(leadPriceRequest.getPromoPageLink())) {
				leadPriceKey = leadPriceRequest.getSearchResultsLink();
			} else {
				leadPriceKey = leadPriceRequest.getPromoPageLink();
			}

				// Get the lead price from in-memory cache (eHcache) or database
				LeadPriceDTO leadPriceDTO = getLeadPriceFromCache(leadPriceKey,this.groupId);
				if (leadPriceDTO != null && leadPriceDTO.getLeadPriceValue() != null) {
					leadPriceValue = leadPriceDTO.getLeadPriceValue();
				} else {
					LeadPriceDTO leadPriceDtoPromo = null;
					/*
					 * For promo page-level lead price if the specified link is
					 * not found in the database then select the lead price for
					 * default promo page link since in most cases the product
					 * colelction will be same as default page. This way we
					 * avoid having to specify each promo page link in Magnolia.
					 */

					boolean isPartner = false;
					String defaultPromoLink = null;

					if (isPromoPageLink(leadPriceKey)) {

						String homeAttValue = siteConfig.getStoreDTO().getAttributeMap().get("HBM").get(0).getAttributeValue();
						String houseAttvalue = siteConfig.getStoreDTO().getAttributeMap().get("HMG").get(0).getAttributeValue();
						isPartner = isPartner(homeAttValue, houseAttvalue);

						if (isPartner) {
							defaultPromoLink = getDefaultPromoLink(link, true);
							leadPriceDtoPromo = getLeadPriceFromCache(defaultPromoLink,Integer.parseInt(SystemInfo.getProperty("wth.default.groupId")));

							if (leadPriceDtoPromo != null && leadPriceDtoPromo.getLeadPriceValue() == null) {
								defaultPromoLink = getDefaultPromoLink(link,false);
								leadPriceDtoPromo = getLeadPriceFromCache(defaultPromoLink,Integer.parseInt(SystemInfo.getProperty("wth.default.groupId")));
							}
						} else {
							defaultPromoLink = getDefaultPromoLink(link, false);
							leadPriceDtoPromo = getLeadPriceFromCache(defaultPromoLink,Integer.parseInt(SystemInfo.getProperty("wth.default.groupId")));
						}
					}
					if (leadPriceDtoPromo != null) {
						leadPriceValue = leadPriceDtoPromo.getLeadPriceValue();
					} else {
						log.debug("lead price could not be found from cache for key: "
								+ leadPriceKey);
					}
				}
			

			/*
			 * If lead price refresh is requested then determine lead price
			 * through search services and save to database Lead price for the
			 * search result link is determined here Lead price for the promo
			 * page (group of product collections) is not determined here but
			 * through PromoAction
			 */
			if (this.refreshLeadPrice && Tools.isEmpty(leadPriceRequest.getPromoPageLink())) {
				if (!isDuplicateKey(leadPriceKey, this.groupId)) {
					leadPriceValue = getLeadPriceForSearchLink(leadPriceRequest);
					saveLeadPrice(leadPriceKey, leadPriceValue, this.groupId);
				}
			}

			if (leadPriceValue != null) {
				StoreDTO store = this.siteConfig.getStoreDTO();
				if (!CurrencyExchange.DEFAULT_CURRENCY_CODE.equals(store.getCurrencyCode())) {
					leadPriceValue = leadPriceValue.multiply(store.getCurrencyRate());
					leadPriceValue = leadPriceValue.setScale(0,RoundingMode.HALF_UP);
				}
			}

		} catch (Exception e) {
			log.error(
					"Lead Price Error: could not determine the lead price for search result link",
					e);
		}
		if (leadPriceValue != null)
			leadPrices.add(leadPriceValue);

		return leadPriceValue;
	}

	public LeadPriceRequestDTO createLeadPriceRequestDTO(String link) {
		LeadPriceRequestDTO leadPriceRequest = new LeadPriceRequestDTO();
		// Create a request DTO for getting the lead price
		if (link != null) {
			log.debug("given link: " + link);
			if (!isPromoPageLink(link)) {
				// Clean up the given link
				String safeLink = prepareSearchLink(link);
				// Sort search parameters and add it to DTO so that it can be
				// reused in SearchCruiseHelper
				String paramLink = StringUtils.substringAfter(safeLink, "?");
				SortedMap<String, String> searchParams = getSearchParams(paramLink);
				leadPriceRequest.setSearchParams(searchParams);
				// Create a key with sorted parameters that can be stored in the
				// database
				String action = StringUtils.substringBefore(safeLink, "?");
				StringBuilder leadPriceKeyBuilder = new StringBuilder();
				for (Map.Entry<String, String> entry : searchParams.entrySet()) {
					leadPriceKeyBuilder.append(entry.getKey() + "=" + entry.getValue() + "&");
				}
				String leadPriceKey = action + "?" + StringUtils.chop(leadPriceKeyBuilder.toString());
				leadPriceRequest.setSearchResultsLink(leadPriceKey.toString());
				log.debug("lead price search key: " + leadPriceKey.toString());
				// Create the SearchRequestDTO that can be used for searching
				// required cruises
				if (this.refreshLeadPrice) {
					// Cruise searching service requires context, so add
					// storecode
					searchParams.put("storecode",this.siteConfig.getStorecode());
					CustomerPref customerPref = new CustomerPref();
					CruiseSearchRequestDTO searchRequestDTO = SearchCruiseHelper.createRequest(searchParams, customerPref);
					searchRequestDTO.setSortItem(getSortElement(searchParams.get("sort_by")));
					leadPriceRequest.setSearchRequest(searchRequestDTO);
				}
			} else {
				String promoPageLink = "http://"
						+ this.siteConfig.getDomainName() + link + ".do";
				log.debug("lead price promo page: " + promoPageLink);
				leadPriceRequest.setPromoPageLink(promoPageLink);
			}
		}
		return leadPriceRequest;
	}

	public LeadPriceDTO getLeadPriceFromCache(String leadPriceKey, int groupId) {
		LeadPriceServiceDelegate leadPriceServiceDelegate = new LeadPriceServiceDelegate();
		return leadPriceServiceDelegate.getLeadPrice(leadPriceKey, groupId);
	}

	public BigDecimal getLeadPriceForSearchLink(
			LeadPriceRequestDTO leadPriceRequest) {
		BigDecimal leadPriceValue = null;
		List<BigDecimal> leadPriceValues = new ArrayList<BigDecimal>();
		log.debug("determining lead price for the search link");
		CruiseServiceDelegate cruiseDelegate = new CruiseServiceDelegate();
		CruiseSearchRequestDTO searchRequest = leadPriceRequest.getSearchRequest();
		searchRequest.setCruiseopselector(getCruiseOpSelector());
		searchRequest.setAscending(searchRequest.getSortItem().isAscending());
		CruiseResponseDTO cruiseresponse = cruiseDelegate.getSearch(searchRequest);
		log.debug("service:response: " + cruiseresponse);
		if (cruiseresponse != null) {
			CruiseSearchDTO cruiseSearch = cruiseresponse.getCruiseSearch();
			if (cruiseSearch != null) {
				List<CruiseDTO> cruiseList = cruiseresponse.getCruiseSearch().getCruiseList();
				if (cruiseList != null && cruiseList.size() > 0) {
					for (CruiseDTO cruise : cruiseresponse.getCruiseSearch().getCruiseList()) {
						leadPriceValues.add(cruise.getCruisesummary().getFromPrice());
					}
				}
			}
		}
		if (leadPriceValues.size() > 0) {
			Collections.sort(leadPriceValues);
			leadPriceValue = leadPriceValues.get(0);
			log.debug("service:lead price: " + leadPriceValues);
		} else {
			// Log to ops data that lead price was not found
			log.error("lead price could not be found for " + leadPriceRequest.getSearchResultsLink());
		}
		return leadPriceValue;
	}

	public SortedMap<String, String> getSearchParams(String link) {
		// Treemap will ensure sorted parameters and hence avoid duplicates
		// (when same parameters are ordered in the database
		SortedMap<String, String> requestParams = new TreeMap<String, String>();
		List<String> paramsList = Tools.splitAsList(link, URL_DELIMITER, true,
				true);
		for (String paramStr : paramsList) {
			String[] param = Tools.split(paramStr, PARAM_DELIMITER, true, true);
			if (param != null && param.length == 2) {
				requestParams.put(param[0].toLowerCase(), param[1]);
			}
		}
		// Since we're interested only in lead price, we'll sort ascending by
		// price and take the first result
		if (requestParams.containsKey("sort_by"))
			requestParams.remove("sort_by");
		requestParams.put("sort_by", "1");
		requestParams.put("startindex", "1");
		requestParams.put("endindex", "1");
		// Remove other unnecessary parameters
		log.debug("removing parameters");
		if (requestParams.containsKey("search"))
			requestParams.remove("search");
		if (requestParams.containsKey("search.x"))
			requestParams.remove("search.x");
		if (requestParams.containsKey("search.y"))
			requestParams.remove("search.y");
		if (requestParams.containsKey("searchorigin"))
			requestParams.remove("searchorigin");
		if (requestParams.containsKey("sort"))
			requestParams.remove("sort");
		return requestParams;
	}

	public void saveLeadPrice(String leadPriceKey, BigDecimal leadPriceValue, int groupId) {
		LeadPriceServiceDelegate leadPriceServiceDelegate = new LeadPriceServiceDelegate();
		leadPriceServiceDelegate.saveLeadPrice(leadPriceKey, groupId,leadPriceValue);
	}

	public CruiseOutputSelectorDTO getCruiseOpSelector() {
		CruiseOutputSelectorDTO cruiseopselector = new CruiseOutputSelectorDTO();
		cruiseopselector.setSummary(true);
		return cruiseopselector;
	}

	public boolean isPromoPageLink(String link) {
		boolean result;
		// Assuming promo-page names (node names in Magnolia) will not have '&'
		// character
		if (link.contains(URL_DELIMITER)) {
			result = false;
		} else {
			result = true;
		}
		return result;
	}

	public String prepareSearchLink(String link) {
		// Remove any markups from the URL
		link = StringUtils.remove(link, "#");
		// Remove jsessionid if any from the URL
		if (StringUtils.contains(link, "jsessionid")) {
			String temp = StringUtils.substringBetween(link, "results.do", "?");
			link = StringUtils.remove(link, temp);
		}
		// Remove html encoding for '&'
		log.debug("Link before: " + link);
		link = StringUtils.replace(link, "&amp;", "&");
		log.debug("Link after: " + link);
		return link;
	}

	public static CruiseSearchSortItemDTO getSortElement(String sort_by) {
		CruiseSearchSortItemDTO sortElement = null;
		if (!Tools.isEmpty(sort_by)) {
			log.debug("specific sort: " + sort_by);
			try {
				SortItemGroup sortItemGroup = SortItemGroup.valueOf(Integer.parseInt(sort_by));
				sortElement = CruiseSearchSortItemDTO.valueOf(sortItemGroup.getSortElement());
				sortElement.setAscending(sortItemGroup.isAscending());
			} catch (NumberFormatException nfe) {
				log.error(nfe.getMessage());
			}
		} else {
			log.debug("default sort: " + sort_by);
			// default sort
			sort_by = Integer.toString(SortItemGroup.PRICE.getSortId());
			sortElement = CruiseSearchSortItemDTO.PRICE;

		}
		return sortElement;
	}

	public static String getDefaultPromoLink(String link, boolean isPartner) {

		String promoPageLink;

		if (isPartner) {
			promoPageLink = "http://" + SystemInfo.getProperty("wth.default.partnerLeadPriceSite") + link + ".do";
		} else {
			promoPageLink = "http://" + SystemInfo.getProperty("wth.default.pageLeadPriceSite") + link + ".do";
		}
		log.debug("new promo page link: " + promoPageLink);
		return promoPageLink;
	}

	public boolean isDuplicateKey(String leadPriceKey, int groupId) {
		LeadPriceServiceDelegate leadPriceServiceDelegate = new LeadPriceServiceDelegate();
		boolean isDup = false;
		Date now = new Date();
		LeadPriceDTO leadPricedto = leadPriceServiceDelegate.getLeadPrice(
				leadPriceKey, groupId);
		if (leadPricedto != null) {
			Date last = leadPricedto.getUpdt();
			if (last != null) {
				long diff = now.getTime() - last.getTime(); // difference in
															// time in
															// milliseconds
				long dupKeyMinutes = Integer.valueOf(SystemInfo
						.getProperty("wth.swap.job.leadprice.dupKeyMinutes")) * 60 * 1000;
				if (diff < dupKeyMinutes) {
					isDup = true;
					log.debug("duplicate request was ignored");
				}
			}
		}
		return isDup;
	}

	public boolean isPartner(String homeAttribue, String houseAttribue) {
		if ((homeAttribue != null && homeAttribue.equalsIgnoreCase("Y"))
				|| (houseAttribue != null && houseAttribue
						.equalsIgnoreCase("Y")))
			return false;
		else
			return true;
	}

	public List<BigDecimal> getLeadPrices() {
		return leadPrices;
	}

	public void setLeadPrices(List<BigDecimal> leadPrices) {
		this.leadPrices = leadPrices;
	}

}
