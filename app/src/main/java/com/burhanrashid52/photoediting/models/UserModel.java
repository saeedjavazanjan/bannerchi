package com.burhanrashid52.photoediting.models;

import com.google.gson.annotations.SerializedName;

public class UserModel{

	@SerializedName("PhoneNumber")
	private String phoneNumber;

	@SerializedName("TypeOfPage")
	private String typeOfPage;

	@SerializedName("JobTitle")
	private String jobTitle;

	@SerializedName("Name")
	private String name;

	public UserModel(String phoneNumber, String typeOfPage, String jobTitle, String name) {
		this.phoneNumber = phoneNumber;
		this.typeOfPage = typeOfPage;
		this.jobTitle = jobTitle;
		this.name = name;
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

	public String getName(){
		return name;
	}
}