package com.bloomberg.model;


import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Abhimanyu
 *
 */
@Entity
@Table(name = "invalid_deals")
public class InValidDeal extends  DealModel implements java.io.Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private String reason;

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	

}
