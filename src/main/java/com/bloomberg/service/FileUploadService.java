package com.bloomberg.service;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bloomberg.dao.IFileUploadDAO;
import com.bloomberg.model.AccumulativeDeal;
import com.bloomberg.model.InValidDeal;
import com.bloomberg.model.ValidDeal;


/**
 * Service class for file upload 
 *
 * @author Abhimanyu
 */

@Service
public class FileUploadService implements IFileUploadService {

	private static final Logger logger = Logger.getLogger(FileUploadService.class);

	@Autowired
	private IFileUploadDAO dao;


	/**
	 * This method insert valid bulk deal 
	 * @param validDeals List of valid deals details
	 */
	@Override
	public void saveValidData(List<ValidDeal> validDeals) {
		logger.info("Stepped into the saveValidData() method CSV record size: "+ validDeals.size());
		dao.bulkValidSave(validDeals);
	}

	/**
	 * This method insert invalid bulk deal 
	 * @param inValidDeals List of invalid deals details
	 */
	@Override
	public void saveInValidData(List<InValidDeal> inValidDeals) {
		logger.info("Stepped into the Save Invalid Method : saveInValidData() method CSV record size: "+ inValidDeals.size());
		dao.bulkInvalidSave(inValidDeals);
	}

	/**
	 * This method check the file name exist in Database 
	 * @param fileName 
	 * @return true or false
	 */
	@Override
	public boolean checkFileExist(String fileName) {
		logger.info("Stepped into the checkFileExist() method file name: "+ fileName);
		return dao.fileExists(fileName);
	}





	/**
	 * This method insert accumulative deals 
	 * @param validDeals List of valid deals details
	 */
	@Override
	public void saveAccumulativeData(List<AccumulativeDeal> accumulativeDeals) {
		logger.info("Stepped into the Save Accumulative Data : saveAccumulativeData() method CSV record size: "+ accumulativeDeals.size());
		dao.bulkAccumulativeSave(accumulativeDeals);
	}

	@Override
	public HashMap<String, Integer> getFileSummary(String fileName) {
		logger.info("Stepped into the File Summary : getFileSummary() method File Name: "+ fileName);
		return dao.getFileSummary(fileName);
	}

}
