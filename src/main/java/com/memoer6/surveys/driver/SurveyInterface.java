package com.memoer6.surveys.driver;

import org.openqa.selenium.WebDriver;

import com.memoer6.surveys.model.SurveyFields;

public interface SurveyInterface {
	
	void fillSurvey(WebDriver driver, SurveyFields surveyFields);	

}
