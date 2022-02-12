package com.utem.mobile.ecomplaint.model;

import android.graphics.Bitmap;

import java.io.InputStream;

public class Resident extends User{

    private int residentID=0;
    private String residentStatus;
    // to hold IC front and back image
    Bitmap frontImage;
    Bitmap backImage;


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

    public Bitmap getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(Bitmap frontImage) {
        this.frontImage = frontImage;
    }

    public Bitmap getBackImage() {
        return backImage;
    }

    public void setBackImage(Bitmap backImage) {
        this.backImage = backImage;
    }
}
