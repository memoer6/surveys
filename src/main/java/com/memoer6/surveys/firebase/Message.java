package com.memoer6.surveys.firebase;

import com.fasterxml.jackson.annotation.JsonRootName;

//specify the name of the root wrapper to be used when converting object to JSON
@JsonRootName(value = "message")
public class Message {
	
	private String token;
	private Notification notification;
	
	public Message(String token) {
		this.token = token;		
	}
	
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Notification getNotification() {
		return notification;
	}
	public void setNotification(Notification notification) {
		this.notification = notification;
	}
	
	@Override
	public String toString() {
		return "Message [token=" + token + ", notification=" + notification + "]";
	}
	
	
	public class Notification {
		
		private String body;
		private String title;
			
		public Notification(String title, String body) {
			this.body = body;
			this.title = title;
		}
		
		
		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}

		@Override
		public String toString() {
			return "Notification [body=" + body + ", title=" + title + "]";
		}
	
		
	}

}
