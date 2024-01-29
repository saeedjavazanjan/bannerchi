package com.burhanrashid52.photoediting.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "saved_packages")
public class SavedModel implements Serializable {
    @PrimaryKey(autoGenerate = false)
    private int id;
    private String designer;
    private String type;
    private String samples;
    private String name;
    private String headerURL;
    private String packageURL;
    private String ocassion;
    private int price;
    private String dowmloadCount;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDesigner() {
        return designer;
    }

    public String getType() {
        return type;
    }

    public String getSamples() {
        return samples;
    }

    public String getName() {
        return name;
    }

    public String getHeaderURL() {
        return headerURL;
    }

    public String getPackageURL() {
        return packageURL;
    }

    public String getOcassion() {
        return ocassion;
    }

    public String getDowmloadCount() {
        return dowmloadCount;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSamples(String samples) {
        this.samples = samples;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHeaderURL(String headerURL) {
        this.headerURL = headerURL;
    }

    public void setPackageURL(String packageURL) {
        this.packageURL = packageURL;
    }

    public void setOcassion(String ocassion) {
        this.ocassion = ocassion;
    }

    public void setDowmloadCount(String dowmloadCount) {
        this.dowmloadCount = dowmloadCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
