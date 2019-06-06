package com.nlg.services.promotion.serviceimpl;

import com.nlg.services.promotion.dto.LeadPriceDTO;
import com.nlg.services.promotion.dto.LeadPriceIdDTO;
import com.nlg.services.promotion.bo.LeadPrice;

public class LeadPriceDTOAssembler {
	
	public static LeadPriceDTO assemble(LeadPrice leadPrice){
		LeadPriceDTO leadPriceDTO = new LeadPriceDTO();
		LeadPriceIdDTO leadPriceIdDTO = new LeadPriceIdDTO();
		if(leadPrice!=null && leadPrice.getId()!=null){
			leadPriceIdDTO.setLeadPriceKey(leadPrice.getId().getLeadPriceKey());
			leadPriceIdDTO.setGroupId(leadPrice.getId().getGroupId());
			leadPriceIdDTO.setDeleteStatus(leadPrice.getId().getDeleteStatus());
			leadPriceDTO.setId(leadPriceIdDTO);
			leadPriceDTO.setLeadPriceValue(leadPrice.getLeadPriceValue());	
			leadPriceDTO.setUpdt(leadPrice.getUpdt());
			leadPriceDTO.setUserIdUpdt(leadPrice.getUserIdUpdt());			
		}
		return leadPriceDTO;
	}
}
