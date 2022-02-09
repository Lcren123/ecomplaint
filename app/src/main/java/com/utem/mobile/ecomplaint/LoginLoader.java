package com.utem.mobile.ecomplaint;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class LoginLoader extends AsyncTaskLoader<Bundle> {

    private final String username, password;

    public LoginLoader(@NonNull Context context, String userName, String Password) {
        super(context);

        this.username = userName;
        this.password = Password;
    }

    @Nullable
    @Override
    public Bundle loadInBackground() {
        Bundle response = null;

try {

}
catch (Exception e){

}
        return null;
    }
}
