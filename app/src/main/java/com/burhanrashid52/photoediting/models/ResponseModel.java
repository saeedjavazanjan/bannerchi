package com.burhanrashid52.photoediting.models;

import java.io.Serializable;

public class ResponseModel implements Serializable {
	private String designer;
	private String type;
	private String downloadCount;

	private String samples;
	private String name;
	private String headerUrl;
	private int id;
	private String packageUrl;
	private String category;
	private int price;

	public int getPrice() {
		return price;
	}

	public String getDesigner(){
		return designer;
	}

	public String getType(){
		return type;
	}

	public String getDownloadCount(){
		return downloadCount;
	}

	public String getSamples(){
		return samples;
	}

	public String getName(){
		return name;
	}

	public String getHeaderUrl(){
		return headerUrl;
	}

	public int getId(){
		return id;
	}

	public String getPackageUrl(){
		return packageUrl;
	}

	public String getOcassion(){
		return category;
	}
	public void setDownloadCount(String downloadCount) {
		this.downloadCount = downloadCount;
	}


}
