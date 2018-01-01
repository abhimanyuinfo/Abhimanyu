package com.bloomberg.service;

import java.util.HashMap;
import java.util.List;

import com.bloomberg.model.AccumulativeDeal;
import com.bloomberg.model.InValidDeal;
import com.bloomberg.model.ValidDeal;
/**
 * @author Abhimanyu
 *
 */
public interface IFileUploadService {

	void saveValidData(List<ValidDeal> dealDetails);
	
	void saveInValidData(List<InValidDeal> dealDetails);
	
	void saveAccumulativeData(List<AccumulativeDeal> accumulativeDeals);
	
	boolean checkFileExist(String fileName);
	
	HashMap<String, Integer> getFileSummary(String fileName);
	

}
