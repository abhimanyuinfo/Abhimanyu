package com.bloomberg.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.bloomberg.configuration.HibernateTestConfiguration;
import com.bloomberg.model.CSVRecord;
import com.bloomberg.model.InValidDeal;
import com.bloomberg.model.ValidDeal;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {HibernateTestConfiguration.class})
@ComponentScan({ "com.bloomberg" })
@WebAppConfiguration
@AutoConfigureMockMvc
public class UploadControllerTest {

	private final Logger logger = Logger.getLogger(UploadControllerTest.class);

	@Autowired
	private MockMvc mockMvc;


	@InjectMocks
	private UploadController uploadController;

	@Mock
	static HttpServletRequest request;       

	@Mock
	static HttpServletResponse response;  

	String fileName = "sample.csv";

	List<String[]> lines;

	static File serverfile;

	@Spy
	List<ValidDeal> validDeals = new ArrayList<ValidDeal>();

	@Spy
	List<InValidDeal> inValidDeals = new ArrayList<InValidDeal>();

	@Before
	public void before() {
		logger.debug("Setup Method before executing test cases..");
		try{
			ClassLoader classLoader = getClass().getClassLoader();
			serverfile = new File(classLoader.getResource(fileName).getFile());
			lines = uploadController.readCSVFile(serverfile, fileName);
		}catch(Exception ex){
			logger.error("Exception in Setup Method : ", ex);
		}
		logger.debug("Setup Complete...");
	}

	@Test
	public void indexTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/"))
		.andExpect(MockMvcResultMatchers.status().is(200));
	}
	
	

	@Test
	public void validateData() {
		logger.debug("validateData Method..");
		for(String[] line:lines) {
			CSVRecord csvRecord = uploadController.extractData(line);
			csvRecord.setFileName(fileName);
			if(uploadController.isInValid(csvRecord)){
				InValidDeal invalidDeal = new InValidDeal();
				BeanUtils.copyProperties(csvRecord, invalidDeal);
				inValidDeals.add(invalidDeal);
			}else{
				ValidDeal validDeal = new ValidDeal();
				BeanUtils.copyProperties(csvRecord, validDeal);
				validDeals.add(validDeal);
			}
		}
		logger.debug("uploadTestData Method ended..");
		Assert.assertEquals((inValidDeals.size()+validDeals.size()), lines.size());
	}



	@Test
	public void upload() throws Exception {
		FileInputStream fileInput = new FileInputStream(serverfile);
		MockMultipartFile file = new MockMultipartFile("file", serverfile.getName(), MediaType.MULTIPART_FORM_DATA.toString(), fileInput);
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/upload")
				.file(file))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void uploadExisting() throws Exception {
		FileInputStream fileInput = new FileInputStream(serverfile);
		MockMultipartFile file = new MockMultipartFile("file", serverfile.getName(), MediaType.MULTIPART_FORM_DATA.toString(), fileInput);
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/upload")
				.file(file))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void summaryTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/summary").param("filename", serverfile.getName()))
		.andExpect(MockMvcResultMatchers.status().is(200));
	}

}