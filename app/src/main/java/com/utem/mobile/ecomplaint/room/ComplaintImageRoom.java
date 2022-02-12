package com.utem.mobile.ecomplaint.room;

import android.graphics.Bitmap;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ComplaintImageRoom {

    @PrimaryKey(autoGenerate = true)
    private int complaintImageID;
    private String image;
    private int complaintID;

    public int getComplaintImageID() {
        return complaintImageID;
    }

    public void setComplaintImageID(int complaintImageID) {
        this.complaintImageID = complaintImageID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getComplaintID() {
        return complaintID;
    }

    public void setComplaintID(int complaintID) {
        this.complaintID = complaintID;
    }
}

