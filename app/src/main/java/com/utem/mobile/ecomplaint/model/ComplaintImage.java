package com.utem.mobile.ecomplaint.model;

import android.graphics.Bitmap;

import java.io.InputStream;

public class ComplaintImage {

    private int complaintImageID=0;
    private Bitmap bitmap ;

    public int getComplaintImageID() {
        return complaintImageID;
    }

    public void setComplaintImageID(int complaintImageID) {
        this.complaintImageID = complaintImageID;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }




}
