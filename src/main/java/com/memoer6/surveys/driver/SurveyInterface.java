package com.memoer6.surveys.driver;

import org.openqa.selenium.WebDriver;

import com.memoer6.surveys.model.SurveyInputs;

public interface SurveyInterface {
	
	int SUCCESS = 1;
	int FAILED = 2;
	
	int fillSurvey(WebDriver driver, SurveyInputs surveyInputs);	

}
