package com.utem.mobile.ecomplaint;

import static android.os.Environment.getExternalStoragePublicDirectory;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;
    private ImageView captureButton;
    private Button finishButton;
    private ArrayList<String> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        captureButton = findViewById(R.id.captureButton);
        finishButton = findViewById(R.id.finishButton);
        
        finishButton.setOnClickListener(this::finish);

        captureButton.setOnClickListener(this::captureImage);

        registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),this::startCamera)
        .launch(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }

    private void finish(View view) {
        if(imageList != null) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra("imageList", imageList);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    private void startCamera(Map<String, Boolean> stringBooleanMap) {
        if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            cameraProviderFuture = ProcessCameraProvider.getInstance(this);
            cameraProviderFuture.addListener(this::startCamera, ContextCompat.getMainExecutor(this));
        }
    }


    private void startCamera() {
        try {
            ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
            PreviewView previewView = findViewById(R.id.previewCamera);
            Preview preview = new Preview.Builder().build();

            // Set up the capture use case to allow users to take photos
            imageCapture = new ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build();


            // Choose the camera by requiring a lens facing
            CameraSelector cameraSelector = new CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build();

            // Connect the preview use case to the previewView
            Preview.SurfaceProvider surface = previewView.getSurfaceProvider();
            preview.setSurfaceProvider(previewView.getSurfaceProvider());
            cameraProvider.unbindAll();

            // Attach use cases to the camera with the same lifecycle owner
            Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

            // For performing operations that affect all outputs.
            CameraControl cameraControl = camera.getCameraControl();

            // For querying information and states.
            CameraInfo cameraInfo = camera.getCameraInfo();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void captureImage(View view) {

        File storageDirectory = getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        System.out.println(storageDirectory.toString());
        String fileName = "Complaint_JPEG_" + timeStamp + ".jpg";

        File photoFile = new File(storageDirectory, fileName );
        System.out.println(photoFile.toString());

        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(photoFile).build();


        imageCapture.takePicture( outputFileOptions,Executors.newSingleThreadExecutor() ,
                new ImageCapture.OnImageSavedCallback() {

                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        System.out.println("success");
                        if(imageList == null){
                            imageList = new ArrayList<>();
                        }
                        imageList.add(Uri.fromFile(photoFile).toString());
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        exception.printStackTrace();
                        System.out.println("fail");
                    }
                });
    }

    public void onClick() {

    }
}