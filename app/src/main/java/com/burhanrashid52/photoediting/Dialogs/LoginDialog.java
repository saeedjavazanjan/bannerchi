package com.burhanrashid52.photoediting.Dialogs;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.burhanrashid52.photoediting.R;
import com.burhanrashid52.photoediting.api.RetrofitApiImpl;
import com.burhanrashid52.photoediting.models.AdUserDtoModel;
import com.burhanrashid52.photoediting.models.SignInRequestModel;
import com.burhanrashid52.photoediting.models.UserData;
import com.burhanrashid52.photoediting.models.UserLoginResponse;
import com.burhanrashid52.photoediting.models.UserModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginDialog extends Dialog {

    TabLayout tabLayout;
    TextInputEditText userName,phoneNumber,otp, jobTitle;
    AutoCompleteTextView typeOfPageTxt;
    TextInputLayout typOfPageLayout,phoneNumberInL,userNameInL,otpInL;
    MaterialButton sendOtpBtn,sendPhoneNumberBtn;
    ConstraintLayout otpLayout,phoneNumberLayout;
    ProgressBar sendOtpPr,sendPhoneNumberPr;


    String requestType="signUp";
    String [] pageType;
    Context context;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;
    RetrofitApiImpl retrofit;
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
        pageType = context.getResources().getStringArray(R.array.page_types);

        lp.copyFrom(this.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        this.getWindow().setAttributes(lp);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

         sharedPreferences = context.getSharedPreferences("MySharedPref",MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
         retrofit =  RetrofitApiImpl.getInstance(context);

        tabLayout = findViewById(R.id.tab_layout);

        otpLayout = findViewById(R.id.otpLayout);

        phoneNumberLayout = findViewById(R.id.signUpLayout);

        userName = findViewById(R.id.userNameEdt);
        userNameInL=findViewById(R.id.userName);

        phoneNumber = findViewById(R.id.phoneNumberEdt);
        phoneNumberInL=findViewById(R.id.phoneNumber);

        jobTitle = findViewById(R.id.jobTitleEdt);

        otp = findViewById(R.id.otpEdt);
        otpInL = findViewById(R.id.otp);

        sendOtpBtn = findViewById(R.id.sendOtpBtn);

        sendPhoneNumberBtn = findViewById(R.id.sendPhoneNumberBtn);

        sendOtpPr = findViewById(R.id.sendOtpProgress);
        sendPhoneNumberPr = findViewById(R.id.sendPhoneNumberProgress);

        typOfPageLayout =findViewById(R.id.typOfPage);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line,pageType);
        typeOfPageTxt =(AutoCompleteTextView)findViewById(R.id.txtTypeOfPage);
        typeOfPageTxt.setThreshold(1);
        typeOfPageTxt.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText(context.getString(R.string.sign_up)));
        tabLayout.addTab(tabLayout.newTab().setText(context.getString(R.string.sign_in)));


        otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length()>0){
                    otpInL.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    userNameInL.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    phoneNumberInL.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 1) {
                    requestType="signIn";
                    userNameInL.setVisibility(View.GONE);
                    jobTitle.setVisibility(View.GONE);
                    typOfPageLayout.setVisibility(View.GONE);
                    phoneNumberLayout.setVisibility(View.VISIBLE);
                    otpLayout.setVisibility(View.GONE);
                }
                if (tabLayout.getSelectedTabPosition() == 0) {
                    requestType="signUp";
                    userNameInL.setVisibility(View.VISIBLE);
                    jobTitle.setVisibility(View.VISIBLE);
                    typOfPageLayout.setVisibility(View.VISIBLE);
                    phoneNumberLayout.setVisibility(View.VISIBLE);
                    otpLayout.setVisibility(View.GONE);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                phoneNumberLayout.setVisibility(View.VISIBLE);
                otpLayout.setVisibility(View.GONE);
            }
        });

        sendPhoneNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNumber.getText().length() == 0 ||(userName.getText().length() == 0 &&requestType.equals("signUp"))) {
                    if(phoneNumber.getText().length() == 0 ){
                        phoneNumberInL.setErrorEnabled(true);
                        phoneNumberInL.requestFocus();
                        phoneNumberInL.setError("لطفا شماره تلفن را وارد کنید");
                    }

                      if (userName.getText().length() == 0 && requestType.equals("signUp")) {
                        userNameInL.setErrorEnabled(true);
                        userNameInL.requestFocus();
                        userNameInL.setError("لطفا نام کاربری را وارد کنید");
                    }
                } else {
                    sendPhoneNumberPr.setVisibility(View.VISIBLE);
                    sendPhoneNumberBtn.setVisibility(View.GONE);
                    if (requestType.equals("signUp")) {


                        UserModel userModel = new UserModel(
                                Objects.requireNonNull(phoneNumber.getText()).toString(),
                                typeOfPageTxt.getText().toString(),
                                Objects.requireNonNull(jobTitle.getText()).toString(),
                                Objects.requireNonNull(userName.getText()).toString());
                        signUp(userModel);

                    } else if (requestType.equals("signIn")) {

                        SignInRequestModel signInRequestModel = new
                                SignInRequestModel(Objects.requireNonNull(phoneNumber.getText()).toString(), "");
                        signIn(signInRequestModel);

                    }


                }
            }
        });

        sendOtpBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (otp.getText().length() == 0) {
                    otpInL.setErrorEnabled(true);
                    otpInL.requestFocus();
                    otpInL.setError("لطفا رمز را وارد کنید");
                }else {
                    sendOtpPr.setVisibility(View.VISIBLE);
                    sendOtpBtn.setVisibility(View.GONE);
                    AdUserDtoModel adUserDtoModel = new AdUserDtoModel(
                            Objects.requireNonNull(userName.getText()).toString(),
                            Objects.requireNonNull(phoneNumber.getText()).toString(),
                            typeOfPageTxt.getText().toString(),
                            Objects.requireNonNull(jobTitle.getText()).toString(),
                            Objects.requireNonNull(otp.getText()).toString()
                    );
                    if (requestType.equals("signUp"))
                        signUpPasswordCheck(adUserDtoModel);

                    else if (requestType.equals("signIn")) {

                        SignInRequestModel signInRequestModel = new SignInRequestModel(
                                Objects.requireNonNull(phoneNumber.getText()).toString(),
                                Objects.requireNonNull(otp.getText()).toString()
                        );

                        sigInPasswordCheck(signInRequestModel);

                    }
                    ;
                }
                }

        });
    }


    private void signUp(UserModel userModel){


       retrofit.signUp(userModel,new Callback<ResponseBody>() {
           @Override
           public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
               sendPhoneNumberPr.setVisibility(View.GONE);
               sendPhoneNumberBtn.setVisibility(View.VISIBLE);
               if (response.isSuccessful()) {
                   try {
                       // Convert response body to string
                       assert response.body() != null;
                       String responseBody = response.body().string();
                       phoneNumberLayout.setVisibility(View.GONE);
                       otpLayout.setVisibility(View.VISIBLE);

                   } catch (IOException e) {
                       e.printStackTrace();
                       Toast.makeText(context,context.getString(R.string.public_error),Toast.LENGTH_SHORT).show();
                   }
               } else {
                   if (response.code()==429){
                       Toast.makeText(context,context.getString(R.string.rate_limit),Toast.LENGTH_SHORT).show();

                   }


                   String errMsg = null;
                   ResponseBody errorBody = response.errorBody();
                   if (errorBody != null) {
                       try {
                           String errorBodyString = errorBody.string();
                           JSONObject errorJson = new JSONObject(errorBodyString);
                           errMsg = errorJson.getString("error");
                           Toast.makeText(context,errMsg,Toast.LENGTH_SHORT).show();
                       } catch (IOException | JSONException e) {
                           e.printStackTrace();
                           // Handle error parsing response body
                       }
                   }

               }

           }
           @Override
           public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
               sendPhoneNumberPr.setVisibility(View.GONE);
               sendPhoneNumberBtn.setVisibility(View.VISIBLE);
               Toast.makeText(context,context.getString(R.string.public_error),Toast.LENGTH_SHORT).show();

           }
       });

    }

    private void signIn(SignInRequestModel signInRequestModel){
        retrofit.signIn(signInRequestModel, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                sendPhoneNumberPr.setVisibility(View.GONE);
                sendPhoneNumberBtn.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {
                    try {
                        // Convert response body to string
                        assert response.body() != null;
                        String responseBody = response.body().string();
                        phoneNumberLayout.setVisibility(View.GONE);
                        otpLayout.setVisibility(View.VISIBLE);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(context,context.getString(R.string.public_error),Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (response.code()==429){
                        Toast.makeText(context,context.getString(R.string.rate_limit),Toast.LENGTH_SHORT).show();

                    }


                    String errMsg = null;
                    ResponseBody errorBody = response.errorBody();
                    if (errorBody != null) {
                        try {
                            String errorBodyString = errorBody.string();
                            JSONObject errorJson = new JSONObject(errorBodyString);
                            errMsg = errorJson.getString("error");
                            Toast.makeText(context,errMsg,Toast.LENGTH_SHORT).show();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            // Handle error parsing response body
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                sendPhoneNumberPr.setVisibility(View.GONE);
                sendPhoneNumberBtn.setVisibility(View.VISIBLE);
                Toast.makeText(context,context.getString(R.string.public_error),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void signUpPasswordCheck(AdUserDtoModel adUserDtoModel){
        retrofit.signUpPasswordCheck(adUserDtoModel, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                sendOtpPr.setVisibility(View.GONE);
                sendOtpBtn.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {
                    try {
                    assert response.body() != null;
                    String responseBody = response.body().string();
                    JSONObject json = null;

                        json = new JSONObject(responseBody);

                    String token=json.getString("tok");
                    myEdit.putString("token", token);
                        JSONObject user=json.getJSONObject("userData");
                        myEdit.putString("token", token);
                        myEdit.putString("userName",user.getString("name"));
                        myEdit.putString("phoneNumber",user.getString("phoneNumber"));
                        myEdit.putString("typeOfPage",user.getString("typeOfPage"));
                        myEdit.putString("jobTitle",user.getString("jobTitle"));
                        myEdit.commit();
                        dismiss();
                    Toast.makeText(context,context.getString(R.string.Registration_successful),Toast.LENGTH_SHORT).show();
                    } catch (JSONException |IOException e) {
                        Toast.makeText(context,"error",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errMsg = null;
                    ResponseBody errorBody = response.errorBody();
                    if (errorBody != null) {
                        try {
                            String errorBodyString = errorBody.string();
                            JSONObject errorJson = new JSONObject(errorBodyString);
                            errMsg = errorJson.getString("error");
                            Toast.makeText(context,errMsg,Toast.LENGTH_SHORT).show();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context,"error",Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                sendOtpPr.setVisibility(View.GONE);
                sendOtpBtn.setVisibility(View.VISIBLE);
                Toast.makeText(context,context.getString(R.string.public_error),Toast.LENGTH_SHORT).show();

            }
        });



    }

    private void sigInPasswordCheck(SignInRequestModel signInRequestModel){
        retrofit.signInPasswordCheck(signInRequestModel, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                sendOtpPr.setVisibility(View.GONE);
                sendOtpBtn.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {

                    // Convert response body to string
                    try {
                        // Convert response body to string
                        assert response.body() != null;
                        String responseBody = response.body().string();
                        JSONObject json = new JSONObject(responseBody);

                        String token=json.getString("tok");
                        JSONObject user=json.getJSONObject("userData");
                        myEdit.putString("token", token);
                        myEdit.putString("userName",user.getString("name"));
                        myEdit.putString("phoneNumber",user.getString("phoneNumber"));
                        myEdit.putString("typeOfPage",user.getString("typeOfPage"));
                        myEdit.putString("jobTitle",user.getString("jobTitle"));
                        myEdit.commit();
                        dismiss();
                        Toast.makeText(context,context.getString(R.string.Login_successful),Toast.LENGTH_SHORT).show();

                    } catch (IOException |JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context,context.getString(R.string.public_error),Toast.LENGTH_SHORT).show();
                    }

                } else {

                    String errMsg = null;
                    ResponseBody errorBody = response.errorBody();
                    if (errorBody != null) {
                        try {
                            String errorBodyString = errorBody.string();
                            JSONObject errorJson = new JSONObject(errorBodyString);
                            errMsg = errorJson.getString("error");
                            Toast.makeText(context,errMsg,Toast.LENGTH_SHORT).show();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context,"error",Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                sendOtpPr.setVisibility(View.GONE);
                sendOtpBtn.setVisibility(View.VISIBLE);
                Toast.makeText(context,context.getString(R.string.public_error),Toast.LENGTH_SHORT).show();

            }
        });


    }


}
