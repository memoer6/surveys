package com.memoer6.surveys.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.memoer6.surveys.driver.SurveyMaster;
import com.memoer6.surveys.firebase.Messenger;
import com.memoer6.surveys.model.SurveyInputs;


@RestController
public class SurveyController implements SurveyAPI {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SurveyMaster surveyMaster;
		
	@Override
	@RequestMapping(value = "/survey", method = RequestMethod.POST)
	public void conductSurvey(@RequestBody SurveyInputs surveyInputs) {
		
		log.info(surveyInputs.toString());		
		
		surveyMaster.fillSurvey(surveyInputs);
		
	}

}
