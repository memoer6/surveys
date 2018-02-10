package com.memoer6.surveys.firebase;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

@Service
public class RestClient {
	
	private final static Logger log = LoggerFactory.getLogger(RestClient.class);
	
	@Value("${surveys.restClient.scopes}")
	private String scopes;
	
	@Value("${surveys.restClient.firebaseUri}")
	private String firebaseUri;
	
	@Value("${surveys.restClient.serviceAccFile}")
	private String serviceAccFile;

	private final RestTemplate restTemplate;
	private final HttpHeaders headers;
	private String accessToken;
	
	public RestClient(RestTemplateBuilder restTemplateBuilder) {
	        this.restTemplate = restTemplateBuilder.build();
	        headers = new HttpHeaders();
	    }
	
	public ResponseEntity<String> sendMessage(String messageStr) {
		
		try {
			accessToken = getAccessToken();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + accessToken);
		HttpEntity<String> request = new HttpEntity<>(messageStr, headers);
				
		ResponseEntity<String> response = restTemplate.postForEntity(firebaseUri, request, String.class);
		log.info(response.toString());
		return response;
		
	}

	
	// get Access token
	private String getAccessToken() throws IOException {
	  GoogleCredential googleCredential = GoogleCredential
	      .fromStream(new FileInputStream(serviceAccFile))
	      .createScoped(Arrays.asList(scopes));
	  googleCredential.refreshToken();
	  log.info(googleCredential.getAccessToken());
	  return googleCredential.getAccessToken();
	}
	
	
}
