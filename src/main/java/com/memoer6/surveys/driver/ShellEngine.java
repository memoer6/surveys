package com.memoer6.surveys.driver;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.memoer6.surveys.model.SurveyInputs;

public class ShellEngine implements SurveyInterface {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final static int MIN_DELAY = 4;
	private final static int MAX_DELAY = 8;
		
	private WebDriver driver;
	private SurveyInputs surveyInputs;
	protected final static String NAME = "Shell";
	private final static String URL = "http://www.shell.ca/opinion";

	@Override
	public int fillSurvey(WebDriver webDriver, SurveyInputs surveyInputs) {
		
		this.driver = webDriver;
		this.surveyInputs = surveyInputs;
		
		driver.get(URL);
		
		//store		
		driver.findElement(By.id("s")).sendKeys(surveyInputs.getStoreNum());
		
		//date
		String day = surveyInputs.getDate().split("/")[1];
		driver.findElement(By.className("ui-datepicker-trigger")).click();
		//driver.findElement(By.xpath("//a[contains(text(), '10')]")).click();
		
		driver.findElement(By.xpath("//a[contains(text(), '" + day + "')]")).click();
		
		//time - hour
		Select selecth = new Select(driver.findElement(By.id("InputHour")));
		selecth.selectByVisibleText(surveyInputs.getTime().split(":")[0]);
		
		//time - minutes
		Select selectm = new Select(driver.findElement(By.id("InputMinute")));
		selectm.selectByVisibleText(surveyInputs.getTime().split(":")[1]);
		enter("1");
		
		
		//Overall satisfaction
		driver.findElements(By.className("radioBranded")).get(0).click();
		enter("2");
		
		//buy fuel question
		driver.findElement(By.xpath("//label[contains(text(), 'Purchased fuel')]//preceding::span[1]")).click();
		enter("3");
		
		//how did you pay
		driver.findElement(By.xpath("//label[contains(text(), 'At the Fuel Pump')]//preceding::span[1]")).click();
		enter("4");
		
		//Did you receive assistance from a forecourt attendant? / did you use your air mile?
		driver.findElement(By.xpath("//input[@id='R000104.2']//preceding::span[1]")).click();
		driver.findElement(By.xpath("//input[@id='R000105.1']//preceding::span[1]")).click();
		enter("5");
		
		//Did you interact with a staff member on this visit?
		driver.findElement(By.xpath("//input[@id='R007000.2']//preceding::span[1]")).click();
		enter("6");
		
		//Please rate your satisfaction with...
		List<WebElement> tds = driver.findElements(By.tagName("td"));
		for (WebElement td : tds) {
			if (td.getAttribute("class").equals("Opt5 inputtyperbloption")) {
				td.click();
			}
		}
		enter("7");
		
		//recommend & return
		driver.findElements(By.className("radioBranded")).get(0).click();
		enter("8");
		driver.findElements(By.className("radioBranded")).get(0).click();
		enter("9");
		
		//problem?
		driver.findElements(By.className("radioBranded")).get(1).click();
		enter("10");
		
		//Tell experience
		driver.findElement(By.id("S081000")).sendKeys("good location, clean and air-miles acceptance");
		enter("11");
		
		//few more questions
		driver.findElement(By.xpath("//label[contains(text(), 'go to Shell because I am an AIR MILES')]//preceding::span[1]")).click();
		enter("12");
		
		//What type of fuel did you purchase on this visit?
		driver.findElement(By.xpath("//label[contains(text(), 'Shell Bronze/Shell Silver/Shell Diesel')]//preceding::span[1]")).click();
		enter("13");
		
		//Did you purchase unleaded fuel or diesel?  When you buy fuel, how do you usually pay?
		driver.findElement(By.xpath("//label[contains(text(), 'Unleaded')]//preceding::span[1]")).click();
		driver.findElement(By.xpath("//label[contains(text(), 'I pay with my own cash/credit/debit card')]//preceding::span[1]")).click();
		enter("14");
		
		//Would you like to enter our prize draw?   Are you a Shell Employee?
		driver.findElements(By.className("radioBranded")).get(0).click();
		driver.findElements(By.className("radioBranded")).get(3).click();
		enter("15");
		
		//fill personal data form
		driver.findElement(By.id("S090100")).sendKeys(surveyInputs.getFirstName());
		driver.findElement(By.id("S090200")).sendKeys(surveyInputs.getLastName());
		driver.findElement(By.id("S092000")).sendKeys(surveyInputs.getPhone());
		driver.findElement(By.id("S093000")).sendKeys(surveyInputs.getEmail());
		driver.findElement(By.id("S093500")).sendKeys(surveyInputs.getEmail());
		enter("16");
		
		driver.close();		
		return SUCCESS;
		
	}
	
	// Create delay between 10 and 15 seconds		
	private void delay() {
		
		
		int delay = ThreadLocalRandom.current().nextInt(MIN_DELAY,MAX_DELAY);
		try {
			Thread.sleep(delay * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	private void enter(String number) {
		log.info("Answering question in page " + number);
		delay();
		driver.findElement(By.id("NextButton")).click();
	
	}

}
