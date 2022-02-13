package com.utem.mobile.ecomplaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;

import com.utem.mobile.ecomplaint.model.Complaint;
import com.utem.mobile.ecomplaint.model.ComplaintCategory;
import com.utem.mobile.ecomplaint.model.ComplaintImage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;


public class ForumDetailLoader extends AsyncTaskLoader<Complaint> {

    private final int complaintID;
    private final String apiConnect;

    public ForumDetailLoader(@NonNull Context context, int complaintID) {
        super(context);
        this.complaintID = complaintID;
        this.apiConnect = context.getString(R.string.api_connect);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public Complaint loadInBackground() {
        Complaint complaint = null;
        try {
            HttpsURLConnection connection = (HttpsURLConnection)
                    new URL(apiConnect + "/getSpecificComplaint.jsp").openConnection();

            JSONObject request = new JSONObject();
            request.put("ComplaintID",complaintID);

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.getOutputStream().write(request.toString().getBytes());

            System.out.println(connection.getResponseCode());

            if (connection.getResponseCode() == 200) {
                complaint = new Complaint();
                JSONObject response = new JSONObject(new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.joining()));
                    complaint.setComplaintID(response.getInt("ComplaintID"));
                    complaint.setComplaintTitle(response.getString("ComplaintTitle"));
                    complaint.setComplaintDateTime(response.getString("ComplaintDateTime"));
                    complaint.setComplaintDescription(response.getString("ComplaintDescription"));
                    complaint.setComplaintLongitude(response.getDouble("ComplaintLongitude"));
                    complaint.setComplaintLatitude(response.getDouble("ComplaintLatitude"));

                    ComplaintCategory complaintCategory = new ComplaintCategory();
                    complaintCategory.setCategoryName(response.getString("CategoryName"));
                    complaint.setCategory(complaintCategory);

                    JSONArray complaintImages = response.getJSONArray("ComplaintImages");

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
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return complaint;
    }
}
