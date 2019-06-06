package com.nlg.services.promotion.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

public class LeadPriceDTO implements Serializable{
		
	private static final long serialVersionUID = 8334124147175431089L;
	private LeadPriceIdDTO id;
	private BigDecimal leadPriceValue;
	private String userIdUpdt;
	private Date updt;
	
	public LeadPriceIdDTO getId() {
		return id;
	}
	public void setId(LeadPriceIdDTO id) {
		this.id = id;
	}
	public BigDecimal getLeadPriceValue() {
		return leadPriceValue;
	}
	public void setLeadPriceValue(BigDecimal leadPriceValue) {
		this.leadPriceValue = leadPriceValue;
	}
	public Date getUpdt() {
		return updt;
	}
	public void setUpdt(Date updt) {
		this.updt = updt;
	}
	public String getUserIdUpdt() {
		return userIdUpdt;
	}
	public void setUserIdUpdt(String userIdUpdt) {
		this.userIdUpdt = userIdUpdt;
	}

	@Override
	public boolean equals(Object obj) {
		if ((this == obj))
			return true;
		if ((obj == null))
			return false;
		if (!(obj instanceof LeadPriceDTO))
			return false;
		LeadPriceDTO castObj = (LeadPriceDTO) obj;
		return this.id != null && castObj.id != null && this.id.equals(castObj.id);
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		if(this.id!=null){
			result = 37 * result + this.id.hashCode();	
		}
		return result;
	}
	
}
