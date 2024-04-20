package com.burhanrashid52.photoediting.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.burhanrashid52.photoediting.Adapters.DownloadedSamplesAdapter;
import com.burhanrashid52.photoediting.CustomFrameLayout;
import com.burhanrashid52.photoediting.ImageShareHelper;
import com.burhanrashid52.photoediting.R;
import com.burhanrashid52.photoediting.ZoomOutPageTransformer;
import com.burhanrashid52.photoediting.database.LocalModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DownloadedPreview extends AppCompatActivity {
    LocalModel localModel;
    ViewPager2 preView0,preView1;
    CustomFrameLayout card0;
    FrameLayout card1;
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

        if((samples[0].charAt(samples[0].length()-6))=='p'){
           changeFirstOne(samples);
        }

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
            card1.setVisibility(View.GONE);
            preView1.setVisibility(View.GONE);
            preView0.setAdapter(samplesAdapter);
            preView0.setPageTransformer(new ZoomOutPageTransformer());

        }else{
            preView1.setVisibility(View.VISIBLE);
            card0.setVisibility(View.GONE);
            card1.setVisibility(View.VISIBLE);
            preView1.setAdapter(samplesAdapter);
            preView1.setPageTransformer(new ZoomOutPageTransformer());
        }
        samplesAdapter.setOnEditClicked(new DownloadedSamplesAdapter.OnEditClicked() {
            @Override
            public void edit(String uri) {
                Intent intent=new Intent(DownloadedPreview.this,EditImageActivity.class);
                intent.putExtra("path",uri);
                intent.putExtra("type",localModel.getType());
                startActivity(intent);
            }


        });
        samplesAdapter.setOnShareClicked(new DownloadedSamplesAdapter.OnShareClicked() {
            @Override
            public void share(Bitmap bmp) {
              //  shareImage(uri);
               ImageShareHelper imageShareHelper= ImageShareHelper.getInstance(DownloadedPreview.this);
                imageShareHelper.shareImage(bmp);
            }
        });






    }
    public void findView(){

        preView0=findViewById(R.id.vipPreview0);
        preView1=findViewById(R.id.vipPreview1);
        card0=findViewById(R.id.dCard0);
        card1=findViewById(R.id.card1);



    }

    public  void changeFirstOne(String[] array) {

        // Remove the element at index 0
        String element = array[0];
        for (int i = 0; i < array.length - 1; i++) {
            array[i] = array[i + 1];
        }

        // Append the removed element to the end
        array[array.length - 1] = element;

        // Print the updated array
        for (String str : array) {
            System.out.println(str);
        }
    }

}