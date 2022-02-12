package com.utem.mobile.ecomplaint;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.utem.mobile.ecomplaint.room.Complaint;

import java.util.List;

@Dao
public interface ComplaintManager {

    @Insert
    void addComplaint(Complaint complaint);

    @Query("SELECT * FROM Complaint")
    LiveData<List<Complaint>> getAllComplaint();
}
