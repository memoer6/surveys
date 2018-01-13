package com.memoer6.surveys.driver;


import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.memoer6.surveys.model.SurveyFields;


@Component
public class SurveyDriver {
	
	SurveyFields surveyFields;
	
	@Autowired
	private DriverBuilder driverBuilder;
	
		
	public void fillSurvey(SurveyFields surveyFields) {
		
		this.surveyFields = surveyFields;
		
		//Normalize some fields before filling the survey
		fieldsAdapter();
		
		WebDriver driver = driverBuilder.getDriver();
			
		
		String storeName = surveyFields.getStoreName();
		switch (storeName) {
		case WalmartEngine.NAME:
			new WalmartEngine().fillSurvey(driver, surveyFields);
			
		}
	}
	
	private void fieldsAdapter() {
		
		//When a caret appears as the first character inside square brackets, it negates the pattern.
		String pc = surveyFields.getPostalCode().replaceAll("[^a-zA-Z0-9]+", ""); //remove any character except letters and numbers 
		surveyFields.setPostalCode(pc);
		
		String ph = surveyFields.getPhone().replaceAll("[^0-9]+", ""); //remove any character except numbers (same as "\D+"
		surveyFields.setPhone(ph);
		
		//Only Walmart
		String tc = surveyFields.getTc().replaceAll("\\s", ""); //remove all white spaces
		surveyFields.setTc(tc);
		
	}

    
}

