package com.nlg.services.promotion.serviceimpl;

import java.math.BigDecimal;

import com.nlg.common.Tools;

import com.nlg.services.promotion.service.LeadPriceService;
import com.nlg.services.promotion.bo.LeadPrice;
import com.nlg.services.promotion.bo.LeadPriceId;
import com.nlg.services.promotion.dto.LeadPriceDTO;

public class LeadPriceServiceImpl implements LeadPriceService {

	public LeadPriceDTO getLeadPrice(String leadPriceKey, int groupId){
		LeadPriceDTO leadPriceDTO = null;
		if(!Tools.isEmpty(leadPriceKey) && groupId > 0){
			LeadPriceId leadPriceId = new LeadPriceId();
			leadPriceId.setLeadPriceKey(leadPriceKey);
			leadPriceId.setGroupId(groupId);
			leadPriceId.setDeleteStatus('N');
			LeadPrice leadPrice = LeadPrice.getLeadPrice(leadPriceId);
			leadPriceDTO = LeadPriceDTOAssembler.assemble(leadPrice);
		}
		return leadPriceDTO;
	}

	public void saveLeadPrice(String leadPriceKey, int groupId, BigDecimal leadPriceValue){
		if(!Tools.isEmpty(leadPriceKey) && groupId > 0){
			LeadPrice leadPrice = new LeadPrice();
			LeadPriceId leadPriceId = new LeadPriceId();
			leadPriceId.setLeadPriceKey(leadPriceKey);
			leadPriceId.setGroupId(groupId);
			leadPriceId.setDeleteStatus('N');
			leadPrice.setId(leadPriceId);
			leadPrice.setLeadPriceValue(leadPriceValue);
			LeadPrice.saveLeadPrice(leadPrice);
		}
	}
	
}

