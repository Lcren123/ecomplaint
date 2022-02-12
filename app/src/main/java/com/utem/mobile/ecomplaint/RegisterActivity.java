package com.utem.mobile.ecomplaint;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.utem.mobile.ecomplaint.model.Resident;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Bundle> {

    private EditText txtName, txtPassword,txtConfirmPassword, IcNo, txtProfileName, phoneNo;
    private List<Uri> imagesUri;
    private LoaderManager loaderManager;
    private Resident resident;
    private ImageView IcFrontImage, IcBackImage;

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
        Resident resident = new Resident();
        IcFrontImage = findViewById(R.id.FrontIC);
        IcBackImage = findViewById(R.id.BackIC);
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::cameraResult);

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

    private void cameraResult(ActivityResult result) {
        Intent intent = result.getData();
        List<String> ICImage = null;
        if (intent != null)
        {
            ICImage = intent.getStringArrayListExtra("imageList");
        }

            for (String imageString : ICImage)
            {
                Uri imageurl = Uri.parse(imageString);
                imagesUri.add(imageurl);
                try {
                    if(IcFrontImage == null) {
                        IcFrontImage.setImageURI(imageurl);
                        //ComplaintImage complaintImage = new ComplaintImage();
                        InputStream inputStream = getContentResolver().openInputStream(imageurl);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        resident.setFrontImage(bitmap);
                    }
                    else if(IcBackImage == null)
                    {
                        IcBackImage.setImageURI(imageurl);
                        //ComplaintImage complaintImage = new ComplaintImage();
                        InputStream inputStream = getContentResolver().openInputStream(imageurl);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        resident.setBackImage(bitmap);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }


    private void openCamera(View view) {
        cameraLauncher.launch(new Intent(this, CameraActivity.class));
    }

    public void GetICPhoto(View view) {
        openCamera(view);
    }
}