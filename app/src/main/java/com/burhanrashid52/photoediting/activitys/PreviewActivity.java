package com.burhanrashid52.photoediting.activitys;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.burhanrashid52.photoediting.Adapters.SamplesAdapter;
import com.burhanrashid52.photoediting.CustomFrameLayout;
import com.burhanrashid52.photoediting.R;
import com.burhanrashid52.photoediting.ZoomOutPageTransformer;
import com.burhanrashid52.photoediting.database.LocalModel;
import com.burhanrashid52.photoediting.database.TaskDao;
import com.burhanrashid52.photoediting.fragmens.BottmSheetFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PreviewActivity extends AppCompatActivity {
   // ResponseModel responseModel;
    LocalModel localModel;
  //  ImageView Header,bookmark;
  //  TextView title,designerName,downloadCountTextview;
    ViewPager2 preView0,preView1;
    FrameLayout cardView1;
    CustomFrameLayout cardView0;
    String sampleURLs;
    String[] samples;
    List<String>itemsURL=new ArrayList<>();
    SamplesAdapter samplesAdapter;
    FloatingActionButton moreInfo;

    TaskDao taskDao;
    LinearLayout indicator0,indicator1;
    String name,packageUrl,designer,occasion,downloadCount,headerUrl,samplesUrl,type;
    int price,id;
    boolean saved;
    public NotificationManager notificationManager;
    public String NOTIFICATION_CHANNEL_ID="default";
    public Notification notification;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        packageUrl=intent.getStringExtra("packageUrl");
        designer=intent.getStringExtra("designer");
        occasion=intent.getStringExtra("occasion");
        downloadCount=intent.getStringExtra("downloadCount");
        headerUrl=intent.getStringExtra("headerUrl");
        price=intent.getIntExtra("price",0);
        samplesUrl=intent.getStringExtra("samples");
        type=intent.getStringExtra("type");
        id=intent.getIntExtra("id",0);


        notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        findView();





        //viewPager
        sampleURLs=samplesUrl;
        samples= sampleURLs.split(",");
        itemsURL=Arrays.asList(samples);
        samplesAdapter=new SamplesAdapter(itemsURL);

        if(type.equals("0")){
            preView0.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    getCurrentIndicator(position,indicator0);

                }
            });
            cardView0.setVisibility(View.VISIBLE);
            cardView1.setVisibility(View.GONE);
            preView0.setAdapter(samplesAdapter);
            preView0.setPageTransformer(new ZoomOutPageTransformer());
            setIndicator(indicator0);
            getCurrentIndicator(0,indicator0);

        }else{
            preView1.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    getCurrentIndicator(position,indicator1);

                }
            });
            cardView1.setVisibility(View.VISIBLE);
            cardView0.setVisibility(View.GONE);
            preView1.setAdapter(samplesAdapter);
            preView1.setPageTransformer(new ZoomOutPageTransformer());
            setIndicator(indicator1);
            getCurrentIndicator(0,indicator1);

        }


        //


        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottmSheetFragment bottmSheetFragment=new BottmSheetFragment();
                Bundle bundle=new Bundle();
                bundle.putString("name",name);
                bundle.putString("designer",designer);
                bundle.putString("downloadCount",downloadCount);
                bundle.putInt("price",price);
                bundle.putString("occasion",occasion);
                bundle.putString("headerUrl",headerUrl);
                bundle.putString("samplesUrl",samplesUrl);
                bundle.putString("packageUrl",packageUrl);
                bundle.putString("type",type);
                bundle.putInt("id",id);
                bottmSheetFragment.setArguments(bundle);
                bottmSheetFragment.show(getSupportFragmentManager(),bottmSheetFragment.getTag());
            }
        });

    }




    public void findView(){
        preView0=findViewById(R.id.vipPreview0);
        preView1=findViewById(R.id.vipPreview1);
        moreInfo =findViewById(R.id.fabDownLoad);
        indicator0=findViewById(R.id.linIndicator0);
        indicator1=findViewById(R.id.linIndicator1);
        cardView0=findViewById(R.id.card0);
        cardView1=findViewById(R.id.card1);


    }


    public void setIndicator(LinearLayout indicator){
        ImageView[] indicators=new ImageView[itemsURL.size()];
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT );
        layoutParams.setMargins(8,8,8,8);
        for (int i = 0; i <indicators.length ; i++) {
            indicators[i]=new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.circle));
            indicators[i].setLayoutParams(layoutParams);
            indicator.addView(indicators[i]);



        }


    }
    public void getCurrentIndicator(int Index,LinearLayout indicator){
        int child=indicator.getChildCount();
        for (int i = 0; i <child ; i++) {
            ImageView imageView=(ImageView)indicator.getChildAt(i);

            if (i==Index){
                imageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.darkcircle));
            }else{
                imageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.circle));


            }


        }



    }



}


