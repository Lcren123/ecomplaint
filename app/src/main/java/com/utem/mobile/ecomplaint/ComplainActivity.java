package com.utem.mobile.ecomplaint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.utem.mobile.ecomplaint.model.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ComplainActivity extends AppCompatActivity {

    private Button btnSubmit, btnGallery, btnCamera;
    private EditText txtTitle, txtDescription;

    private List <Uri> imagesUri;

    /*private List <Bitmap> bitmaps;
    private ImageSwitcher imageView;*/
    private int PHOTO_FROM_GALLERY = 1;
    private int position = 0;


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

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


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
                    /*try {
                        InputStream inputStream = getContentResolver().openInputStream(imageurl);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        bitmaps.add(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }*/
                }
                viewPagerAdapter = new ViewPagerAdapter(this, imagesUri);
                // initializing the ViewPager Object adding the Adapter to the ViewPager
                viewPager.setAdapter(viewPagerAdapter);
            } else {

                Uri imageurl = data.getData();
                /*try {
                    InputStream inputStream = getContentResolver().openInputStream(imageurl);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    bitmaps.add(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }*/
                imagesUri.add(imageurl);
                viewPagerAdapter = new ViewPagerAdapter(this, imagesUri);
                // initializing the ViewPager Object adding the Adapter to the ViewPager
                viewPager.setAdapter(viewPagerAdapter);
            }
        }
        else
        {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }
}