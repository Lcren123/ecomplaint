package com.utem.mobile.ecomplaint;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.material.textfield.TextInputLayout;
import com.utem.mobile.ecomplaint.model.Complaint;
import com.utem.mobile.ecomplaint.model.ComplaintCategory;
import com.utem.mobile.ecomplaint.model.ComplaintImage;
import com.utem.mobile.ecomplaint.model.Resident;
import com.utem.mobile.ecomplaint.model.ViewPagerAdapter;
import com.utem.mobile.ecomplaint.room.ComplaintImageRoom;
import com.utem.mobile.ecomplaint.room.ComplaintRoom;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class ComplainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Integer> {

    private TextInputLayout txtInputLytCategory;
    private AutoCompleteTextView txtCategory;
    private Button btnSubmit, btnGallery, btnCamera;
    private EditText txtTitle, txtDescription;
    private TextView txtLocation;

    // creating obj for photo
    private List<Uri> imagesUri;
    private LoaderManager loaderManager;

    private int PHOTO_FROM_GALLERY = 1;
    private int position = 0;
    private List<ComplaintImage> complaintImageList;
    private Complaint complaint;
    private Complaint localComplaint;

    // creating object of ViewPager for photo
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    // creating obj for google map
    private int LOCATION_REQUEST_CODE = 2;
    private FusedLocationProviderClient client;
    private SupportMapFragment supportMapFragment;
    private LatLng latLng;

    private ActivityResultLauncher<Intent> cameraLauncher;

    // obj for drop down menu
    String[] category = {"Construction Work Zones" ,"Drains, Gullies And Sewer" , "Pothole Or Other Surface Defects" , "Road Sign Or Marking" ,
            "Road Drainage Fault" , "Roadside Grass Cutting" , "Street Light Fault" , "Traffic Lights"};
    private ComplaintCategory complaintCategory;
    private ComplaintViewModel complaintViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);

        txtTitle = findViewById(R.id.txtTitle);
        txtDescription = findViewById(R.id.txtDescription);
        txtLocation = findViewById(R.id.txtLocation);

        // for drop down menu
        txtInputLytCategory = findViewById(R.id.txtInputLytCategory);
        txtCategory = findViewById(R.id.txtCategory);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnGallery = findViewById(R.id.btnGallery);
        viewPager = findViewById(R.id.viewPager);
        btnCamera = findViewById(R.id.btnGCamera);

        imagesUri = new ArrayList<>();
        complaintImageList = new ArrayList<>();
        complaint = new Complaint();
        complaintCategory= new ComplaintCategory();
        loaderManager = LoaderManager.getInstance(this);

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::cameraResult);
        complaintViewModel = new ViewModelProvider(this).get(ComplaintViewModel.class);

        createNotificationChannel();

        // set string list into drop down menu
        // set the first string in arr into txtView
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                ComplainActivity.this,R.layout.complaint_category_dropdown_item, category
        );
        txtCategory.setText(category[0]);
        txtCategory.setAdapter(arrayAdapter);

        txtCategory.setOnItemClickListener((parent, view, position, id) -> complaintCategory.setCategoryName( arrayAdapter.getItem(position)));

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
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                // add marker on map
                googleMap.addMarker(markerOptions);
                txtLocation.setText(latLng.latitude + "," + latLng.longitude);

            }));
        }


        btnCamera.setOnClickListener(this::openCamera);

        // click button to select image from gallery
        btnGallery.setOnClickListener(v -> {

            if (ActivityCompat.checkSelfPermission(ComplainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(ComplainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
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
        });

        btnSubmit.setOnClickListener(this::submitComplaint);

        Executors.newSingleThreadExecutor().execute(()-> {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null){
            List<Complaint> complaints = complaintViewModel.getLocalComplaints();
            System.out.println(complaints.size());
           for(Complaint complaint: complaints){
               localComplaint = complaint;
               sendLocalToDatabase();
           }
        }});
    }

    private void cameraResult(ActivityResult result) {
        Intent intent = result.getData();
        List<String> imageList = null;
        if (intent != null)
        {
            imageList = intent.getStringArrayListExtra("imageList");
        }

        if (imageList != null)
        {   viewPager.setVisibility(View.VISIBLE);
            for (String imageString : imageList)
            {
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
        cameraLauncher.launch(new Intent(this, CameraActivity.class));
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
        } else if (data == null && requestCode == PHOTO_FROM_GALLERY) {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_SHORT).show();
        } else {
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
        if(loader.getId() == 0) {
            int status = 0;
            if (data != null) {
                status = data;
            }
            if (status == 1) {
                Toast.makeText(this, "Submit successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ForumActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Something error", Toast.LENGTH_SHORT).show();
            }
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

    private void getCurrentLocation() {

        CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            client.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY,cancellationTokenSource.getToken())
                    .addOnSuccessListener(this,location ->  supportMapFragment.getMapAsync(googleMap -> {
                        latLng = new LatLng(location.getLatitude(),location.getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions();
                        //set position of marker
                        markerOptions.position(latLng);
                        //set title of marker
                        markerOptions.title("I am there");
                        // remove all marker
                        googleMap.clear();
                        // animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        // add marker on map
                        googleMap.addMarker(markerOptions);
                        txtLocation.setText(latLng.latitude + "," + latLng.longitude);
                    }));
        }
    }

    private void submitComplaint(View view) {

        txtTitle.setEnabled(false);
        txtDescription.setEnabled(false);
        txtCategory.setEnabled(false);
        String strLocation []= txtLocation.getText().toString().split(",") ;
        complaint.setComplaintLatitude(Double.parseDouble(String.format("%.5f",Double.parseDouble(strLocation[0]))));
        complaint.setComplaintLongitude(Double.parseDouble(String.format("%.5f",Double.parseDouble(strLocation[1]))));
        complaint.setImageList(complaintImageList);
        complaint.setComplaintTitle(txtTitle.getText().toString());
        complaint.setComplaintDescription(txtDescription.getText().toString());

        ComplaintCategory complaintCategory = new ComplaintCategory();
        complaintCategory.setCategoryName(txtCategory.getText().toString());
        complaint.setCategory(complaintCategory);
        Resident resident = new Resident();

        SharedPreferences sharedPreferences = getSharedPreferences("com.utem.mobile.ecomplaint",MODE_PRIVATE);
        String token = sharedPreferences.getString("Token",null);
        System.out.println(token);
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(token);
        String username = new String(bytes);
        resident.setUserName(username);

        complaint.setResident(resident);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null) {
            loaderManager.initLoader(0, null, this);
        }else{

            ComplaintRoom complaintRoom = new ComplaintRoom();
            complaintRoom.setComplaintTitle(complaint.getComplaintTitle());
            complaintRoom.setComplaintDescription(complaint.getComplaintDescription());
            complaintRoom.setComplaintLongitude(complaint.getComplaintLongitude());
            complaintRoom.setComplaintLatitude(complaint.getComplaintLatitude());
            complaintRoom.setComplaintStatus(complaint.getComplaintStatus());
            complaintRoom.setUsername(username);
            complaintRoom.setCategoryName(complaint.getCategory().getCategoryName());
            complaintRoom.setConnectedToDatabase(false);

            List<ComplaintImageRoom> images = null;
            if(complaintImageList != null)
                images = new ArrayList<>();

            for(ComplaintImage image : complaintImageList){
                ComplaintImageRoom roomImage = new ComplaintImageRoom();

                Bitmap bitmap = image.getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                Base64.Encoder encoder = Base64.getEncoder();
                String encodeImage = encoder.encodeToString(byteArray);
                roomImage.setImage(encodeImage);
                images.add(roomImage);
            }

            complaintViewModel.addComplaint(complaintRoom,images);
            Toast.makeText(this, "Added into room database", Toast.LENGTH_SHORT).show();
        }
    }

    private void createNotificationChannel() {
        CharSequence name = "channel";
        String description = "channel";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("1",name,importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public void sendLocalToDatabase(){
        try {
            JSONObject request = new JSONObject();

            List<ComplaintImage> images = localComplaint.getImageList();
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
            request.put("imageList",imagesJSON);
            request.put("ComplaintTitle",localComplaint.getComplaintTitle());
            request.put("ComplaintDescription",localComplaint.getComplaintDescription());
            request.put("ComplaintLongitude",localComplaint.getComplaintLongitude());
            request.put("ComplaintLatitude",localComplaint.getComplaintLatitude());
            request.put("ComplaintStatus",localComplaint.getComplaintStatus());
            //TODO::
            //request.put("Username",complaint.getResident().getUserName());
            request.put("Username","resident");

            request.put("CategoryName",localComplaint.getCategory().getCategoryName());
            //request.put("CategoryID",complaint.getCategory().getComplaintCategoryID());

            HttpsURLConnection connection = (HttpsURLConnection)
                    new URL(this.getString(R.string.api_connect) + "/addComplaint.jsp").openConnection();

            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.getOutputStream().write(request.toString().getBytes());


            System.out.println(connection.getResponseCode());
            if (connection.getResponseCode() == 200) {
                System.out.println("200");
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                        .setContentTitle("Local complaint updated")
                        .setContentText("Your local complaint " + localComplaint.getComplaintTitle() + " has been uploaded")
                        .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat manager = NotificationManagerCompat.from(this);
                manager.notify(1,builder.build());
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}