package com.utem.mobile.ecomplaint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.utem.mobile.ecomplaint.model.Complaint;

import java.util.ArrayList;
import java.util.List;

public class ForumActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Complaint>> {

    private ArrayList<Complaint> forumList = new ArrayList<>();
    private LoaderManager loaderManager;
    private ForumRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);


        loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(1,null,this);

       // adapter.setClickListener((ForumRecyclerViewAdapter.ItemClickListener) this);

    }


    public void onItemClick(View view, int position) {
        Complaint num = adapter.getItem(position);
        //Intent intent=new Intent(ForumActivity.this,ForumDetailActivity.class);
        //startActivity(intent);

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
            recyclerView = findViewById(R.id.forum);
            adapter = new ForumRecyclerViewAdapter(this, data);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Complaint>> loader) {

    }
}