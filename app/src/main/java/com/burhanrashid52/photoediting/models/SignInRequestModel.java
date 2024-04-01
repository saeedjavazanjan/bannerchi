package com.burhanrashid52.photoediting.models;

import com.google.gson.annotations.SerializedName;

public class SignInRequestModel{

	@SerializedName("PhoneNumber")
	private String phoneNumber;

	@SerializedName("Password")
	private String password;

	public SignInRequestModel(String phoneNumber, String password) {
		this.phoneNumber = phoneNumber;
		this.password = password;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}
	public String getPassword(){return password;}
}