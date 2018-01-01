package com.bloomberg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bloomberg.model.AccumulativeDeal;
import com.bloomberg.model.InValidDeal;
import com.bloomberg.model.ValidDeal;
import com.bloomberg.service.FileUploadService;

/**
 * Data access layer for file upload 
 *
 * @author Abhimanyu
 */
@Transactional
@Repository
public class FileUploadDao implements IFileUploadDAO {

	private static final Logger logger = Logger.getLogger(FileUploadService.class);

	@Autowired
	private SessionFactory sessionFactory;

	//@Value("${hibernate.jdbc.batch_size}")
	private int batchSize = 50;

	private static String VALID_DEALS =  "select count(*) from ValidDeal where fileName=:fileName";

	private static String INVALID_QUERY = "insert into invalid_deals (deal_id,from_currency_iso_code,to_currency_iso_code,date,deal_amount,file_name,reason) values (?,?,?,?,?,?,?) ";

	private static String VALID_QUERY = "insert into valid_deals (deal_id,from_currency_iso_code,to_currency_iso_code,date,deal_amount,file_name) values (?,?,?,?,?,?) ";

	private static String INVALID_DEALS = "select count(*) from InValidDeal where fileName=:fileName";


	/**
	 * This method check the file name exist in Database 
	 * @param fileName 
	 * @return true or false
	 */
	@Override
	public boolean fileExists(String fileName) {
		Session session = sessionFactory.openSession();
		int validDeals = ((Long)session.createQuery(FileUploadDao.VALID_DEALS).setParameter("fileName", fileName).uniqueResult()).intValue();
		int invalidDeals = ((Long)session.createQuery(FileUploadDao.INVALID_DEALS).setParameter("fileName", fileName).uniqueResult()).intValue();
		return  (validDeals+invalidDeals)> 0 ? true : false;
	}

	/**
	 * This method insert invalid bulk deal 
	 * @param invalidDeals List of InValidBulkDeal
	 */
	@Override
	public void bulkInvalidSave(List<InValidDeal> invalidDeals) {

		logger.info("Save invalid entities method started...");
		long startTime = System.currentTimeMillis();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.doWork(new Work() {
			@Override
			public void execute(Connection conn) throws SQLException {
				PreparedStatement pstmt = null;
				try{
					pstmt = conn.prepareStatement(FileUploadDao.INVALID_QUERY);
					int i=0;
					for(InValidDeal inValidDeal : invalidDeals){
						pstmt.setLong(1, Long.parseLong(inValidDeal.getDeal_id().toString()));
						pstmt.setString(2, inValidDeal.getFromCurrency());
						pstmt.setString(3, inValidDeal.getToCurrency());
						pstmt.setDate(4, inValidDeal.getDealDate()!=null? new java.sql.Date(inValidDeal.getDealDate().getTime()):null);
						pstmt.setLong(5, inValidDeal.getAmount()!=null?Long.parseLong(inValidDeal.getAmount().toString()):null);
						pstmt.setString(6, inValidDeal.getFileName());
						pstmt.setString(7, inValidDeal.getReason());
						pstmt.addBatch();
						//20 : JDBC batch size
						if ( i % batchSize == 0 ) { 
							pstmt.executeBatch();
						}
						i++;
					}
					pstmt.executeBatch();
				}
				finally{
					pstmt.close();
				}                                
			}
		});
		tx.commit();
		session.close();
		long endTime = System.currentTimeMillis();
		long dur = (endTime - startTime)/1000;
		logger.info("Invalid entities saved in : "+dur+" seconds");
	}

	/**
	 * This method insert valid bulk deal 
	 * @param validDeals List of ValidBulkDeal
	 */
	@Override
	public void bulkValidSave(List<ValidDeal> validDeals) {
		logger.info("Save valid entities method started...");
		long startTime = System.currentTimeMillis();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.doWork(new Work() {
			@Override
			public void execute(Connection conn) throws SQLException {
				PreparedStatement pstmt = null;
				try{
					pstmt = conn.prepareStatement(FileUploadDao.VALID_QUERY);
					int i=0;
					for(ValidDeal validDeal : validDeals){
						pstmt.setLong(1, Long.parseLong(validDeal.getDeal_id().toString()));
						pstmt.setString(2, validDeal.getFromCurrency());
						pstmt.setString(3, validDeal.getToCurrency());
						pstmt.setDate(4, validDeal.getDealDate()!=null? new java.sql.Date(validDeal.getDealDate().getTime()):null);
						pstmt.setLong(5, validDeal.getAmount()!=null?Long.parseLong(validDeal.getAmount().toString()):null);
						pstmt.setString(6, validDeal.getFileName());
						pstmt.addBatch();
						//20 : JDBC batch size
						if ( i % batchSize == 0 ) { 
							pstmt.executeBatch();
						}
						i++;
					}
					pstmt.executeBatch();
				}
				finally{
					pstmt.close();
				}                                
			}
		});
		tx.commit();
		session.close();
		long endTime = System.currentTimeMillis();
		long dur = (endTime - startTime)/1000;
		logger.info("Valid Entities saved in : "+dur+" seconds");
	}

	/**
	 * This method save all AccumulativeDeals
	 * @param accumulativeDeals : List of Accumulative Deals
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void bulkAccumulativeSave(List<AccumulativeDeal> accumulativeDeals) {

		long startTime = System.currentTimeMillis();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Iterator<AccumulativeDeal> it = accumulativeDeals.iterator();
		int i = 0;
		while(it.hasNext()){ 
			i++;
			AccumulativeDeal accumulativeDeal = (AccumulativeDeal)it.next();
			List<AccumulativeDeal> deals = session.createQuery("from AccumulativeDeal where oderingCurrency=:oderingCurrency")
					.setParameter("oderingCurrency", accumulativeDeal.getOderingCurrency()).list();
			if(deals.size() > 0){
				AccumulativeDeal deal = deals.get(0);
				deal.setCount(deal.getCount().add(accumulativeDeal.getCount()));
				session.saveOrUpdate(deal);
			}
			else{
				session.persist(accumulativeDeal);
			}
			if (i % batchSize == 0) { session.flush(); session.clear(); }
		}
		long endTime = System.currentTimeMillis();
		long dur = (endTime - startTime)/1000;
		logger.info("Valid Accumulative Deals in : "+dur+" seconds");
		tx.commit();
		session.close();
	}

	@Override
	public HashMap<String, Integer> getFileSummary(String filename) {
		logger.info("Fetching File Summary...");
		Session session = sessionFactory.openSession();
		int validValues = ((Long)session.createQuery(FileUploadDao.VALID_DEALS).setParameter("fileName", filename).uniqueResult()).intValue();
		int invalidValues = ((Long)session.createQuery(FileUploadDao.INVALID_DEALS).setParameter("fileName", filename).uniqueResult()).intValue();
		HashMap<String, Integer> dealsSummary = new HashMap<String, Integer>();
		dealsSummary.put("Valid", new Integer(validValues));
		dealsSummary.put("Invalid", new Integer(invalidValues));
		logger.info("Returning File Summary details...");
		return dealsSummary;
	}

}