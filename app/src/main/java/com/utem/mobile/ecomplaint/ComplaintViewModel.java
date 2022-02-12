package com.utem.mobile.ecomplaint;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.utem.mobile.ecomplaint.room.Complaint;

import java.util.List;
import java.util.concurrent.Executors;

public class ComplaintViewModel extends AndroidViewModel {
    private final ComplaintManager complaintManager;
    private LiveData<List<Complaint>> complaintLiveData;
    public ComplaintViewModel(@NonNull Application application) {
        super(application);

        complaintManager = ComplaintDatabase.getInstance(application.getApplicationContext()).getComplaintManager();
    }

    public void addComplaint(Complaint complaint){
        Executors.newSingleThreadExecutor().execute(()-> complaintManager.addComplaint(complaint));
    }

    public LiveData<List<Complaint>> getAllComplaint(){
        if(complaintLiveData == null){
            complaintLiveData = complaintManager.getAllComplaint();
        }
        return complaintLiveData;
    }
}
