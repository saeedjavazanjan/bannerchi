package com.burhanrashid52.photoediting.Dialogs;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.burhanrashid52.photoediting.InAppBill;
import com.burhanrashid52.photoediting.PurchaseDoneCallback;
import com.burhanrashid52.photoediting.R;
import com.google.android.material.button.MaterialButton;

public class BuySubscribeDialog extends Dialog implements PurchaseDoneCallback {

    final private String THREE_MONTH_PACKAGE_NAME="3monthsubscribe";
    final private String SIX_MONTH_PACKAGE_NAME="6monthsubscribe";
    final private String Twelve_MONTH_PACKAGE_NAME="12monthsubscribe";

    String USER_ID="";

    private DialogListener mListener;


    MaterialButton threeMonth,sixMonth,oneYear;

    Context context;
    FragmentActivity activity;
    InAppBill bazar;
    SharedPreferences sharedPreferences;

    public BuySubscribeDialog(@NonNull Context context, FragmentActivity activity) {
        super(context);

        this.context=context;
        this.activity=activity;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.buy_subscribe_dialog);



        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.copyFrom(this.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        this.getWindow().setAttributes(lp);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        sharedPreferences = context.getSharedPreferences("MySharedPref",MODE_PRIVATE);
        bazar=new InAppBill(context,activity);


        USER_ID=sharedPreferences.getString("phoneNumber","");

        threeMonth=findViewById(R.id.btnThreeMonthBuy);
        sixMonth=findViewById(R.id.btnSixMonthBuy);
        oneYear=findViewById(R.id.btnOneYearBuy);

        threeMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bazar.connectToBazar(THREE_MONTH_PACKAGE_NAME,USER_ID, BuySubscribeDialog.this);

            }
        });

        sixMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bazar.connectToBazar(SIX_MONTH_PACKAGE_NAME,USER_ID,BuySubscribeDialog.this);

            }
        });
        oneYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bazar.connectToBazar(Twelve_MONTH_PACKAGE_NAME,USER_ID,BuySubscribeDialog.this);

            }
        });


    }

    @Override
    public void onPurchaseDone() {
        if (mListener != null) {
            mListener.onDialogResult();
        }
        dismiss();

    }

    public void setDialogListener(DialogListener listener) {
        mListener = listener;
    }
}
