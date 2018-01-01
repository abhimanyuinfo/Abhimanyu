package com.bloomberg.model;


import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author Abhimanyu
 *
 */
@MappedSuperclass
public class DealModel {

	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private BigInteger deal_id;
	private String fromCurrency;
	private String toCurrency;
	private Date dealDate;
	private BigInteger amount;
	private String fileName;

	public DealModel() {
	}

	public DealModel(int id) {
		this.id = id;
	}
	
	/**
	 * @param id
	 * @param fromCurrency
	 * @param toCurrency
	 * @param dealDate
	 * @param amount
	 * @param fileName
	 */
	public DealModel(Integer id,
			BigInteger deal_id,
			String fromCurrency, String toCurrency, Date dealDate, BigInteger amount,
			String fileName) {
		super();
		this.id = id;
		this.deal_id = deal_id;
		this.fromCurrency = fromCurrency;
		this.toCurrency = toCurrency;
		this.dealDate = dealDate;
		this.amount = amount;
		this.fileName = fileName;
	}
	
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
	 * @return the fromCurrency
	 */
	@Column(name = "from_currency_iso_code")
	public String getFromCurrency() {
		return fromCurrency;
	}

	/**
	 * @param fromCurrency the fromCurrency to set
	 */
	public void setFromCurrency(String fromCurrency) {
		this.fromCurrency = fromCurrency;
	}

	/**
	 * @return the toCurrency
	 */
	@Column(name = "to_currency_iso_code")
	public String getToCurrency() {
		return toCurrency;
	}

	/**
	 * @param toCurrency the toCurrency to set
	 */
	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
	}

	/**
	 * @return the dealDate
	 */
	@Column(name = "date")
	public Date getDealDate() {
		return dealDate;
	}

	/**
	 * @param dealDate the dealDate to set
	 */
	public void setDealDate(Date dealDate) {
		this.dealDate = dealDate;
	}

	/**
	 * @return the amount
	 */
	@Column(name = "deal_amount")
	public BigInteger getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigInteger amount) {
		this.amount = amount;
	}

	/**
	 * @return the fileName
	 */
	@Column(name = "file_name")
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the deal_id
	 */
	public BigInteger getDeal_id() {
		return deal_id;
	}

	/**
	 * @param deal_id the deal_id to set
	 */
	public void setDeal_id(BigInteger deal_id) {
		this.deal_id = deal_id;
	}

	
	

}
