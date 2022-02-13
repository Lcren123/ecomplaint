package com.utem.mobile.ecomplaint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.utem.mobile.ecomplaint.model.Complaint;

public class LoginActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Bundle> {
    private EditText txtName, txtPassword;
    private LoaderManager loaderManager;

    @NonNull
    @Override
    public Loader<Bundle> onCreateLoader(int id, @Nullable Bundle args) {
        return new LoginLoader(this, txtName.getText().toString(), txtPassword.getText().toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Bundle> loader, Bundle data) {
        loaderManager.destroyLoader(loader.getId());

        if (data != null) {
            Intent intent = null;

            String token = data.getString("Token");

            if (token != null) {
                Toast.makeText(LoginActivity.this, "login Success",Toast.LENGTH_SHORT).show();

                SharedPreferences sharedPreferences = getSharedPreferences("com.utem.mobile.ecomplaint",MODE_PRIVATE);
                sharedPreferences.edit()
                        .putString("Token", token)
                        .apply();

                intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("Token", token);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Username/password is incorrect",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Bundle> loader) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loaderManager = LoaderManager.getInstance(this);
        txtName = findViewById(R.id.editName);
        txtPassword = findViewById(R.id.editPassword);
        txtName.setHintTextColor(Color.BLACK);
        txtPassword.setHintTextColor(Color.BLACK);
    }

    public void login(View view) {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            txtName.setEnabled(false);
            txtPassword.setEnabled(false);
            loaderManager.initLoader(1, null, this);
        }else{
            Toast.makeText(this, "INTERNET NOT AVAILABLE", Toast.LENGTH_SHORT).show();
        }
    }

    public void Register(View view) {
        Intent intent = null;
        intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }


}