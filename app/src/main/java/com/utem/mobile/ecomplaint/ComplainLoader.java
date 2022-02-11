package com.utem.mobile.ecomplaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslCertificate;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import androidx.annotation.NonNull;

import com.utem.mobile.ecomplaint.model.Complaint;
import com.utem.mobile.ecomplaint.model.ComplaintImage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class ComplainLoader extends AsyncTaskLoader<Integer> {

    private final Complaint complaint;
    private final String apiConnect;

    public ComplainLoader(@NonNull Context context, Complaint complaint){
        super(context);
        this.complaint = complaint;
        this.apiConnect = context.getString(R.string.api_connect);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public Integer loadInBackground() {
        Integer result = null;

        try {
            JSONObject request = new JSONObject();
            String token = null;

            List<ComplaintImage> images = complaint.getImageList();
            List<String> encodedImages = null;

            if(images != null)
                encodedImages = new ArrayList<>();
            for(ComplaintImage image : images){
                Bitmap bitmap = image.getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                Base64.Encoder encoder = Base64.getEncoder();
                String encodeImage = encoder.encodeToString(byteArray);
                encodedImages.add(encodeImage);
            }
            JSONArray imagesJSON = new JSONArray(encodedImages);
            encodedImages.size();
            request.put("imageList",imagesJSON);
            int i = 0;
            Integer.toString(i);

            HttpsURLConnection connection = (HttpsURLConnection)
                    new URL(apiConnect + "/addComplaint.jsp").openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.getOutputStream().write(request.toString().getBytes());

            System.out.println(connection.getResponseCode());
            if (connection.getResponseCode() == 200) {
                System.out.println("200");

                JSONObject resp = new JSONObject(new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.joining()));
                result = resp.getInt("result");
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
