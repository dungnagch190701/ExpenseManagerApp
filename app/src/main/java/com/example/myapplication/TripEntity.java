package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TripEntity implements Serializable {
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTripName() {
        return tripName;
    }
    public void setTripName(String tripName) {
        this.tripName = tripName;
    }
    public String getTripDest() {
        return tripDest;
    }
    public void setTripDest(String tripDest) {
        this.tripDest = tripDest;
    }
    public String getTripRisk() {
        return tripRisk;
    }
    public void setTripRisk(String tripRisk) {
        this.tripRisk = tripRisk;
    }
    public String getTripDesc() {
        return tripDesc;
    }
    public void setTripDesc(String tripDesc) {
        this.tripDesc = tripDesc;
    }
    public String getTripDept() {
        return tripDept;
    }
    public void setTripDept(String tripDept) {
        this.tripDept = tripDept;
    }
    public String getTripDateFrom() {
        return tripDateFrom;
    }
    public void setTripDateFrom(String tripDateFrom) {
        this.tripDateFrom = tripDateFrom;
    }
    public String getTripDateTo() {
        return tripDateTo;
    }
    public void setTripDateTo(String tripDateTo) {
        this.tripDateTo = tripDateTo;
    }

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String tripName;
    @SerializedName("dept")
    private String tripDept;
    @SerializedName("dest")
    private String tripDest;
    @SerializedName("date_from")
    private String tripDateFrom;
    @SerializedName("date_to")
    private String tripDateTo;
    @SerializedName("risk")
    private String tripRisk;
    @SerializedName("desc")
    private String tripDesc;



    public TripEntity(int id, String tripName, String tripDept, String tripDest, String tripDateFrom,String tripDateTo, String tripRisk, String tripDesc) {
        this.id = id;
        this.tripName = tripName;
        this.tripDept = tripDept;
        this.tripDest = tripDest;
        this.tripDateFrom = tripDateFrom;
        this.tripDateTo = tripDateTo;
        this.tripRisk = tripRisk;
        this.tripDesc = tripDesc;

    }
}
