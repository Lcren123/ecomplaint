package com.utem.mobile.ecomplaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.utem.mobile.ecomplaint.model.Complaint;
import com.utem.mobile.ecomplaint.model.ComplaintCategory;
import com.utem.mobile.ecomplaint.model.ComplaintImage;
import com.utem.mobile.ecomplaint.model.Resident;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class ForumLoader extends AsyncTaskLoader<List<Complaint>> {

    private final String apiConnect;


    public ForumLoader(@NonNull Context context) {
        super(context);
        this.apiConnect = context.getString(R.string.api_connect);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Complaint> loadInBackground() {
        List<Complaint> complaints = null;

        try {
            HttpsURLConnection connection = (HttpsURLConnection)
                    new URL(apiConnect + "/getAllComplaint.jsp").openConnection();

            connection.setRequestMethod("GET");

            System.out.println(connection.getResponseCode());

            if (connection.getResponseCode() == 200) {
                complaints = new ArrayList<>();

                JSONArray response = new JSONArray(new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.joining()));
                for(int i = 0; i < response.length(); i++){
                    Complaint complaint = new Complaint();
                    complaint.setComplaintID(response.getJSONObject(i).getInt("ComplaintID"));
                    complaint.setComplaintTitle(response.getJSONObject(i).getString("ComplaintTitle"));
                    complaint.setComplaintDescription(response.getJSONObject(i).getString("ComplaintDescription"));
                    complaint.setComplaintLongitude(response.getJSONObject(i).getDouble("ComplaintLongitude"));
                    complaint.setComplaintLatitude(response.getJSONObject(i).getDouble("ComplaintLatitude"));
                    complaint.setComplaintStatus(response.getJSONObject(i).getString("ComplaintStatus"));
                    complaint.setComplaintDateTime(response.getJSONObject(i).getString("ComplaintDateTime"));

                    Resident resident = new Resident();
                    resident.setResidentID(response.getJSONObject(i).getInt("ResidentID"));
                    resident.setUserID(response.getJSONObject(i).getInt("UserID"));
                    resident.setUserProfileName(response.getJSONObject(i).getString("UserProfileName"));
                    complaint.setResident(resident);

                    ComplaintCategory complaintCategory = new ComplaintCategory();
                    complaintCategory.setCategoryName(response.getJSONObject(i).getString("CategoryName"));
                    complaint.setCategory(complaintCategory);

                    JSONArray complaintImages = response.getJSONObject(i).getJSONArray("ComplaintImages");

                    List<ComplaintImage> complaintImageList = new ArrayList<>();
                    for(int j = 0; j < complaintImages.length(); j++){
                        JSONObject imageJSON = complaintImages.getJSONObject(j);
                        ComplaintImage complaintImage = new ComplaintImage();
                        Base64.Decoder decoder = Base64.getDecoder();
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decoder.decode(imageJSON.getString("ComplaintImage")));
                        Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
                        complaintImage.setBitmap(bitmap);
                        complaintImageList.add(complaintImage);
                    }
                    complaint.setImageList(complaintImageList);
                    complaints.add(complaint);
                }
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return complaints;
    }
}
