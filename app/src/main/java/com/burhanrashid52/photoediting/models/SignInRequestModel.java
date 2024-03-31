package com.burhanrashid52.photoediting.models;

import com.google.gson.annotations.SerializedName;

public class SignInRequestModel{

	@SerializedName("PhoneNumber")
	private String phoneNumber;

	public SignInRequestModel(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}
}