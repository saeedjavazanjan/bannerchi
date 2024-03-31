package com.burhanrashid52.photoediting.models;

import com.google.gson.annotations.SerializedName;

public class AdUserDtoModel{

	@SerializedName("UserName")
	private String userName;

	@SerializedName("PhoneNumber")
	private String phoneNumber;

	@SerializedName("TypeOfPage")
	private String typeOfPage;

	@SerializedName("JobTitle")
	private String jobTitle;

	@SerializedName("Password")
	private String password;

	public AdUserDtoModel(String userName, String phoneNumber, String typeOfPage, String jobTitle, String password) {
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.typeOfPage = typeOfPage;
		this.jobTitle = jobTitle;
		this.password = password;
	}


	public String getUserName(){
		return userName;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}

	public String getTypeOfPage(){
		return typeOfPage;
	}

	public String getJobTitle(){
		return jobTitle;
	}

	public String getPassword(){
		return password;
	}
}