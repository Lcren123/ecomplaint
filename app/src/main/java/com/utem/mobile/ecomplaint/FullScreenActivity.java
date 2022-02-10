package com.utem.mobile.ecomplaint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class FullScreenActivity extends AppCompatActivity {

    ImageView fullScreenImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        fullScreenImageView = (ImageView) findViewById(R.id.fullScreenImageView);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null)
        {
            Uri uri = Uri.parse(bundle.getString("imageUri"));
            fullScreenImageView.setImageURI(uri);

        }
    }
}