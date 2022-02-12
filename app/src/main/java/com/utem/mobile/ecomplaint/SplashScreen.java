package com.utem.mobile.ecomplaint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("com.utem.mobile.ecomplaint",MODE_PRIVATE);

        Intent intent = null;
        if(sharedPreferences.contains("token"))
            intent = new Intent(this,MainActivity.class);
        else
            intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
