package com.bloomberg.dao;

import java.util.HashMap;
import java.util.List;

import com.bloomberg.model.AccumulativeDeal;
import com.bloomberg.model.InValidDeal;
import com.bloomberg.model.ValidDeal;

public interface IFileUploadDAO {
	
	    boolean fileExists(String fileName);
	    
	    public void bulkValidSave(List<ValidDeal> validDeals);
	    
	    public void bulkInvalidSave(List<InValidDeal> inValidDeals);
	    
	    public void bulkAccumulativeSave(List<AccumulativeDeal> accumulativeDeals);
	    
	    public HashMap<String, Integer> getFileSummary(String filename);

}
