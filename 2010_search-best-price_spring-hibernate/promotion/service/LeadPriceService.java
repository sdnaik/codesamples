package com.nlg.services.promotion.service;

import java.math.BigDecimal;
import com.nlg.services.promotion.dto.LeadPriceDTO;

public interface LeadPriceService {
	public LeadPriceDTO getLeadPrice(String leadPriceKey, int groupId);
	public void saveLeadPrice(String leadPriceKey, int groupId, BigDecimal leadPriceValue);
}
