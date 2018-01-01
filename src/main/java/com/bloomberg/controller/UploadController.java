package com.bloomberg.controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bloomberg.model.AccumulativeDeal;
import com.bloomberg.model.CSVRecord;
import com.bloomberg.model.InValidDeal;
import com.bloomberg.model.ValidDeal;
import com.bloomberg.service.FileUploadService;
import com.opencsv.CSVReader;

/**
 * This class is the main controller for the application 
 *  
 *  @author Abhimanyu
 */

@Controller
public class UploadController {

	private final Logger logger = Logger.getLogger(UploadController.class);


	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	@Autowired
	FileUploadService service ;

	@Autowired
	private MessageSource messageSource;

	@GetMapping("/*")
	public String index() {
		logger.info("Stepped into the index() method");
		return "fileUpload";
	}

	@GetMapping(value = "/summary")
	public String summary(ModelMap model,
			@RequestParam("filename") String fileName){
		logger.info("summary method started...");
		
		if (fileName.isEmpty() || fileName == null) {
			Map<String, String> errorMessages = new HashMap<String, String>();
			errorMessages.put("alert-danger", messageSource.getMessage("missing.filename",null, Locale.getDefault()));
			model.put("isFileUploaded", Boolean.FALSE);
			model.put("errorMessages", errorMessages);
			logger.info("Validation failed file name is empty");
			return "fileUpload";
		}
		
		HashMap<String, Integer> summary = service.getFileSummary(fileName);
		model.put("isFileUploaded", Boolean.TRUE);
		model.put("isNotSummary", Boolean.FALSE);
		model.put("totalfiles", String.valueOf(summary.get("Valid") + summary.get("Invalid")));
		model.put("validDeals", String.valueOf(summary.get("Valid")));
		model.put("invalidDeals", String.valueOf(summary.get("Invalid")));
		model.put("filename", fileName);
		logger.info("summary method ended...");
		return "fileUpload";
	}
	
	@PostMapping(value = "/upload")
	public String uploadFileRequest(
			ModelMap model,
			@RequestParam("file") MultipartFile file,
			HttpServletRequest request,
			RedirectAttributes redirectAttributes){
		long startTime = System.currentTimeMillis();
		Map<String, String> messages = new HashMap<String, String>();
		logger.info("uploadFile method started...");
		if (file.isEmpty()) {
			messages.put("alert-danger", messageSource.getMessage("missing.file",null, Locale.getDefault()));
			model.put("isFileUploaded", Boolean.FALSE);
			model.put("messages", messages);
			logger.info("Validation failed file is empty");
			return "fileUpload";
		}
		else if(checkFileExist(file.getOriginalFilename())){
			messages.put("alert-danger", "File already exist");
			model.put("messages", messages);
			model.put("isFileUploaded", Boolean.FALSE);
			logger.info("File already exist");
			return "fileUpload";
		}
		File serverFile = new File(System.getProperty("java.io.tmpdir")+"/"+file.getOriginalFilename());
		try {
			file.transferTo(serverFile);
		} catch (IOException ex) {
			messages.put("alert-danger", messageSource.getMessage("failed.msg",new Object [] {ex}, Locale.getDefault()) );
			model.put("messages", messages);
			model.put("isFileUploaded", Boolean.FALSE);
			logger.error("uploadFile method crashed ", ex);
			return "fileUpload";
		}
		return processFile(serverFile, file.getOriginalFilename(), file, startTime, messages, model);
	}


	/**
	 * This method invoke dao call to store invalid deals 
	 * @param serverFile : Server File Path
	 * @param fileName : File Name
	 * @param file : Uploaded File
	 * @param startTime : Process Start Time
	 * @param messages : Message map 
	 * @param model : Model map
	 */
	public String processFile(File serverFile,String fileName,MultipartFile file,long startTime,Map<String, String> messages,ModelMap model) {
		List<ValidDeal> validDeals = new ArrayList<>();
		List<InValidDeal> inValidDeals = new ArrayList<>();
		Integer accCounts = null;
		try {
			logger.info("Process CSV file ...");
			List<String[]> lines = readCSVFile(serverFile, fileName);
			for(String[] line:lines) {
				CSVRecord csvRecord = extractData(line);
				csvRecord.setFileName(fileName);
				if(isInValid(csvRecord)){
					InValidDeal invalidDeal = new InValidDeal();
					csvRecord.setReason("Invalid Data");
					BeanUtils.copyProperties(csvRecord, invalidDeal);
					inValidDeals.add(invalidDeal);
				}
				else{
					ValidDeal validDeal = new ValidDeal();
					BeanUtils.copyProperties(csvRecord, validDeal);
					validDeals.add(validDeal);
				}
			}
			final CountDownLatch latch = new CountDownLatch(3);
			List<AccumulativeDeal> accumulativeDeals = getAccumulativeDeals(validDeals);
			persistInvalidDeals(inValidDeals,latch);
			persistValidDeals(validDeals,latch);
			persistAccmulativeDeals(accumulativeDeals,latch);
			accCounts = accumulativeDeals.size();
			latch.await();
		} catch (Exception e) {
			logger.error("processFile method crashed ", e);
		} 
		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime)/1000;
		messages.put("success", messageSource.getMessage("success.msg",new Object [] {file.getOriginalFilename()}, Locale.getDefault()));
		model.put("messages", messages);
		model.put("isFileUploaded", Boolean.TRUE);
		model.put("isNotSummary", Boolean.TRUE);
		model.put("filename", fileName);
		
		model.put("totalfiles", String.valueOf(validDeals.size() + inValidDeals.size()));
		model.put("timetaken", String.valueOf(duration));
		model.put("validDeals", String.valueOf(validDeals.size()));
		model.put("invalidDeals", String.valueOf(inValidDeals.size()));
		model.put("accumulativeDeals", String.valueOf(accCounts));
		logger.info("processFile completed successfully in :"+duration+" seconds");
		return "fileUpload";
	}



	public Boolean checkFileExist(String filename){
		return service.checkFileExist(filename);
	}

	/**
	 * This method invoke dao call to store invalid deals 
	 * @param inValidDeals : List of all invalid deals 
	 */
	private void persistInvalidDeals(List<InValidDeal> inValidDeals,CountDownLatch latch){
		if(inValidDeals.size() > 0){
			Runnable runnable = () -> {
				service.saveInValidData(inValidDeals);
				latch.countDown();
			};
			new Thread(runnable).start();
		}
	}

	/**
	 * This method invoke dao call to store valid deals 
	 * @param validDeals : List of all valid deals 
	 */
	private void persistValidDeals(List<ValidDeal> validDeals,CountDownLatch latch){
		if(validDeals.size() > 0){
			Runnable runnable = () -> {
				
				service.saveValidData(validDeals);
				latch.countDown();
			};
			new Thread(runnable).start();
		}
	}

	/**
	 * This method invoke dao call to store accmulative deals 
	 * @param validDeals : List of all valid deals 
	 */
	private void persistAccmulativeDeals(List<AccumulativeDeal> accumulativeDeals,CountDownLatch latch){
		if(accumulativeDeals.size() > 0){
			Runnable runnable = () -> {
				
				service.saveAccumulativeData(accumulativeDeals);
				latch.countDown();
			};
			new Thread(runnable).start();
		}
	}

	/**
	 * This method Checks if the CSV records are valid or not  
	 * @param csvRecord : The Record of CSV file.
	 */
	public Boolean isInValid(CSVRecord csvRecord){
		return StringUtils.isEmpty(csvRecord.getFromCurrency()) ||
				StringUtils.isEmpty(csvRecord.getToCurrency()) ||
				StringUtils.isEmpty(csvRecord.getAmount()) ||
				StringUtils.isEmpty(csvRecord.getDealDate());
	}

	private List<AccumulativeDeal> getAccumulativeDeals(List<ValidDeal> validDeals){ 
		Map<String, Long> accumulativeValues = validDeals.stream()
				.collect(Collectors.groupingBy(dealDetail -> dealDetail.getFromCurrency(), 
						Collectors.counting()));
		List<AccumulativeDeal> accumulativeDeals = (List<AccumulativeDeal>) accumulativeValues.keySet().stream().map(temp -> {
			AccumulativeDeal accumulativeDeal = new AccumulativeDeal();
			accumulativeDeal.setCount(new BigInteger(String.valueOf(accumulativeValues.get(temp))));
			accumulativeDeal.setOderingCurrency(temp.toString());
			return accumulativeDeal;
		}).collect(Collectors.toList());
		return accumulativeDeals;
	}

	/**
	 * This method extract the data and return the CVSRecord class 
	 * @param line : The line of CSV file.
	 */
	CSVRecord extractData(String[] line){
		CSVRecord dealModel = new CSVRecord();
		dealModel.setDeal_id(new BigInteger(line[0]));
		dealModel.setToCurrency(line[1]);
		dealModel.setFromCurrency(line[2]);
		try {
			formatter.setLenient(false);
			if(line[3] != null && !line[3].isEmpty())
				dealModel.setDealDate(formatter.parse(line[3]));
			else
				dealModel.setDealDate(null);
		} catch (ParseException e) {
			dealModel.setDealDate(null);
			dealModel.setReason("Invalid Date");;
			logger.error("Invalid Date : " + line[3]);
		}
		dealModel.setAmount(new BigInteger(line[4]));
		return dealModel;
	}


	/**
	 * This read the data from CSV File 
	 * @param serverFile : File Path , 
	 * @param fileName : File Name.
	 */
	List<String[]> readCSVFile(File serverFile, String fileName){
		logger.info("In readCSVFile Method..");
		List<String[]> lines = null;
		try {
			//read file
			logger.info(" reading CSV file");
			FileReader fileReader = new FileReader(serverFile);
			CSVReader reader = new CSVReader(fileReader, ',');
			lines = reader.readAll();
		} catch (IOException e) {
			logger.error("readCSVFile Method crashed ", e);
		} 
		return lines;
	}


}