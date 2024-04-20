package com.burhanrashid52.photoediting.models;

import com.google.gson.annotations.SerializedName;

public class DownloadDetail {

	@SerializedName("UserPurchaseToken")
	private String userPurchaseToken;
	@SerializedName("PackName")
	private String packName;

	@SerializedName("PackId")
	private int packId;

	public DownloadDetail(String userPurchaseToken, String packName, int packId) {
		this.userPurchaseToken = userPurchaseToken;
		this.packName = packName;
		this.packId = packId;
	}

	public String getUserPurchaseToken(){
		return userPurchaseToken;
	}



	public String getPackName(){
		return packName;
	}

	public int getPackId(){
		return packId;
	}
}