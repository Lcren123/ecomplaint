package com.utem.mobile.ecomplaint;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class RegisterLoader extends AsyncTaskLoader<Bundle> {

    private final String name, profileName, IcNo, phoneNo, password, apiConnect;


    public RegisterLoader(@NonNull Context context, String name, String profileName, String IcNo, String phoneNo, String password) {
        super(context);
        this.name = name;
        this.profileName = profileName;
        this.IcNo = IcNo;
        this.phoneNo = phoneNo;
        this.password = password;
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

            request.put("UserName", name);
            request.put("UserPassword", password);
            request.put("UserIC", IcNo);
            request.put("UserPhone",phoneNo);
            request.put("UserProfileName",profileName);
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

