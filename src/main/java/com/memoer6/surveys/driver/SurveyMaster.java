package com.memoer6.surveys.driver;


import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.memoer6.surveys.firebase.Messenger;
import com.memoer6.surveys.firebase.RestClient;
import com.memoer6.surveys.model.SurveyInputs;

//Class that triggers the survey and inform the result to the user

@Component
public class SurveyMaster {
	
	private final static Logger log = LoggerFactory.getLogger(SurveyMaster.class);
	
	SurveyInputs surveyInputs;
	private int surveyResult;
	
	@Autowired
	private WebdriverBuilder webdriverBuilder;
	
	@Autowired
	private Messenger messenger;

	
	// Method called from the controller to start the survey	
	public void fillSurvey(SurveyInputs surveyInputs) {
		
		this.surveyInputs = surveyInputs;
		
		WebDriver driver = webdriverBuilder.getWebdriver();
		
		String storeName = surveyInputs.getStoreName();
		
		//Normalize some fields before filling the survey
		inputsAdapter(storeName);
		
		//Conduct the survey
		switch (storeName) {
		case WalmartEngine.NAME:			
			surveyResult = new WalmartEngine().fillSurvey(driver, surveyInputs);
			break;
			
		case ShellEngine.NAME:			
			surveyResult = new ShellEngine().fillSurvey(driver, surveyInputs);
			break;	
			
		}
		
		log.info("The survey result is " + surveyResult);	
		
		//Send message via firebase cloud to inform the result
		if (surveyResult == SurveyInterface.SUCCESS) {
			messenger.sendMessage("the survey for " + storeName + " finished successfully");
		} else {
			messenger.sendMessage("the survey for " + storeName + " couldn't finish");
		}
	}
	
	//Internal method to adapt the inputs before running the survey
	private void inputsAdapter(String storeName) {
		
		//When a caret appears as the first character inside square brackets, it negates the pattern.
		String pc = surveyInputs.getPostalCode().replaceAll("[^a-zA-Z0-9]+", ""); //remove any character except letters and numbers 
		surveyInputs.setPostalCode(pc);
		
		String ph = surveyInputs.getPhone().replaceAll("[^0-9]+", ""); //remove any character except numbers (same as "\D+"
		surveyInputs.setPhone(ph);
		
		//Only Walmart
		if (storeName.equals(WalmartEngine.NAME)) {
			String tc = surveyInputs.getTc().replaceAll("\\s", ""); //remove all white spaces
			surveyInputs.setTc(tc);
		}
		
		//Only Shell
		if (storeName.equals(ShellEngine.NAME)) {
			
			// Add 0 to time if it's one digit 
			String time = surveyInputs.getTime();
			if (time.split(":")[0].length() == 1) {
				surveyInputs.setTime("0" + time);
			}
					
			//change O by 0 in store number
			String storenum = surveyInputs.getStoreNum();			
			if (storenum.substring(1,2).equals("O")) {
				surveyInputs.setStoreNum(storenum.substring(0,1) + "0" + storenum.substring(2));				
			}
						
		}
		
		
	}

    
}

