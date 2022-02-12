package com.utem.mobile.ecomplaint;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.utem.mobile.ecomplaint.model.Resident;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class RegisterLoader extends AsyncTaskLoader<Bundle> {

    private final Resident resident;
    private final String apiConnect;


    public RegisterLoader(@NonNull Context context, Resident newResident) {
        super(context);
      this.resident = newResident;
        this.apiConnect = context.getString(R.string.api_connect);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public Bundle loadInBackground() {
        Bundle response = null;

        try {
            JSONObject request = new JSONObject();
            String token = null;

            request.put("UserName", resident.getUserName());
            request.put("UserPassword", resident.getPassword());
            request.put("UserIC", resident.getUserIC());
            request.put("UserPhone",resident.getUserPhone());
            request.put("UserProfileName",resident.getUserProfileName());
            request.put("Role","resident");

            HttpsURLConnection connection = (HttpsURLConnection)
                    new URL(apiConnect + "/register.jsp").openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.getOutputStream().write(request.toString().getBytes());

            if (connection.getResponseCode() == 200) {
                System.out.println("200");
                response = new Bundle();
                JSONObject resp = new JSONObject(new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.joining()));
                token = response.getString("Token");

                response.putString("Token", resp.getString("Token"));
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    }

