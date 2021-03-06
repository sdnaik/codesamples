package com.nlg.services.promotion.bo;

import java.io.Serializable;

public class LeadPriceId implements Serializable {

	private static final long serialVersionUID = 1L;
	private String leadPriceKey;
	private int groupId;
	private char deleteStatus;

	public char getDeleteStatus() {
		return deleteStatus;
	}
	public void setDeleteStatus(char deleteStatus) {
		this.deleteStatus = deleteStatus;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getLeadPriceKey() {
		return leadPriceKey;
	}
	public void setLeadPriceKey(String leadPriceKey) {
		this.leadPriceKey = leadPriceKey;
	}

	@Override
	public boolean equals(Object obj) {
		if ((this == obj))
			return true;
		if ((obj == null))
			return false;
		if (!(obj instanceof LeadPriceId))
			return false;
		LeadPriceId castObj = (LeadPriceId) obj;
		return (
		(this.leadPriceKey != null && castObj.leadPriceKey != null && this.leadPriceKey.equalsIgnoreCase(castObj.leadPriceKey)) &&
		(this.groupId == castObj.groupId) &&
		(this.deleteStatus == castObj.deleteStatus)
		);
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		if(this.leadPriceKey!=null){
			result = 37 * result + this.leadPriceKey.hashCode();	
		}
		result = 37 * result + (new Integer(this.groupId)).hashCode();
		result = 37 * result + (new Character(this.deleteStatus)).hashCode();
		return result;
	}
	
}
