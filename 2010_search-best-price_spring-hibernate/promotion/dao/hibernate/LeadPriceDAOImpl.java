package com.nlg.services.promotion.dao.hibernate;

import java.util.List;
import java.util.ArrayList;

import org.hibernate.Criteria;

import com.nlg.common.logging.Log;
import com.nlg.services.core.dao.DAOHibernateImpl;
import com.nlg.services.core.dao.QueryRefreshUtil;
import com.nlg.services.promotion.bo.LeadPrice;
import com.nlg.services.promotion.bo.LeadPriceId;

public class LeadPriceDAOImpl extends DAOHibernateImpl<LeadPrice, LeadPriceId> implements LeadPriceDAO {

	private static Log log = Log.getLog(LeadPriceDAOImpl.class);
	
	//Pull all lead prices at a time from DAO; the Business Object will then read the one that it needs 
	public List<LeadPrice> getLeadPrices(){
		List<LeadPrice> leadPrices = new ArrayList<LeadPrice>();
		try{	
			Criteria c = this.getSession()
							.createCriteria(LeadPrice.class)						
							.setCacheable(true)
							.setCacheRegion("query.leadprice");
			leadPrices = QueryRefreshUtil.list(c);
		}
		catch(Exception e){
			log.error("Lead Price Error: could not get the lead price. ", e);
		}
		return leadPrices;
	}
	
}
