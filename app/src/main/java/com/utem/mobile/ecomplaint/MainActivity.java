package com.utem.mobile.ecomplaint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void GoToComplaint(View view) {
        Intent intent = new Intent();

        intent = new Intent(MainActivity.this, ComplainActivity.class);
        startActivity(intent);
    }

    public void GoToForum(View view) {
        Intent intent = new Intent();

        intent = new Intent(MainActivity.this, ForumActivity.class);
        startActivity(intent);
    }
}