package com.utem.mobile.ecomplaint;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;


import com.utem.mobile.ecomplaint.model.Complaint;
import com.utem.mobile.ecomplaint.room.ComplaintImageRoom;
import com.utem.mobile.ecomplaint.room.ComplaintRoom;

import java.util.List;

@Dao
public abstract class ComplaintManager {

    @Insert
    public abstract long addComplaint(ComplaintRoom complaint);

    @Query("SELECT * FROM ComplaintRoom")
    public abstract LiveData<List<ComplaintRoom>> getAllComplaint();

    @Query("SELECT * FROM ComplaintImageRoom WHERE complaintID = :complaintID")
    public abstract List<ComplaintImageRoom> getComplaintImages(int complaintID);

    @Query("SELECT * FROM ComplaintRoom WHERE isConnectedToDatabase = :isConnectedToDatabase")
    public abstract List<ComplaintRoom> getLocalComplaint(boolean isConnectedToDatabase);

    @Query("UPDATE ComplaintRoom SET isConnectedToDatabase = :isConnectedToDatabase WHERE complaintID = :complaintID")
    public abstract void upDateLocalFinish(boolean isConnectedToDatabase, int complaintID);

    @Query("DELETE FROM complaintRoom")
    public abstract void deleteAllComplaint();

    @Insert
    public abstract void addComplaintImage(ComplaintImageRoom complaintImage);

    @Transaction
    public void addComplaintAndImage(ComplaintRoom complaint, List<ComplaintImageRoom> images){
        long complaintID = addComplaint(complaint);
        for(ComplaintImageRoom image: images){
            image.setComplaintID((int)complaintID);
            addComplaintImage(image);
        }
    }



}
