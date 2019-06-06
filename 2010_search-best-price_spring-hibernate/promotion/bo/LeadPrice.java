package com.nlg.services.promotion.bo;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

import com.nlg.common.logging.Log;
import com.nlg.services.promotion.dao.hibernate.LeadPriceDAO;

public class LeadPrice implements Serializable{
	
	private static final long serialVersionUID = 480923777621393279L;
	private static Log log = Log.getLog(LeadPrice.class);
	private LeadPriceId id;
	private BigDecimal leadPriceValue;
	private String userIdUpdt;
	private Date updt;
	private static LeadPriceDAO leadPriceDao;
	
	public LeadPriceId getId() {
		return id;
	}
	public void setId(LeadPriceId id) {
		this.id = id;
	}
	public BigDecimal getLeadPriceValue() {
		return leadPriceValue;
	}
	public void setLeadPriceValue(BigDecimal leadPriceValue) {
		this.leadPriceValue = leadPriceValue;
	}
	public LeadPriceDAO getLeadPriceDao() {
		return leadPriceDao;
	}
	public void setLeadPriceDao(LeadPriceDAO leadPriceDao) {
		LeadPrice.leadPriceDao = leadPriceDao;
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

	public static LeadPrice getLeadPrice(LeadPriceId leadPriceId){
		LeadPrice lp = null;
		try{
			lp = leadPriceDao.read(leadPriceId, LeadPrice.class);
			/*
			 * Since timestamp is added by a database trigger, it won't be added to the cached object after database insert
			 * This is the reason why read method will not capture database triggered value
			 * The solution is to call refresh method (only if the timestamp is null) and then call read again
			 */
			if(lp!=null && lp.getUpdt()==null){
				 leadPriceDao.refresh(lp);		
				 log.debug("lead price for key: " + lp.getId().getLeadPriceKey() + " was refreshed to: " + lp.getLeadPriceValue().toString());
				 lp= leadPriceDao.read(leadPriceId, LeadPrice.class);
			}			
		}
		catch(Exception e){
			log.error("Lead Price Error: could not get the lead price from the cache or database. ", e);
		}
		return lp;
	}
		
	public static void saveLeadPrice(LeadPrice leadPrice){
		try {
			BigDecimal leadPriceValue = leadPrice.getLeadPriceValue();
			if (leadPriceValue != null){
				leadPriceDao.saveOrUpdate(leadPrice);		
				log.debug("lead price key: " + leadPrice.getId().getLeadPriceKey() + " and value: " + leadPriceValue + " was saved or updated");					
			}
			else {
				leadPriceDao.delete(leadPrice);
				if(leadPrice!=null && leadPrice.getId()!=null){
					log.debug("lead price key: " + leadPrice.getId().getLeadPriceKey() + " was deleted");	
				}
			}
		}
		catch (Exception e){
			log.error("Lead Price Error: could not save the lead price to the database. ", e);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if ((this == obj))
			return true;
		if ((obj == null))
			return false;
		if (!(obj instanceof LeadPrice))
			return false;
		LeadPrice castObj = (LeadPrice) obj;
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
