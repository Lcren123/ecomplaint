package com.utem.mobile.ecomplaint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.graphics.Color;
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
            //System.out.println(data);
            String token = data.getString("Token", null);

            if (token != null) {
                Toast.makeText(LoginActivity.this, "login Success",
                        Toast.LENGTH_SHORT).show();
                intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("Token", token);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Username/password is incorrect",
                        Toast.LENGTH_SHORT).show();
                System.out.println("Username/password is incorrect");
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
        txtName.setEnabled(false);
        txtPassword.setEnabled(false);
        loaderManager.initLoader(1, null, this);

    }

    public void Register(View view) {
        Intent intent = null;
        intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }


    public void complain(View view) {
        Intent intent = null;
        intent = new Intent(LoginActivity.this, ComplainActivity.class);
        startActivity(intent);
    }
}