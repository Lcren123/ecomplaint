package com.utem.mobile.ecomplaint.model;

import java.io.InputStream;

public class Resident extends User{

    private int residentID=0;
    private String residentStatus;
    // to hold IC front and back image
    private String base64Front;
    private String base64Back;
    private InputStream frontInputStream;
    private InputStream backInputStream;

    public int getResidentID() {
        return residentID;
    }

    public void setResidentID(int residentID) {
        this.residentID = residentID;
    }

    public String getResidentStatus() {
        return residentStatus;
    }

    public void setResidentStatus(String residentStatus) {
        this.residentStatus = residentStatus;
    }

    public String getBase64Front() {
        return base64Front;
    }

    public void setBase64Front(String base64Front) {
        this.base64Front = base64Front;
    }

    public String getBase64Back() {
        return base64Back;
    }

    public void setBase64Back(String base64Back) {
        this.base64Back = base64Back;
    }

    public InputStream getFrontInputStream() {
        return frontInputStream;
    }

    public void setFrontInputStream(InputStream frontInputStream) {
        this.frontInputStream = frontInputStream;
    }

    public InputStream getBackInputStream() {
        return backInputStream;
    }

    public void setBackInputStream(InputStream backInputStream) {
        this.backInputStream = backInputStream;
    }

}
