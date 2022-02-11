package com.utem.mobile.ecomplaint;
import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.json.JSONObject;

import java.net.URL;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;


public class BluetoothActivity extends AppCompatActivity {

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //put layout
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                token = null;
            } else {
                token = extras.getString("token");
            }
        } else {
            token = (String) savedInstanceState.getSerializable("token");
        }



        //ActivityCompat.requestPermissions(BluetoothActivity.this,
          //      new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
            //    MY_PERMISSIONS_REQUEST_LOCATION);


        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION,false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                              //send();
                                Toast.makeText(this, "Haha", Toast.LENGTH_SHORT).show();
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                Toast.makeText(this, "Location permission should be enable!", Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(this, "No location access granted!", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                locationPermissionRequest.launch(new String[] {
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
});

    }


    private void send()
    {
        try
        {
            JSONObject request = new JSONObject();
            HttpsURLConnection connection = (HttpsURLConnection) new URL(
                    ""// + sub
                            + "/telemetry").openConnection();

            request.put("token", token);

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.getOutputStream().write(request.toString().getBytes());

            if (connection.getResponseCode() == 200)
                System.out.println("Success");
            else
                System.out.println("Fail");

            connection.disconnect();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
