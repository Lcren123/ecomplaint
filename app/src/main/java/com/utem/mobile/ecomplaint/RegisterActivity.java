package com.utem.mobile.ecomplaint;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.utem.mobile.ecomplaint.model.ComplaintImage;
import com.utem.mobile.ecomplaint.model.Resident;
import com.utem.mobile.ecomplaint.model.ViewPagerAdapter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Bundle> {

    private EditText txtName, txtPassword,txtConfirmPassword, IcNo, txtProfileName, phoneNo;
    private List<Uri> imagesUri;
    private LoaderManager loaderManager;
    private Resident resident;

    // creating object of ViewPager for photo
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    private ActivityResultLauncher<Intent> cameraLauncher;


    @NonNull
    @Override
    public Loader<Bundle> onCreateLoader(int id, @Nullable Bundle args) {
        return new RegisterLoader(this, resident);
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
        Resident resident = new Resident();
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::cameraResult);

    }

    public void Register(View view) {
        txtName.setEnabled(false);
        txtProfileName.setEnabled(false);
        IcNo.setEnabled(false);
        phoneNo.setEnabled(false);
        txtPassword.setEnabled(false);
        txtConfirmPassword.setEnabled(false);
        resident.setUserName(txtName.getText().toString());

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
        int n, check = 0;
        //nameCheck
        for(n=0; n <8; n++) {
            switch(n)
            {
                case 1: //namecheck
                    if (name == null) {
                        Toast.makeText(RegisterActivity.this, "Your name cannot be empty!!",
                                Toast.LENGTH_SHORT).show();
                        check = 1;
                        n = 7;
                        break;
                    }
                case 2://profilenamecheck
                    if (profileName == null) {
                        Toast.makeText(RegisterActivity.this, "Your Profile name cannot be empty!!",
                                Toast.LENGTH_SHORT).show();
                        check = 1;
                        n = 7;
                        break;
                    }
                case 3: //IcNocheck
                    if (IcNo.length() != 12) {
                        Toast.makeText(RegisterActivity.this, "Please ensure your Ic No is correct",
                                Toast.LENGTH_SHORT).show();
                        check = 1;
                        n = 7;
                        break;
                    }
                case 4: //phoneNoCheck
                    if (phoneNo == null) {
                        Toast.makeText(RegisterActivity.this, "Your phone number cannot be empty!!",
                                Toast.LENGTH_SHORT).show();
                        check = 1;
                        n = 7;
                        break;
                    }
                case 5: //passwordcheck
                    if (password == null) {
                        Toast.makeText(RegisterActivity.this, "Your password cannot be empty!!",
                                Toast.LENGTH_SHORT).show();
                        check = 1;
                        n = 7;
                        break;
                    }
                case 6: //compare password and current password
                    if (password == confirmPassword) {
                        Toast.makeText(RegisterActivity.this, "Please make sure you type in the same password",
                                Toast.LENGTH_SHORT).show();
                        check = 1;
                        n = 7;
                        break;
                    }
                case 7:
                    if(n == 0){
                        Toast.makeText(RegisterActivity.this, "Something Went Wrong, please try again",
                                Toast.LENGTH_SHORT).show();
                        retry();
                    }
                    else if(check == 1)
                        retry();
                    else if(check == 2)
                        loaderManager.initLoader(1, null, this);
                    break;
            }


        }
    }


    private void cameraResult(ActivityResult result) {
        Intent intent = result.getData();
        List<String> imageList = null;
        if (intent != null)
        {
            imageList = intent.getStringArrayListExtra("imageList");
        }

        if (imageList != null)
        {   viewPager.setVisibility(View.VISIBLE);
            for (String imageString : imageList)
            {
                Uri imageurl = Uri.parse(imageString);
                imagesUri.add(imageurl);
                try {
                    ComplaintImage complaintImage = new ComplaintImage();
                    InputStream inputStream = getContentResolver().openInputStream(imageurl);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    resident.setFrontImage(bitmap);



                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            viewPagerAdapter = new ViewPagerAdapter(this, imagesUri);
            // initializing the ViewPager Object adding the Adapter to the ViewPager
            viewPager.setAdapter(viewPagerAdapter);
        }
    }

    private void openCamera(View view) {
        cameraLauncher.launch(new Intent(this, CameraActivity.class));
    }
}