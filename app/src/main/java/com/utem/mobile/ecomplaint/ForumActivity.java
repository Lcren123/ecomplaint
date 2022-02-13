package com.utem.mobile.ecomplaint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.utem.mobile.ecomplaint.model.Complaint;

import java.util.ArrayList;
import java.util.List;

public class ForumActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Complaint>>, ItemClickListener {

    private ArrayList<Complaint> forumList = new ArrayList<>();
    private LoaderManager loaderManager;
    private ForumRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<Complaint> complaints;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        progressBar = findViewById(R.id.spinnerLoader);

        loaderManager = LoaderManager.getInstance(this);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null) {
            progressBar.setVisibility(View.VISIBLE);
            loaderManager.initLoader(1, null, this);
        }else{

        }
    }

    @NonNull
    @Override
    public Loader<List<Complaint>> onCreateLoader(int id, @Nullable Bundle args) {
        return new ForumLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Complaint>> loader, List<Complaint> data) {

        if(data != null){
            System.out.println("success");
            System.out.println(data.size());
            recyclerView = findViewById(R.id.forum);
            complaints = data;
            adapter = new ForumRecyclerViewAdapter(this, data,this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            System.out.println("finish");
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Complaint>> loader) {

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this,ForumDetailActivity.class);
        intent.putExtra("complaintID",complaints.get(position).getComplaintID());
        startActivity(intent);
    }
}