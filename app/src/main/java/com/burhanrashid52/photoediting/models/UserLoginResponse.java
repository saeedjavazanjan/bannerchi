package com.burhanrashid52.photoediting.models;

import com.google.gson.annotations.SerializedName;

public class UserLoginResponse{

	@SerializedName("userData")
	private UserData userData;

	@SerializedName("token")
	private String token;

	public UserData getUserData(){
		return userData;
	}

	public String getToken(){
		return token;
	}
}