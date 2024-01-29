package com.burhanrashid52.photoediting.activitys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.burhanrashid52.photoediting.Adapters.DownloadedSamplesAdapter;
import com.burhanrashid52.photoediting.Adapters.SamplesAdapter;
import com.burhanrashid52.photoediting.CustomFrameLayout;
import com.burhanrashid52.photoediting.FileSaveHelper;
import com.burhanrashid52.photoediting.R;
import com.burhanrashid52.photoediting.ZoomOutPageTransformer;
import com.burhanrashid52.photoediting.database.LocalModel;
import com.burhanrashid52.photoediting.models.ResponseModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ja.burhanrashid52.photoeditor.SaveSettings;

public class DownloadedPreview extends AppCompatActivity {
    LocalModel localModel;
    ViewPager2 preView0,preView1;
    CustomFrameLayout card0;
    List<String> itemURIs=new ArrayList<>();
    String sampleURIs;
    String [] samples;
    DownloadedSamplesAdapter samplesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloaded_preview);

        Intent intent=getIntent();
        localModel=(LocalModel) intent.getSerializableExtra("model");
        findView();


        sampleURIs=localModel.getSamples();
        samples= sampleURIs.split(",");



        StringBuilder builder;
        for (int i = 0; i < samples.length ; i++) {
            builder=new StringBuilder(samples[i]);
            builder.deleteCharAt(0);
            samples[i]=builder.toString();

        }
        itemURIs= Arrays.asList(samples);

        samplesAdapter=new DownloadedSamplesAdapter(itemURIs);

        if(localModel.getType().equals("0")){
            card0.setVisibility(View.VISIBLE);
            preView1.setVisibility(View.GONE);
            preView0.setAdapter(samplesAdapter);
            preView0.setPageTransformer(new ZoomOutPageTransformer());

        }else{
            preView1.setVisibility(View.VISIBLE);
            card0.setVisibility(View.GONE);
            preView1.setAdapter(samplesAdapter);
            preView1.setPageTransformer(new ZoomOutPageTransformer());

        }
        samplesAdapter.setOnEditClicked(new DownloadedSamplesAdapter.OnEditClicked() {
            @Override
            public void edit(String uri) {
                Intent intent=new Intent(DownloadedPreview.this,EditImageActivity.class);
                intent.putExtra("path",uri);
                startActivity(intent);
            }


        });







    }
    public void findView(){

        preView0=findViewById(R.id.vipPreview0);
        preView1=findViewById(R.id.vipPreview1);
        card0=findViewById(R.id.dCard0);



    }



}