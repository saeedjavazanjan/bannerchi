package com.burhanrashid52.photoediting.fragmens;


import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.burhanrashid52.photoediting.DataFilledCallback;
import com.burhanrashid52.photoediting.Dialogs.BuySubscribeDialog;
import com.burhanrashid52.photoediting.Dialogs.DialogListener;
import com.burhanrashid52.photoediting.InAppBill;
import com.burhanrashid52.photoediting.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;
import java.util.Objects;

import ir.cafebazaar.poolakey.entity.PurchaseInfo;


public class ProfileFragment extends Fragment implements DataFilledCallback, DialogListener {
    MaterialTextView userName,phoneNumber,jobTitle,typeOfPage, SubscribeName,SubscribeTime;

    MaterialButton buy;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;
    CardView SubscribeCv;
    InAppBill bazar;

    String name;
    String phone;
    String job;
    String page;
Context currentContext;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
       this.currentContext=context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = Objects.requireNonNull(getContext()).
                getSharedPreferences("MySharedPref",MODE_PRIVATE);
        myEdit = sharedPreferences.edit();


    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        bazar=new InAppBill(currentContext, Objects.requireNonNull(getActivity()));
        bazar.connectForSubscribe(this);

        userName=view.findViewById(R.id.txtUserName);
        phoneNumber=view.findViewById(R.id.txtPhone);
        jobTitle=view.findViewById(R.id.txtJobTitle);
        typeOfPage=view.findViewById(R.id.txtPageType);
        SubscribeCv=view.findViewById(R.id.subscribeCv);
        SubscribeName =view.findViewById(R.id.txtPurchName);
        SubscribeTime=view.findViewById(R.id.txtPurchTime);
        buy=view.findViewById(R.id.btnBuy);

        SubscribeCv.setVisibility(View.GONE);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BuySubscribeDialog buySubscribeDialog=new BuySubscribeDialog(currentContext,getActivity());
                buySubscribeDialog.setDialogListener(ProfileFragment.this); // 'this' refers to the Fragment which implements the listener

                buySubscribeDialog.show();

            }
        });

        name=sharedPreferences.getString("userName",currentContext.getString(R.string.gust));
        phone=sharedPreferences.getString("phoneNumber",currentContext.getString(R.string.empty));
        page=sharedPreferences.getString("typeOfPage","");
        job=sharedPreferences.getString("jobTitle","");

        userName.setText(name);
        phoneNumber.setText(phone);
        jobTitle.setText("عنوان کاری: "+job);
        typeOfPage.setText("نوع صفحه: "+page);



        return view;
    }

    @Override
    public void onDataFilled(List<PurchaseInfo> dataList) {

        if(!dataList.isEmpty() &&!phone.equals(currentContext.getString(R.string.empty))){
            SubscribeCv.setVisibility(View.VISIBLE);
            Long leftTime=calculateExpireTime(dataList.get(0));



            SubscribeName.setText(subscribeName(dataList.get(0)));
            SubscribeTime.setText(String.valueOf(leftTime)+" روز باقی مانده");

            if (leftTime<=0){
                buy.setVisibility(View.VISIBLE);
                SubscribeTime.setText("منقضی شده");
                myEdit.putString("PURCHASE_TOKEN","");
                myEdit.commit();

            }
        }


    }

    private Long calculateExpireTime(PurchaseInfo purchaseInfo){
        Long currentTime=System.currentTimeMillis();
        Long leftTime=0L;
        if (purchaseInfo.getProductId().equals("3monthsubscribe")){
             leftTime=((purchaseInfo.getPurchaseTime()+(2592000000L*3))-currentTime)/86400000;


        }else if(purchaseInfo.getProductId().equals("6monthsubscribe")){
            leftTime=((purchaseInfo.getPurchaseTime()+(2592000000L*6))-currentTime)/86400000;


        }else if(purchaseInfo.getProductId().equals("12monthsubscribe")){
            leftTime=((purchaseInfo.getPurchaseTime()+((2592000000L*12)+518400000))-currentTime)/86400000;

        }else{
            leftTime=((purchaseInfo.getPurchaseTime()+(300000))-currentTime);

        }

        return leftTime;
    }

    private String subscribeName(PurchaseInfo purchaseInfo){
        String name="";
        if (purchaseInfo.getProductId().equals("3monthsubscribe")){
            name="اشتراک سه ماهه";

        }else if(purchaseInfo.getProductId().equals("6monthsubscribe")){
            name="اشتراک شش ماهه";


        }else if(purchaseInfo.getProductId().equals("12monthsubscribe")){
            name="اشتراک یک ساله";


        }
        return name;
    }

    @Override
    public void onDialogResult() {
        bazar.connectForSubscribe(this);


    }
}
