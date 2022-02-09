package com.utem.mobile.ecomplaint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ComplainActivity extends AppCompatActivity {

    Button nextBtn, btnGallery;
    ImageView addImageBtn;
    ImageSwitcher imageView;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    TextView txtTitle, txtDescription;
    List <Bitmap> bitmaps;

    int position = 0;
    List<String> imagesEncodedList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);

        nextBtn=findViewById(R.id.nextBtn);

        imageView = findViewById(R.id.image);
        btnGallery = findViewById(R.id.btnGallery);


        bitmaps=new ArrayList<>();


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
        btnGallery.setOnClickListener(new View.OnClickListener() {
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
                if (position < bitmaps.size() - 1) {
                    // increase the position by 1
                    position++;
                    Drawable drawable =new BitmapDrawable(bitmaps.get(position));
                    imageView.setImageDrawable(drawable);

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

                    Uri imageurl = data.getClipData().getItemAt(i).getUri();

                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageurl);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        bitmaps.add(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            } else {

                Uri imageurl = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageurl);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    bitmaps.add(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (final Bitmap bitmap : bitmaps){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Drawable drawable =new BitmapDrawable(bitmap);
                                imageView.setImageDrawable(drawable);
                            }
                        });
                        try {
                            Thread.sleep(3000);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }

                    }

                }
            }).start();

        } else {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }
}