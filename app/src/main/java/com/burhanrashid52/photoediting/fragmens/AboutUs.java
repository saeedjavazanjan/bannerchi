package com.burhanrashid52.photoediting.fragmens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.burhanrashid52.photoediting.R;

public class AboutUs extends Fragment {
    ImageView ratingImage;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.about_us,container,false);
        ratingImage=view.findViewById(R.id.imgRate);
        ratingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setData(Uri.parse("bazaar://details?id=" + "com.saeed.zanjan.bestDesign"));
                intent.setPackage("com.farsitel.bazaar");
                startActivity(intent);
            }
        });
        return view;


    }
}
