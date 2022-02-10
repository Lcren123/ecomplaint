package com.utem.mobile.ecomplaint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.utem.mobile.ecomplaint.model.Complaint;
import com.utem.mobile.ecomplaint.model.ComplaintImage;
import com.utem.mobile.ecomplaint.model.ViewPagerAdapter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ComplainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Integer>{

    private Button btnSubmit, btnGallery, btnCamera;
    private EditText txtTitle, txtDescription;

    private List <Uri> imagesUri;
    LoaderManager loaderManager;

    private int PHOTO_FROM_GALLERY = 1;
    private int position = 0;
    private List <ComplaintImage> complaintImageList;
    private Complaint complaint;
    // creating object of ViewPager
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);

        txtTitle = findViewById(R.id.txtTitle);
        txtDescription = findViewById(R.id.txtDescription);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnGallery = findViewById(R.id.btnGallery);
        viewPager = findViewById(R.id.viewPager);

        imagesUri= new ArrayList<>();
        complaintImageList = new ArrayList<>();
        complaint = new Complaint();

        loaderManager =LoaderManager.getInstance(this);

        // click button to select image from gallery
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(ComplainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(ComplainActivity.this, new String []{Manifest.permission.READ_EXTERNAL_STORAGE},100);
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

        btnSubmit.setOnClickListener(this:: submit);


    }

    private void submit(View view) {

        complaint.setImageList(complaintImageList);
        complaint.setComplaintTitle(txtTitle.getText().toString());
        complaint.setComplaintDescription(txtDescription.getText().toString());

        loaderManager.initLoader(0, null, this);

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
        else
        {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_SHORT).show();
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

        if (status == 1){
            Toast.makeText(this, "Submit successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ForumActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Something error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Integer> loader) {

    }





}