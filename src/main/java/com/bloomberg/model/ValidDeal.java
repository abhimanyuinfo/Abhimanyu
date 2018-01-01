package com.bloomberg.model;


import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Abhimanyu
 *
 */
@Entity
@Table(name = "valid_deals")
public class ValidDeal extends  DealModel implements java.io.Serializable {

	
	private static final long serialVersionUID = 1L;

}
