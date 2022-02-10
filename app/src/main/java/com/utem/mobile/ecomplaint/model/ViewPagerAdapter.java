package com.utem.mobile.ecomplaint.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.utem.mobile.ecomplaint.FullScreenActivity;
import com.utem.mobile.ecomplaint.R;

import java.util.List;
import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {

    // Context object
    Context context;
    // Array of images
    List <Uri> imagesUri ;

    LayoutInflater layoutInflater;

    // Viewpager Constructor
    public ViewPagerAdapter(Context context,  List <Uri> imagesUri) {
        this.context = context;
        this.imagesUri = imagesUri;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // return the number of images
        return imagesUri.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        View viewpager = layoutInflater.inflate(R.layout.viewpager, container, false);

        // referencing the image view from the item.xml file
        ImageView imageView = (ImageView) viewpager.findViewById(R.id.imageViewPhoto);

        // setting the image in the imageView
        imageView.setImageURI(imagesUri.get(position));

        // adding the View
        Objects.requireNonNull(container).addView(viewpager);

        // set imageview onclick listener to display full screen
        imageView.setOnClickListener( view -> displayFullScreen (imagesUri.get(position)) );

        return viewpager;
    }

    // this method will pass the bitmap to FullScreenActivity to display full screen
    private void displayFullScreen(Uri imageUri) {
        Intent intent  = new Intent(context, FullScreenActivity.class);
        intent.putExtra("imageUri" ,imageUri.toString());
        context.startActivity(intent);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((LinearLayout) object);
    }
}
