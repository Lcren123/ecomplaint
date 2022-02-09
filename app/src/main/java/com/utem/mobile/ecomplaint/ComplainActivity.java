package com.utem.mobile.ecomplaint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;

public class ComplainActivity extends AppCompatActivity {

    Button nextBtn;
    ImageView addImageBtn;
    ImageSwitcher imageView;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    TextView txtTotal;
    ArrayList<Uri> mArrayUri;
    int position = 0;
    List<String> imagesEncodedList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);

        nextBtn=findViewById(R.id.nextBtn);
        txtTotal = findViewById(R.id.txtTotal);
        imageView = findViewById(R.id.image);
        addImageBtn = findViewById(R.id.addImageBtn);
        mArrayUri = new ArrayList<Uri>();

        // showing all images in imageswitcher
        imageView.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView1 = new ImageView(getApplicationContext());
                imageView1.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView1.setLayoutParams(new ImageSwitcher.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
                return imageView1;
            }
        });

        // click here to select image
        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // initialising intent
                Intent intent = new Intent();

                // setting type to select to be image
                intent.setType("image/*");

                // allowing multiple image to be selected
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });

        // click here to select next image
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < mArrayUri.size() - 1) {
                    // increase the position by 1
                    position++;
                    imageView.setImageURI(mArrayUri.get(position));
                }else if (position==0)
                    Toast.makeText(ComplainActivity.this, "No Image", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(ComplainActivity.this, "Last Image", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // When an Image is picked
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            // Get the Image from data
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                int cout = data.getClipData().getItemCount();
                for (int i = 0; i < cout; i++) {
                    // adding imageuri in array
                    Uri imageurl = data.getClipData().getItemAt(i).getUri();
                    mArrayUri.add(imageurl);

                }
                System.out.print(cout);
                // setting 1st selected image into image switcher
                imageView.setImageURI(mArrayUri.get(0));
                position = 0;
            } else {

                Uri imageurl = data.getData();
                mArrayUri.add(imageurl);
                imageView.setImageURI(mArrayUri.get(0));
                position = 0;
                //Toast.makeText(this, mArrayUri.size(), Toast.LENGTH_LONG).show();

            }
        } else {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }
}