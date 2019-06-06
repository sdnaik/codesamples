package com.nlg.services.promotion.dto;

import java.util.SortedMap;
import com.nlg.services.cruise.dto.CruiseSearchRequestDTO;

public class LeadPriceRequestDTO {

	String promoPageLink = ""; //For pulling lead price of promo pages from cache
	String searchResultsLink = ""; //For pulling lead price of search results from cache
	SortedMap<String,String> searchParams;//For sorting search parameters
	CruiseSearchRequestDTO searchRequest; //For getting search results through CruiseServices
	
	public String getPromoPageLink() {
		return promoPageLink;
	}
	public void setPromoPageLink(String promoPageLink) {
		this.promoPageLink = promoPageLink;
	}
	public String getSearchResultsLink() {
		return searchResultsLink;
	}
	public void setSearchResultsLink(String searchResultsLink) {
		this.searchResultsLink = searchResultsLink;
	}
	public CruiseSearchRequestDTO getSearchRequest() {
		return searchRequest;
	}
	public void setSearchRequest(CruiseSearchRequestDTO searchRequest) {
		this.searchRequest = searchRequest;
	}
	public SortedMap<String, String> getSearchParams() {
		return searchParams;
	}
	public void setSearchParams(SortedMap<String, String> searchParams) {
		this.searchParams = searchParams;
	}
		
}
