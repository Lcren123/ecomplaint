package com.utem.mobile.ecomplaint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Bundle> {

    private EditText txtName, txtPassword,txtConfirmPassword, IcNo, txtProfileName, phoneNo;
    private LoaderManager loaderManager;

    @NonNull
    @Override
    public Loader<Bundle> onCreateLoader(int id, @Nullable Bundle args) {
        return new RegisterLoader(this, txtName.getText().toString(),txtProfileName.getText().toString(),
                IcNo.getText().toString(),phoneNo.getText().toString(),txtPassword.getText().toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Bundle> loader, Bundle data) {

        if (data != null) {
            Intent intent = null;
            //System.out.println(data);
            String token = data.getString("Token", null);

            if (token != null) {
                Toast.makeText(RegisterActivity.this, "Register Success",
                        Toast.LENGTH_SHORT).show();
                intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.putExtra("Token", token);
                startActivity(intent);
            } else {
                Toast.makeText(RegisterActivity.this, "Something Went Wrong, please try again",
                        Toast.LENGTH_SHORT).show();
                retry();
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Bundle> loader) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loaderManager = LoaderManager.getInstance(this);
        txtName = findViewById(R.id.editnameOnIc);
        txtProfileName = findViewById(R.id.editProfileName);
        IcNo = findViewById(R.id.editIC);
        phoneNo = findViewById(R.id.editPhoneNo);
        txtPassword = findViewById(R.id.editPassword);
        txtConfirmPassword = findViewById(R.id.editConfirmPassword);


    }

    public void Register(View view) {
        txtName.setEnabled(false);
        txtProfileName.setEnabled(false);
        IcNo.setEnabled(false);
        phoneNo.setEnabled(false);
        txtPassword.setEnabled(false);
        txtConfirmPassword.setEnabled(false);
        RegisterCheck(txtName.getText().toString(),txtProfileName.getText().toString(),
                IcNo.getText().toString(),phoneNo.getText().toString(),txtPassword.getText().toString(),
                txtConfirmPassword.getText().toString());

    }

    public void retry(){
        txtName.setText("");
        txtName.setEnabled(true);
        txtProfileName.setText("");
        txtProfileName.setEnabled(true);
        IcNo.setText("");
        IcNo.setEnabled(true);
        phoneNo.setText("");
        phoneNo.setEnabled(true);
        txtPassword.setText("");
        txtPassword.setEnabled(true);
        txtConfirmPassword.setText("");
        txtConfirmPassword.setEnabled(true);
    }

    public void RegisterCheck(String name, String profileName, String IcNo, String phoneNo, String password
    ,String confirmPassword){
        int n, check;
        //nameCheck
        for(n=0; n <7; n++) {
            switch(n)
            {
                case 1: //namecheck
                    if (name == null) {
                        Toast.makeText(RegisterActivity.this, "Your name cannot be empty!!",
                                Toast.LENGTH_SHORT).show();
                        check = 1;
                        n = 6;
                        break;
                    }
                case 2://profilenamecheck
                    if (profileName == null) {
                        Toast.makeText(RegisterActivity.this, "Your Profile name cannot be empty!!",
                                Toast.LENGTH_SHORT).show();
                        check = 1;
                        n = 6;
                        break;
                    }
                case 3: //IcNocheck
                    if (IcNo.length() != 12) {
                        Toast.makeText(RegisterActivity.this, "Please ensure your Ic No is correct",
                                Toast.LENGTH_SHORT).show();
                        check = 1;
                        n = 6;
                        break;
                    }
                case 4: //phoneNoCheck
                    if (name == null) {
                        Toast.makeText(RegisterActivity.this, "Your name cannot be empty!!",
                                Toast.LENGTH_SHORT).show();
                        check = 1;
                        n = 6;
                        break;
                    }
                case 5: //passwordcheck
                    if (name == null) {
                        Toast.makeText(RegisterActivity.this, "Your name cannot be empty!!",
                                Toast.LENGTH_SHORT).show();
                        check = 1;
                        n = 6;
                        break;
                    }
                case 6:
                    if(n == 0){
                        Toast.makeText(RegisterActivity.this, "Something Went Wrong, please try again",
                                Toast.LENGTH_SHORT).show();
                        retry();
                    }
                    else if(n == 1)
                        retry();
                    else if(n == 2)
                        loaderManager.initLoader(1, null, this);
            }


        }
    }

}