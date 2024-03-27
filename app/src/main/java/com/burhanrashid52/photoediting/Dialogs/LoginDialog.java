package com.burhanrashid52.photoediting.Dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.burhanrashid52.photoediting.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

public class LoginDialog extends Dialog {

    TabLayout tabLayout;
    TextInputEditText userName,phoneNumber,otp, jobTitle;
    AutoCompleteTextView typeOfPage;
    MaterialButton sendOtpBtn,sendPhoneNumberBtn;
    ConstraintLayout otpLayout,phoneNumberLayout;
    String[] pageType={"شخصی","کاری"};

    Context context;
    public LoginDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(this.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        this.getWindow().setAttributes(lp);
        tabLayout = findViewById(R.id.tab_layout);

        otpLayout = findViewById(R.id.otpLayout);

        phoneNumberLayout = findViewById(R.id.phoneNumberLayout);

        userName = findViewById(R.id.userNameEdt);

        phoneNumber = findViewById(R.id.phoneNumberEdt);

        jobTitle = findViewById(R.id.jobTitleEdt);

        phoneNumber = findViewById(R.id.phoneNumberEdt);

        otp = findViewById(R.id.otpEdt);

        sendOtpBtn = findViewById(R.id.sendOtpBtn);

        sendPhoneNumberBtn = findViewById(R.id.sendPhoneNumberBtn);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line,pageType);
        typeOfPage=(AutoCompleteTextView)findViewById(R.id.txtTypeOfPage);
        typeOfPage.setThreshold(1);
        typeOfPage.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("ثبت نام"));
        tabLayout.addTab(tabLayout.newTab().setText("ورود"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 1) {
                    userName.setVisibility(View.GONE);

                }
                if (tabLayout.getSelectedTabPosition() == 0) {
                    userName.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        sendPhoneNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }
}
