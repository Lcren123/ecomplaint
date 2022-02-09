package com.utem.mobile.ecomplaint.model;

public class Complaint {

    private ComplaintCategory category;
    private Resident resident;

    private int complaintID=0;
    private String complaintDescription;
    private long complaintLongitude;
    private long complaintLatitude;
    private String complaintStatus;
    private String complaintDateTime;

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

    public String getComplaintDescription() {
        return complaintDescription;
    }

    public void setComplaintDescription(String complaintDescription) {
        this.complaintDescription = complaintDescription;
    }

    public long getComplaintLongitude() {
        return complaintLongitude;
    }

    public void setComplaintLongitude(long complaintLongitude) {
        this.complaintLongitude = complaintLongitude;
    }

    public long getComplaintLatitude() {
        return complaintLatitude;
    }

    public void setComplaintLatitude(long complaintLatitude) {
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
