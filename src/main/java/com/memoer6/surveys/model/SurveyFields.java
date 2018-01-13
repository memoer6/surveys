package com.memoer6.surveys.model;



public class SurveyFields  {
	
			
	// Required by Jackson for JSON mapping
	SurveyFields() {			
	}
	
	private String storeName; 	
	private String firstName;
	private String lastName;
	private String gender;
	private int birthYear;
	private String street;	
	private String city;
	private String postalCode;
	private String email;
	private String phone;	
	private String tc;
	private String storeNum;
	private String date;
	private String time;
	
	//setter and getters are required by Jackson to add those fields in JSON	
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getBirthYear() {
		return birthYear;
	}
	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}	
	public String getTc() {
		return tc;
	}
	public void setTc(String tc) {
		this.tc = tc;
	}
	public String getStore() {
		return storeNum;
	}
	public void setStore(String storeNum) {
		this.storeNum = storeNum;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}		
	public String getStoreNum() {
		return storeNum;
	}
	public void setStoreNum(String storeNum) {
		this.storeNum = storeNum;
	}
	@Override
	public String toString() {
		return "SurveyFields [storeName=" + storeName + ", firstName=" + firstName + ", lastName="
				+ lastName + ", gender=" + gender + ", birthYear=" + birthYear + ", street=" + street + ", city=" + city
				+ ", postalCode=" + postalCode + ", email=" + email + ", phone=" + phone + ", tc=" + tc + ", store="
				+ storeNum + ", date=" + date + ", time=" + time + "]";
	}
	
		
	

}
