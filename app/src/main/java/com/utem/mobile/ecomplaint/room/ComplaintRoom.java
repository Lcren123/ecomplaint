package com.utem.mobile.ecomplaint.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ComplaintRoom {

    private String categoryName;
    private String username;

    @PrimaryKey(autoGenerate = true)
    private int complaintID = 0;
    private String complaintTitle;
    private String complaintDescription;
    private double complaintLongitude;
    private double complaintLatitude;
    private String complaintStatus;
    private String complaintDateTime;
    private boolean isConnectedToDatabase;

    public boolean isConnectedToDatabase() {
        return isConnectedToDatabase;
    }

    public void setConnectedToDatabase(boolean connectedToDatabase) {
        isConnectedToDatabase = connectedToDatabase;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getComplaintID() {
        return complaintID;
    }

    public void setComplaintID(int complaintID) {
        this.complaintID = complaintID;
    }

    public String getComplaintTitle() {return complaintTitle;}

    public void setComplaintTitle(String complaintTitle) {
        this.complaintTitle = complaintTitle;
    }

    public String getComplaintDescription() {
        return complaintDescription;
    }

    public void setComplaintDescription(String complaintDescription) {
        this.complaintDescription = complaintDescription;
    }

    public double getComplaintLongitude() {
        return complaintLongitude;
    }

    public void setComplaintLongitude(double complaintLongitude) {
        this.complaintLongitude = complaintLongitude;
    }

    public double getComplaintLatitude() {
        return complaintLatitude;
    }

    public void setComplaintLatitude(double complaintLatitude) {
        this.complaintLatitude = complaintLatitude;
    }

    public String getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(String complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

    public String getComplaintDateTime() {
        return complaintDateTime;
    }

    public void setComplaintDateTime(String complaintDateTime) {
        this.complaintDateTime = complaintDateTime;
    }

}

