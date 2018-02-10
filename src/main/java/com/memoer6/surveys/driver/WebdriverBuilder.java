package com.memoer6.surveys.driver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Level;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;




@Component
public class WebdriverBuilder {
		
	@Autowired
	private Environment environment;
	
		
	@Value("${surveys.remoteDriver.url}")
	private String URL_DRIVER;
			
	
	public WebDriver getWebdriver() {
		
		//Deactivate the logging in the driver
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
		
		//Check if active profiles contains production		
		if(Arrays.stream(environment.getActiveProfiles()).anyMatch(
			env -> (env.equalsIgnoreCase("production")))) {

				//instantiate firefox remote driver provided by the container		
				FirefoxOptions capability = new FirefoxOptions();				
				
				try {			
					return new RemoteWebDriver(new URL(URL_DRIVER), capability);
				} catch (MalformedURLException e) {			
					e.printStackTrace();
					return null;
			
				}
			
		} else {			
			//instantiate local firefox for development			
			return new FirefoxDriver();	
		}
		
	}

		

}

