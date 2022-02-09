package com.utem.mobile.ecomplaint.model;

import java.io.InputStream;

public class ComplaintImage {

    private Complaint complaint;

    private int complaintImageID=0;
    private String base64Image;
    private InputStream input;

    public Complaint getComplaint() {
        return complaint;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    public int getComplaintImageID() {
        return complaintImageID;
    }

    public void setComplaintImageID(int complaintImageID) {
        this.complaintImageID = complaintImageID;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public InputStream getInput() {
        return input;
    }

    public void setInput(InputStream input) {
        this.input = input;
    }




}
