package com.burhanrashid52.photoediting.models;

import com.google.gson.annotations.SerializedName;

public class UserData{

	@SerializedName("phoneNumber")
	private String phoneNumber;

	@SerializedName("jobTitle")
	private String jobTitle;

	@SerializedName("name")
	private String name;

	@SerializedName("typeOfPage")
	private String typeOfPage;

	@SerializedName("id")
	private int id;

	public String getPhoneNumber(){
		return phoneNumber;
	}

	public String getJobTitle(){
		return jobTitle;
	}

	public String getName(){
		return name;
	}

	public String getTypeOfPage(){
		return typeOfPage;
	}

	public int getId(){
		return id;
	}
}