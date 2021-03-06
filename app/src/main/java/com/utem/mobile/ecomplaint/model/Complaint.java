package com.utem.mobile.ecomplaint.model;

import android.graphics.Bitmap;

import java.util.List;

public class Complaint {

    private ComplaintCategory category;
    private Resident resident;

    private int complaintID = 0;
    private String complaintTitle;
    private String complaintDescription;
    private double complaintLongitude;
    private double complaintLatitude;
    private String complaintStatus="Pending";
    private String complaintDateTime;
    private List <ComplaintImage> imageList;

    public ComplaintCategory getCategory() {
        return category;
    }

    public void setCategory(ComplaintCategory category) {
        this.category = category;
    }

    public Resident getResident() {
        return resident;
    }

    public void setResident(Resident resident) {
        this.resident = resident;
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
    public List<ComplaintImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<ComplaintImage> imageList) {
        this.imageList = imageList;
    }


}
