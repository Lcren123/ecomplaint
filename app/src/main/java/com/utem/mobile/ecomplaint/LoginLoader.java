package com.utem.mobile.ecomplaint;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class LoginLoader extends AsyncTaskLoader<Bundle> {

    private final String username, password;
    private final String apiConnect;

    public LoginLoader(@NonNull Context context, String userName, String Password) {
        super(context);

        this.username = userName;
        this.password = Password;
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

            request.put("username", username);
            request.put("password", password);
            HttpsURLConnection connection = (HttpsURLConnection)
                    new URL(apiConnect + "/login.jsp").openConnection();

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
