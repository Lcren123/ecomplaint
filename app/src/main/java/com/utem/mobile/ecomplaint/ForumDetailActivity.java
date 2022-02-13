package com.utem.mobile.ecomplaint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.utem.mobile.ecomplaint.model.Complaint;
import com.utem.mobile.ecomplaint.model.ViewPagerAdapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ForumDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Complaint> {

    private ImageView image;
    private TextView complaintCategoryTextView;
    private TextView complaintTitleTextView;
    private TextView complaintTimeTextView;
    private LoaderManager loaderManager;
    private TextView complaintDescriptionTextView;
    private TextView complaintStatusTextView;
    private TextView complaintLocationTesxtView;

    private int complaintID;
    private Complaint complaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_detail);

        complaintCategoryTextView = findViewById(R.id.CategoryTextView);
        complaintTitleTextView = findViewById(R.id.ComplaintTitleTextView);
        complaintTimeTextView = findViewById(R.id.ComplaintTimeTextView);
        complaintDescriptionTextView = findViewById(R.id.detail);
        complaintStatusTextView = findViewById(R.id.complaintStatus);
        complaintLocationTesxtView = findViewById(R.id.ComplaintLocationTextView);
        image = findViewById(R.id.complaintImage);

        Intent intent = getIntent();
        complaintID = intent.getIntExtra("complaintID", 0);

        loaderManager = LoaderManager.getInstance(this);
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null) {
            loaderManager.initLoader(1, null, this);
        }else{

        }

    }

    @NonNull
    @Override
    public Loader<Complaint> onCreateLoader(int id, @Nullable Bundle args) {
        return new ForumDetailLoader(this,complaintID);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Complaint> loader, Complaint data) {
        if(data != null){
            complaint = data;
            setComplaintData();
        }else{
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Complaint> loader) {

    }

    private void setComplaintData(){
        complaintTimeTextView.setText(complaint.getComplaintDateTime());
        complaintTitleTextView.setText(complaint.getComplaintTitle());
        complaintCategoryTextView.setText(complaint.getCategory().getCategoryName());
        complaintDescriptionTextView.setText(complaint.getComplaintDescription());
        complaintStatusTextView.setText(complaint.getComplaintStatus());
        String location = Double.toString(complaint.getComplaintLongitude()) + "," + Double.toString(complaint.getComplaintLatitude());
        complaintLocationTesxtView.setText(location);
        image.setImageBitmap(complaint.getImageList().get(0).getBitmap());
    }
}