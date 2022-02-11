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

    private ActivityResultLauncher<Intent> bluetoothEnabler;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bleScanner;
    private BeaconCallback beaconCallback;
    private String token;
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
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

        bluetoothEnabler = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this::enable);

        registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                this::request).launch(Manifest.permission.ACCESS_FINE_LOCATION);
        Intent intent2 = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(intent2, REQUEST_DISCOVER_BT);

        ActivityCompat.requestPermissions(BluetoothActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);

    }

    private void request(boolean isGranted)
    {
        BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        bleScanner = bluetoothAdapter.getBluetoothLeScanner();
        beaconCallback = new BeaconCallback();




    }


    private void enable(ActivityResult activityResult)
    {
        if (activityResult.getResultCode() == RESULT_OK)
            scan();
    }

    private void scan(View view)
    {

        if (!bluetoothAdapter.isEnabled())
            bluetoothEnabler.launch(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
        else
            scan();
    }

    private void scan()
    {
        new Handler().postDelayed(this::stop, 30000);
        bleScanner.startScan(beaconCallback);
    }

    private void stop()
    {
        Executors.newSingleThreadExecutor().execute(this::send);
        bleScanner.stopScan(beaconCallback);

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

    private class BeaconCallback extends ScanCallback
    {


        @Override
        public void onScanResult(int callbackType, ScanResult result)
        {
//           //
        }
    }
}
