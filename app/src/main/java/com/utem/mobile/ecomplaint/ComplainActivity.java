package com.utem.mobile.ecomplaint;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.utem.mobile.ecomplaint.model.Complaint;
import com.utem.mobile.ecomplaint.model.ComplaintImage;
import com.utem.mobile.ecomplaint.model.ViewPagerAdapter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ComplainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Integer> {

    private Button btnSubmit, btnGallery, btnCamera;
    private EditText txtTitle, txtDescription;
    private TextView txtLocation;

    private List<Uri> imagesUri;
    private LoaderManager loaderManager;

    private int PHOTO_FROM_GALLERY = 1;
    private int position = 0;
    private List<ComplaintImage> complaintImageList;
    private Complaint complaint;

    // creating object of ViewPager for photo
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    // creating obj for google map
    private int LOCATION_REQUEST_CODE = 2;
    private FusedLocationProviderClient client;
    private SupportMapFragment supportMapFragment;
    private LatLng latLng;

    private ActivityResultLauncher<Intent> cameraLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);

        txtTitle = findViewById(R.id.txtTitle);
        txtDescription = findViewById(R.id.txtDescription);
        txtLocation = findViewById(R.id.txtLocation);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnGallery = findViewById(R.id.btnGallery);
        viewPager = findViewById(R.id.viewPager);
        btnCamera = findViewById(R.id.btnGCamera);

        imagesUri = new ArrayList<>();
        complaintImageList = new ArrayList<>();
        complaint = new Complaint();
        loaderManager = LoaderManager.getInstance(this);
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::cameraResult);

        /*
        // initialize map fragment
        Fragment fragment = new MapFragment();

        // open fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayoutMap,fragment)
                .commit();*/

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
        client = LocationServices.getFusedLocationProviderClient(this);

        // check google map permission
        if (ActivityCompat.checkSelfPermission(ComplainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(ComplainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ComplainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);

        } else {
            // when permission granted get current location
            getCurrentLocation();
            supportMapFragment.getMapAsync(googleMap -> googleMap.setOnMapClickListener(latLng -> {
                // when click on map
                // initialize marker options
                MarkerOptions markerOptions = new MarkerOptions();
                //set position of marker
                markerOptions.position(latLng);
                //set title of marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                // remove all marker
                googleMap.clear();
                // animating to zoom the marker
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                // add marker on map
                googleMap.addMarker(markerOptions);
                txtLocation.setText(latLng.latitude + "," + latLng.longitude);

            }));
        }


        btnCamera.setOnClickListener(this::openCamera);

        // click button to select image from gallery
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(ComplainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(ComplainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
                    return;

                }
                // initialising intent
                Intent intent = new Intent();

                // setting type to select to be image
                intent.setType("image/*");

                // allowing multiple image to be selected
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PHOTO_FROM_GALLERY);
            }
        });

        btnSubmit.setOnClickListener(this::submitComplaint);

    private void cameraResult(ActivityResult result) {
        Intent intent = result.getData();
        List<String> imageList = null;
        if(intent != null) {
             imageList = intent.getStringArrayListExtra("imageList");
        }

        if (imageList != null) {
            for (String imageString: imageList) {

                Uri imageurl = Uri.parse(imageString);
                imagesUri.add(imageurl);
                try {
                    ComplaintImage complaintImage = new ComplaintImage();
                    InputStream inputStream = getContentResolver().openInputStream(imageurl);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    complaintImage.setBitmap(bitmap);
                    complaintImageList.add(complaintImage);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            viewPagerAdapter = new ViewPagerAdapter(this, imagesUri);
            // initializing the ViewPager Object adding the Adapter to the ViewPager
            viewPager.setAdapter(viewPagerAdapter);
        }
    }

    private void openCamera(View view) {
        cameraLauncher.launch(new Intent(this,CameraActivity.class));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // When an Image is picked
        if (requestCode == PHOTO_FROM_GALLERY && resultCode == RESULT_OK && null != data) {
            // Get the Image from data
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                int cout = data.getClipData().getItemCount();
                viewPager.setVisibility(View.VISIBLE);
                for (int i = 0; i < cout; i++) {

                    Uri imageurl = data.getClipData().getItemAt(i).getUri();
                    imagesUri.add(imageurl);
                    try {
                        ComplaintImage complaintImage = new ComplaintImage();
                        InputStream inputStream = getContentResolver().openInputStream(imageurl);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        complaintImage.setBitmap(bitmap);
                        complaintImageList.add(complaintImage);


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                viewPagerAdapter = new ViewPagerAdapter(this, imagesUri);
                // initializing the ViewPager Object adding the Adapter to the ViewPager
                viewPager.setAdapter(viewPagerAdapter);
            } else {
                viewPager.setVisibility(View.VISIBLE);
                Uri imageurl = data.getData();
                try {
                    ComplaintImage complaintImage = new ComplaintImage();
                    InputStream inputStream = getContentResolver().openInputStream(imageurl);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    complaintImage.setBitmap(bitmap);
                    complaintImageList.add(complaintImage);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imagesUri.add(imageurl);
                viewPagerAdapter = new ViewPagerAdapter(this, imagesUri);
                // initializing the ViewPager Object adding the Adapter to the ViewPager
                viewPager.setAdapter(viewPagerAdapter);
            }
        }
        else if(data == null && requestCode == PHOTO_FROM_GALLERY)
        {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "You haven't capture any image", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public Loader<Integer> onCreateLoader(int id, @Nullable Bundle args) {
        return new ComplainLoader(this, complaint);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Integer> loader, Integer data) {
        loaderManager.destroyLoader(loader.getId());
        int status = data;

        if (status == 1) {
            Toast.makeText(this, "Submit successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ForumActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Something error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Integer> loader) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            getCurrentLocation();
        }
    }

    //@SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        // initialize task location

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(location -> {
                // when success
                if (location!= null){
                    supportMapFragment.getMapAsync(googleMap -> {

                        latLng = new LatLng(location.getLatitude(), location.getLatitude());
                        // when click on map
                        // initialize marker options
                        MarkerOptions markerOptions = new MarkerOptions();
                        //set position of marker
                        markerOptions.position(latLng);
                        //set title of marker
                        markerOptions.title("I am here");
                        //markerOptions.title(latLng.latitude + " : "+ latLng.longitude);
                        // remove all marker
                        googleMap.clear();
                        // animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                        // add marker on map
                        googleMap.addMarker(markerOptions);

                        txtLocation.setText(latLng.latitude + " , "+ latLng.longitude);
                    });
                }
            });
        }


    }

    private void setGoogleMapMarker(@NonNull GoogleMap googleMap, LatLng latLng) {
        // when click on map
        // initialize marker options
        MarkerOptions markerOptions = new MarkerOptions();
        //set position of marker
        markerOptions.position(this.latLng);
        //set title of marker
        markerOptions.title(this.latLng.latitude + " : "+ this.latLng.longitude);
        // remove all marker
        googleMap.clear();
        // animating to zoom the marker
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(this.latLng,10));
        // add marker on map
        googleMap.addMarker(markerOptions);

        txtLocation.setText(this.latLng.latitude + ","+ this.latLng.longitude);
    }

    private void cameraResult(ActivityResult result) {}

    private void openCamera(View view) {}

    // this method will submit the user's complaint
    private void submitComplaint(View view)
    {
        String strLocation []= txtLocation.getText().toString().split(",") ;
        complaint.setComplaintLatitude(Long.parseLong(strLocation[0]));
        complaint.setComplaintLongitude(Long.parseLong(strLocation[1]));
        complaint.setImageList(complaintImageList);
        complaint.setComplaintTitle(txtTitle.getText().toString());
        complaint.setComplaintDescription(txtDescription.getText().toString());

        loaderManager.initLoader(0, null, this);

    }



}