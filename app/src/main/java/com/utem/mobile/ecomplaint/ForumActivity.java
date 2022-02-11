package com.utem.mobile.ecomplaint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class ForumActivity extends AppCompatActivity {

    ArrayList<String> ForumList = new ArrayList<>();
    ForumRecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        RecyclerView recyclerView = findViewById(R.id.forum);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ForumRecyclerViewAdapter(this, ForumList);
        adapter.setClickListener((ForumRecyclerViewAdapter.ItemClickListener) this);
        recyclerView.setAdapter(adapter);
    }


    public void onItemClick(View view, int position) {
        String num = adapter.getItem(position);
        //Intent intent=new Intent(ForumActivity.this,ForumDetailActivity.class);
        //startActivity(intent);

    }
}