package com.memoer6.surveys.firebase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

// Class that sends the message to the device via Firebase Cloud Messaging
//Messenger has to be autowired in order to autowire RestClient. It cannot be instantiate manually
@Service
public class Messenger {
	
	private final static Logger log = LoggerFactory.getLogger(Messenger.class);
	
	@Autowired
	private RestClient restClient;
	
	//This is also called FCM registration token, and it can be used to send messages from Firebase console (Notifications)
	@Value("${surveys.messenger.deviceToken}")
	private String deviceToken;
	
	
	public void sendMessage(String textMessage) {
		
		//Create Message object
		Message messageObj = new Message(deviceToken);
		messageObj.setNotification(messageObj.new Notification("Survey result", textMessage));
		
		//Convert JSON to String
		ObjectMapper mapper = new ObjectMapper();
		//Enable to have "message" key as the root JSON value in the serialization
		mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
		
		String messageStr;
		try {
			messageStr = mapper.writeValueAsString(messageObj);
		} catch (JsonProcessingException e) {			
			messageStr = null;
			e.printStackTrace();
		}
		
		log.info(messageStr);			
		
		restClient.sendMessage(messageStr);
		
	}

}
