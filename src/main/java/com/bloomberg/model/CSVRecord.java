package com.bloomberg.model;

import java.math.BigInteger;
import java.util.Date;

public class CSVRecord {

	private Integer id;
	private BigInteger deal_id;
	private String fromCurrency;
	private String toCurrency;
	private Date dealDate;
	private BigInteger amount;
	private String reason;
	private String fileName;
	/**
	 * @return the id
	 */
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
	/**
	 * @return the fileName
	 */
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
