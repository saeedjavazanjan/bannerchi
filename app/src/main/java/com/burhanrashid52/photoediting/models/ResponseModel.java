package com.burhanrashid52.photoediting.models;

import java.io.Serializable;

public class ResponseModel implements Serializable {
	private String designer;
	private String type;
	private String downloadCount;


	private String samples;
	private String name;
	private String headerURL;
	private int id;
	private String packageURL;
	private String ocassion;
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

	public String getHeaderURL(){
		return headerURL;
	}

	public int getId(){
		return id;
	}

	public String getPackageURL(){
		return packageURL;
	}

	public String getOcassion(){
		return ocassion;
	}
	public void setDownloadCount(String downloadCount) {
		this.downloadCount = downloadCount;
	}


}
