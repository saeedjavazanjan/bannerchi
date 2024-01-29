package com.burhanrashid52.photoediting.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Downloaded_Packages")
public class LocalModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String designer;
    private String type;
    private String samples;
    private String name;
    private String headerURI;
    private String packageURI;
    private String ocassion;

    public int getId() {
        return id;
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

    public String getHeaderURI() {
        return headerURI;
    }

    public String getPackageURI() {
        return packageURI;
    }

    public String getOcassion() {
        return ocassion;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setHeaderURI(String headerURI) {
        this.headerURI = headerURI;
    }

    public void setPackageURI(String packageURI) {
        this.packageURI = packageURI;
    }

    public void setOcassion(String ocassion) {
        this.ocassion = ocassion;
    }
}
