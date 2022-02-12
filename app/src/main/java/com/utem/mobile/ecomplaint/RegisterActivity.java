package com.utem.mobile.ecomplaint;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.utem.mobile.ecomplaint.model.Resident;

public class RegisterActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Bundle> {

    private static final int CAMERA_PERM_CODE = 1 ;
    private static final int IC_BACK_CODE = 101;
    private static final int IC_FRONT_CODE = 102;
    private EditText txtName, txtPassword,txtConfirmPassword, IcNo, txtProfileName, phoneNo;

    private LoaderManager loaderManager;
    private Resident resident;
    private ImageView IcFrontImage, IcBackImage;
    private Button btnICBack, btnICFront;

    private ActivityResultLauncher<Intent> cameraLauncher;

    @NonNull
    @Override
    public Loader<Bundle> onCreateLoader(int id, @Nullable Bundle args) {
        return new RegisterLoader(this, resident);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Bundle> loader, Bundle data) {

        if (data != null) {
            Intent intent;
            //System.out.println(data);
            String token = data.getString("Token", null);

            if (token != null) {
                Toast.makeText(RegisterActivity.this, "Register Success",
                        Toast.LENGTH_SHORT).show();
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
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
        resident = new Resident();
        IcFrontImage = findViewById(R.id.FrontIC);
        IcBackImage = findViewById(R.id.BackIC);
        //cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::cameraResult);

        btnICBack = findViewById(R.id.btnICBack);
        btnICFront= findViewById(R.id.btnICFront);

        // capture back ic photo
        btnICFront.setOnClickListener(v->askCameraPermission(IC_FRONT_CODE));
        btnICBack.setOnClickListener(v->askCameraPermission(IC_BACK_CODE));

        IcFrontImage.setOnClickListener(v->removePhoto(IC_FRONT_CODE));
        IcBackImage.setOnClickListener(v->removePhoto(IC_BACK_CODE));
    }

    private void removePhoto(int requestCode) {
        if (requestCode==IC_FRONT_CODE && resident.getFrontImage()!=null){
            int imageResource = getResources().getIdentifier("@drawable/ic_addphoto", null, getPackageName());
            Drawable drawable = getResources().getDrawable(imageResource,getTheme());
            IcFrontImage.setImageDrawable(drawable);
        }
        else if (requestCode==IC_BACK_CODE && resident.getBackImage()!=null)
            IcBackImage.setImageDrawable(null);
    }

    private void askCameraPermission(int requestCode) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},CAMERA_PERM_CODE);
        }else if(requestCode==IC_FRONT_CODE){
            // if permission is given
            takeICFrontPhoto();
        }else {
            takeICBackPhoto();
        }

    }

    private void takeICFrontPhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, IC_FRONT_CODE);
    }

    private void takeICBackPhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, IC_BACK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode== CAMERA_PERM_CODE){
            takeICBackPhoto();
            takeICFrontPhoto();
        }else{
            // if permission is refused
            Toast.makeText(this,"Camera Permission is required to capture IC.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // insert ic photo to resident obj & imageview
        if (requestCode== IC_FRONT_CODE){
            Bitmap image = (Bitmap) data.getExtras().get("data");
            IcFrontImage.setImageBitmap(image);
            resident.setFrontImage(image);

        }
        else if (requestCode== IC_BACK_CODE){
            Bitmap image = (Bitmap) data.getExtras().get("data");
            IcBackImage.setImageBitmap(image);
            resident.setBackImage(image);
        }
    }

    public void Register(View view) {
        txtName.setEnabled(false);
        resident.setUserName(txtName.getText().toString());
        txtProfileName.setEnabled(false);
        resident.setUserProfileName(txtProfileName.getText().toString());
        IcNo.setEnabled(false);
        resident.setUserIC(IcNo.getText().toString());
        phoneNo.setEnabled(false);
        resident.setUserPhone(phoneNo.getText().toString());
        txtPassword.setEnabled(false);
        resident.setPassword(txtPassword.getText().toString());
        txtConfirmPassword.setEnabled(false);


        RegisterCheck(resident, txtConfirmPassword.getText().toString());

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

    public void RegisterCheck(Resident resident, String confirmPassword){
        int n, check = 0;
        //nameCheck

                    if (resident.getUserName() == null) {
                        Toast.makeText(RegisterActivity.this, "Your name cannot be empty!!",
                                Toast.LENGTH_SHORT).show();
                        check = 1;
                        retry();
                    }

                    else if (resident.getUserProfileName() == null) {
                        Toast.makeText(RegisterActivity.this, "Your Profile name cannot be empty!!",
                                Toast.LENGTH_SHORT).show();
                        check = 1;
                        retry();

                    }

                    else if  (resident.getUserIC().length() != 12) {
                        Toast.makeText(RegisterActivity.this, "Please ensure your Ic No is correct",
                                Toast.LENGTH_SHORT).show();
                        check = 1;
                        retry();

                    }

                    else if  (resident.getUserPhone() == null) {
                        Toast.makeText(RegisterActivity.this, "Your phone number cannot be empty!!",
                                Toast.LENGTH_SHORT).show();
                        check = 1;
                        retry();

                    }

                    else if  (resident.getPassword() == null) {
                        Toast.makeText(RegisterActivity.this, "Your password cannot be empty!!",
                                Toast.LENGTH_SHORT).show();
                        check = 1;
                        retry();

                    }

                    else if  (resident.getPassword().equals(confirmPassword)) {
                        Toast.makeText(RegisterActivity.this, "Please make sure you type in the same password",
                                Toast.LENGTH_SHORT).show();
                        check = 1;
                        retry();

                    }

                    else if  (resident.getFrontImage() == null || resident.getBackImage() == null) {
                        Toast.makeText(RegisterActivity.this, "Please take picture for both side of IC",
                                Toast.LENGTH_SHORT).show();
                        check = 1;
                        retry();
                    }
                    else
                        loaderManager.initLoader(1, null, this);


       /* if(check == 0){
            Toast.makeText(RegisterActivity.this, "Something Went Wrong, please try again",
                    Toast.LENGTH_SHORT).show();
            retry();
        }
        else if(check == 1)
            retry();*/

    }


}