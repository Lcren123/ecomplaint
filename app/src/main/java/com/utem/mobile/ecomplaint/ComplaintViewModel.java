package com.utem.mobile.ecomplaint;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.utem.mobile.ecomplaint.model.Complaint;
import com.utem.mobile.ecomplaint.model.ComplaintCategory;
import com.utem.mobile.ecomplaint.model.ComplaintImage;
import com.utem.mobile.ecomplaint.room.ComplaintImageRoom;
import com.utem.mobile.ecomplaint.room.ComplaintRoom;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class ComplaintViewModel extends AndroidViewModel {
    private final ComplaintManager complaintManager;
    private LiveData<List<ComplaintRoom>> complaintLiveData;
    public ComplaintViewModel(@NonNull Application application) {
        super(application);

        complaintManager = ComplaintDatabase.getInstance(application.getApplicationContext()).getComplaintManager();
    }

    public void addComplaint(ComplaintRoom complaint, List<ComplaintImageRoom> complaintImages){
        Executors.newSingleThreadExecutor().execute(()-> {
            complaintManager.addComplaintAndImage(complaint,complaintImages);
        });
    }

    public LiveData<List<ComplaintRoom>> getAllComplaint(){
        if(complaintLiveData == null){
            complaintLiveData = complaintManager.getAllComplaint();
        }
        return complaintLiveData;
    }

    public List<Complaint> getLocalComplaints() {
        List<Complaint> complaints = new ArrayList<>();
        List<ComplaintRoom> complaintRooms = complaintManager.getLocalComplaint(0);
        for (ComplaintRoom complaintRoom : complaintRooms) {
            Complaint complaint = new Complaint();
            complaint.setComplaintTitle(complaintRoom.getComplaintTitle());
            complaint.setComplaintDescription(complaintRoom.getComplaintDescription());
            complaint.setComplaintLongitude(complaintRoom.getComplaintLongitude());
            complaint.setComplaintLatitude(complaintRoom.getComplaintLatitude());
            complaint.setComplaintStatus(complaintRoom.getComplaintStatus());
            complaint.setComplaintDateTime(complaintRoom.getComplaintDateTime());

            ComplaintCategory complaintCategory = new ComplaintCategory();
            complaintCategory.setComplaintCategoryID(complaintRoom.getCategoryID());
            complaint.setCategory(complaintCategory);

            List<ComplaintImageRoom> imageRooms = complaintManager.getComplaintImages(complaintRoom.getComplaintID());
            List<ComplaintImage> images = new ArrayList<>();
            for (ComplaintImageRoom roomImage : imageRooms) {
                ComplaintImage complaintImage = new ComplaintImage();
                //complaintImage.setBitmap();
                String encodedImage = roomImage.getImage();
                Base64.Decoder decoder = Base64.getDecoder();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decoder.decode(encodedImage));
                Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
                complaintImage.setBitmap(bitmap);
                images.add(complaintImage);
            }
            complaint.setImageList(images);
            complaints.add(complaint);
            complaintManager.upDateLocalFinish(true, complaintRoom.getComplaintID());
        }
        return complaints;

    }

    public void addAllComplaint(HashMap<ComplaintRoom,List<ComplaintImageRoom>> complaintMap){
        Executors.newSingleThreadExecutor().execute(()-> {
            //complaintManager.addComplaintAndImage(complaint,complaintImages);
        });
    }


}
