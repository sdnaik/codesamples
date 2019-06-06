package com.nlg.services.promotion.dao.hibernate;

import java.util.List;

import com.nlg.services.core.dao.DAO;
import com.nlg.services.promotion.bo.LeadPrice;
import com.nlg.services.promotion.bo.LeadPriceId;

public interface LeadPriceDAO extends DAO<LeadPrice, LeadPriceId> {
	public List<LeadPrice> getLeadPrices();
}
