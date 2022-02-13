package com.utem.mobile.ecomplaint;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

    public void Logout(View view) {
        AlertDialog builder = new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Do you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes",this::confirmLogout)
                .setNegativeButton("No",this::cancelLogout)
                .show();

    }

    private void cancelLogout(DialogInterface dialogInterface, int i) {
    }

    private void confirmLogout(DialogInterface dialogInterface, int i) {
        SharedPreferences sharedPreferences = getSharedPreferences("com.utem.mobile.ecomplaint",MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}