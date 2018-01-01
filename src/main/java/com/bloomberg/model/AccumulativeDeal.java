package com.bloomberg.model;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "accumulative_count")
public class AccumulativeDeal {
	
	
	private Integer id;
	private String oderingCurrency;
	private BigInteger count ;
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the oderingCurrency
	 */
	@Column(name = "ordering_currency")
	public String getOderingCurrency() {
		return oderingCurrency;
	}
	/**
	 * @param oderingCurrency the oderingCurrency to set
	 */
	public void setOderingCurrency(String oderingCurrency) {
		this.oderingCurrency = oderingCurrency;
	}
	/**
	 * @return the count
	 */
	@Column(name = "count")
	public BigInteger getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(BigInteger count) {
		this.count = count;
	}
	
	

}
