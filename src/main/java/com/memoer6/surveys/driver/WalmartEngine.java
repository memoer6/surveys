package com.memoer6.surveys.driver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.memoer6.surveys.model.SurveyInputs;


public class WalmartEngine implements SurveyInterface {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final static int MIN_DELAY = 8;
	private final static int MAX_DELAY = 12;
	
	private WebDriver driver;
	private SurveyInputs surveyInputs;
	protected final static String NAME = "Walmart";
	private final static String URL = "http://survey.walmart.ca/";
	private final static List<String> starGroup = new ArrayList<> (
			Arrays.asList("Q2", "Q3", "Q4", "Q4a", "Q9", "Q13", "Q17", "Q20",
					"Q33", "Q37", "Q37d", "Q65", "Q75", "Q81", "Q83", "Q98", "Q94"));
	
	//this creates a immutable and static hash map using the static initializer
	//In java 9, it would be simpler (Map.of)
	private static Map<String, Integer> rowGroup = new HashMap<>();
	static {	
	
		rowGroup.put("Q5", 1);
		rowGroup.put("Q10b", 2);
		rowGroup.put("Q12", 6);
		rowGroup.put("Q14", 1);
		rowGroup.put("Q23", 5);
		rowGroup.put("Q26", 98);
		rowGroup.put("Q31", 7);
		rowGroup.put("Q37b", 4);		
		rowGroup.put("Q39", 6);
		rowGroup.put("Q41", 9);
		rowGroup.put("Q42", 4);
		rowGroup.put("Q43", 3);
		rowGroup.put("Q44", 1);
		rowGroup.put("Q61", 0);
		rowGroup.put("Q62", 96);
		rowGroup.put("Q68", 1);
		rowGroup.put("Q69", 10);
		rowGroup.put("Q70", 1);
		rowGroup.put("Q71", 1);
		rowGroup.put("Q72", 0);
		rowGroup.put("Q73", 1);
		rowGroup.put("Q74", 0);
		rowGroup.put("Q91", 1);
		rowGroup.put("Q92", 1);
		rowGroup.put("Q93", 1);		
		rowGroup.put("Q98", 1);
		rowGroup.put("Q99", 3);
		rowGroup.put("ST4", 2);
		rowGroup.put("S1", 0);
		rowGroup.put("S2a", 3);
		rowGroup.put("S3", 1);
		rowGroup.put("A2", 0);
		rowGroup.put("A1a", 0);
		rowGroup.put("A13", 1);
		rowGroup.put("A14", 0);			
	}
	
	private static final Map<String, String> messageGroup;
	static {
		Map<String, String> m = new HashMap<>();
		m.put("Q8b", "It's OK");
		m.put("Q11", "Nothing to add");
		m.put("Q19", "It's OK");
		m.put("Q30b", "As I expected");
		m.put("Q37a", "The service");
		messageGroup = Collections.unmodifiableMap(m);		
	}
	
	// These questions don't require any action. They are just passed
	//private static final List<String> noAction = new ArrayList<>( 
	//	Arrays.asList("Drillint", "Intro", "Q21a", "Z1Intro"));	
	
	// These questions require filling a toggle box
	private static final List<String> toogleBoxGroup = new ArrayList<>( 
		Arrays.asList("Q15a", "Q15b", "Q22b", "Q22a", "Tips"));	
	
			
	
	@Override
	public int fillSurvey(WebDriver webDriver, SurveyInputs surveyInputs) {
		
		
		this.driver = webDriver;
		this.surveyInputs = surveyInputs;
		
		//Implicit wait
		//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		//Update answer for gender
		int gender = surveyInputs.getGender().equals("Male") ? 1 : 0; 
		rowGroup.put("Q38", gender);
		
		driver.get(URL);
		String question = driver.getTitle();
		return mainLoop(question);
		
		
		
			
	}
	
	//Main loop of the survey
	private int mainLoop(String question) {
		
		String formerQuestion = "";
		
		try {
			
			while (!question.equals("Thank You")) {
				
				formerQuestion = question;
				log.info("Answering question in page " + question);			
				answerQuestion(question);				
				question = driver.getTitle();	
				
				//If the new page title is the same, we have problems
				if (formerQuestion.equals(question)) {
					log.info("The survey is repeating the page " + question);	
					driver.close();
					return FAILED;
					
				}
								
			}			
		
			log.info("Finishing the survey with page " + question);	
			driver.close();
			return SUCCESS;
			
			
			
		} catch (UnhandledAlertException e) {
			
			Alert alert = driver.switchTo().alert();
			log.info("Handling alert (text): " + alert.getText());		
			alert.accept();	
			delay();			
			question = driver.getTitle();
			
			//If the new page title is the same, we have problems
			if (formerQuestion.equals(question)) {
				log.info("The survey is repeating the page " + question);	
				driver.close();
				return FAILED;
				
			}
			
			mainLoop(question);
			
		} catch (org.openqa.selenium.NoSuchElementException e) {
			log.info("No Such element location in " + question);	
			driver.close();
			return FAILED;			
		}
		
		return SUCCESS;
		
	}
	
		
	private void answerQuestion(String question) {
		
		//Add delay before answering each question
		delay();
	
		
		//Answer the questions asking for feedback with stars
		if (starGroup.contains(question)) {
			
			answerStars(question);
		
		//Answer the questions asking for simple row selection		
		} else if (rowGroup.containsKey(question)) {
			
			answerRowSelection(question);
		
		//Answer the questions asking to type a message	
		} else if (messageGroup.containsKey(question)) {
			
			answerMessages(question);
		
		//Answer the questions with toogle box
		} else if (toogleBoxGroup.contains(question)) {
			
			answerToogleBox(question);
		
		//Answer the language selection
		} else if (question.equals("Intro - survey wal-mart.ca")) {
						
			answerLanguageSelection(question);
		
		} else if (question.equals("Q10a")) {
			
			for (WebElement option : driver.findElements(By.tagName("option"))) {
				if (option.getText().equals("1 hour")) option.click();
			}
		} else if (question.equals("A3")) {
			
			log.info("Entering NonScript-Pharm in page " + question);					
			driver.findElement(By.xpath("//img[contains(@src, '/Surveys/WMCA/WMCASTrak/Images/Dept/NonScript-Pharm_Gray.gif')]")).click();	
			
		//Answer math challenge
		} else if (question.equals("S3a")) {
			
			answerMath(question);
			
		} else if (question.equals("Postal")) {
			
			answerPostalCode(question);
			
		} else if (question.equals("S2")) {
			
			answerBirthYear(question);
			
		//Answer with data from the receipt				
		} else if (question.equals("S4b")) {
			
			answerReceipt(question);
			
		//Answer the arrival time to the store			
		} else if (question.equals("A0")) {
			
			answerArrivalTime(question);
			
		//Answer final form	
		} else if (question.equals("Z1")) {
			
			answerForm(question);
		}
		
		
		//Click "Continue" button 
		driver.findElement(By.name("image")).click();
	
	
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
	
	//assign number of stars randomly between 7-10 to numberRows questions
	private List<Integer> setStars(int numberRows) {
		List<Integer> starList = new ArrayList<>();
		for (int i=0; i < numberRows; i++) {
			starList.add(ThreadLocalRandom.current().nextInt(8, 10)); //select random number between 8-10			
		}
		log.info("Stars given: " + starList.toString());
		return starList;
	}
	
	//return ~ 1 hour earlier from the time in the receipt (hh:mm:ss)
	private String setArrivalTime(String receiptTime) {
		Integer hour = Integer.valueOf(receiptTime.split(":")[0]) - 1;		
		if (hour == 12) return "12:00pm";
		else if (hour == 0) return "12:00am";
		else if (hour == -1) return "11:00pm";
		else if (hour < 12) return String.valueOf(hour) + ":00am";
		else return String.valueOf(hour - 12) + ":00pm";
	}
	
	private void answerStars(String question) {
		
		if (starGroup.contains(question)) {
			
			// find the number of questions to answer with stars in the page by looking how many star{number}_1 labels
			// are in the document
			String src = driver.getPageSource();
			Pattern pattern = Pattern.compile("star.*_1\"");
			Matcher matcher = pattern.matcher(src);
			List<String> allMatches = new ArrayList<>();
			while (matcher.find()) {
				allMatches.add(matcher.group());
			}
			
			// Get a list of random number of stars to answer per question
			List<Integer> starList = setStars(allMatches.size());
			
			// Answer each question with the random number of stars
			int index = 0;
			for (String match : allMatches) {
				String label = match.split("_")[0] + "_" + starList.get(index);
				log.info("Selecting " + label + " in page " + question);
				driver.findElement(By.id(label)).click();
				index++;
				
			}
		}
	}
	
	private void answerRowSelection(String question) {	
		
		String selection = "row_" + rowGroup.get(question);
		log.info("Selecting " + selection + " in page " + question);
		driver.findElement(By.id(selection)).click();
	}
	
	private void answerMessages(String question) {
		
		String message = messageGroup.get(question);
		log.info("Typing \"" + message + "\" in page " + question);
		driver.findElement(By.id("explanation")).sendKeys(message);
	}
	
	private void answerLanguageSelection(String question) {			
		
		for (WebElement language : driver.findElements(By.name("lang"))) {
			if (language.getAttribute("value").equals("EN")) {
				log.info("Selecting language \"EN\" in page " + question);
				language.click();				
			}
		}
	}
	
		
	private void answerPostalCode(String question) {
		
		log.info("Entering " + surveyInputs.getPostalCode() + " in page " + question);		
		driver.findElement(By.id("Postal")).sendKeys(surveyInputs.getPostalCode());
	}
	
	private void answerBirthYear(String question) {
		
		log.info("Entering " + String.valueOf(surveyInputs.getBirthYear()) + " in page " + question);
		driver.findElement(By.id("S2")).sendKeys(String.valueOf(surveyInputs.getBirthYear()));
		driver.findElement(By.id("row_1_img")).click();
	}
	
	private void answerToogleBox(String question) {
		if (toogleBoxGroup.contains(question)) {
			log.info("Entering row_1_img in page " + question);
			driver.findElement(By.id("row_1_img")).click();
		}
	}
	
	private void answerMath(String question) {
		
		String answer = "5";
		log.info("Entering " + answer + " in page " + question);
		driver.findElement(By.id("S3a")).sendKeys(answer);		
	}
	
	private void answerReceipt(String question) {
	
		
		delay();
		
		//Store NUmber
		log.info("Entering " + surveyInputs.getStoreNum() + " in page " + question);	
		driver.findElement(By.name("storeNum")).sendKeys(surveyInputs.getStoreNum());
		
		//TC#
		String tc = surveyInputs.getTc();
		log.info("Entering " + tc + " in page " + question);	
		driver.findElement(By.name("TCNum1")).sendKeys(tc.substring(0, 4));
		driver.findElement(By.name("TCNum2")).sendKeys(tc.substring(4, 8));
		driver.findElement(By.name("TCNum3")).sendKeys(tc.substring(8, 12));
		driver.findElement(By.name("TCNum4")).sendKeys(tc.substring(12, 16));
		driver.findElement(By.name("TCNum5")).sendKeys(tc.substring(16));
		
		//date  mm/dd/yy
		log.info("Entering " + surveyInputs.getDate() + " in page " + question);	
		String[] dateElements = surveyInputs.getDate().split("/");
		driver.findElement(By.name("month")).sendKeys(dateElements[0]);
		driver.findElement(By.name("day")).sendKeys(dateElements[1]);
		driver.findElement(By.name("year")).sendKeys(dateElements[2]);
	}
	
	private void answerArrivalTime(String question) {
		
		String arrivalTime = setArrivalTime(surveyInputs.getTime());
		log.info("Entering " + arrivalTime + " in page " + question);		
		for (WebElement option : driver.findElements(By.tagName("option"))) {
			if (option.getText().equals(arrivalTime)) option.click();
		}
	}
	
	private void answerForm(String question) {
		
		delay();
		log.info("Filling form in page " + question);		
		driver.findElement(By.name("FirstName")).sendKeys(surveyInputs.getFirstName());
		driver.findElement(By.name("LastName")).sendKeys(surveyInputs.getLastName());
		driver.findElement(By.name("Street")).sendKeys(surveyInputs.getStreet());
		driver.findElement(By.name("City")).sendKeys(surveyInputs.getCity());
		driver.findElement(By.name("Email")).sendKeys(surveyInputs.getEmail());
		driver.findElement(By.name("Phone1")).sendKeys(surveyInputs.getPhone().substring(0, 3));
		driver.findElement(By.name("Phone2")).sendKeys(surveyInputs.getPhone().substring(3, 6));
		driver.findElement(By.name("Phone3")).sendKeys(surveyInputs.getPhone().substring(6));
		for (WebElement option : driver.findElements(By.tagName("option"))) {
			if (option.getText().equals("Ontario")) option.click();	
		}
		
	}
}
