package com.utem.mobile.ecomplaint;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.utem.mobile.ecomplaint.model.Complaint;

public class ComplainLoader extends AsyncTaskLoader<Integer> {

    public ComplainLoader(@NonNull Context context, Complaint complaint)
    {
        super(context);
    }

    @Nullable
    @Override
    public Integer loadInBackground() {
        return null;
    }


}
