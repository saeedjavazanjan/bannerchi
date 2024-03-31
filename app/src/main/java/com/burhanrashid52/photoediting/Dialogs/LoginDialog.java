package com.burhanrashid52.photoediting.Dialogs;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.burhanrashid52.photoediting.R;
import com.burhanrashid52.photoediting.api.RetrofitApiImpl;
import com.burhanrashid52.photoediting.models.AdUserDtoModel;
import com.burhanrashid52.photoediting.models.SignInRequestModel;
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
    TextInputLayout typOfPageLayout;
    MaterialButton sendOtpBtn,sendPhoneNumberBtn;
    ConstraintLayout otpLayout,phoneNumberLayout;
    String[] pageType={"شخصی","کاری"};

    String requestType="signUp";
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
        lp.copyFrom(this.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        this.getWindow().setAttributes(lp);

         sharedPreferences = context.getSharedPreferences("MySharedPref",MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
         retrofit =  RetrofitApiImpl.getInstance(context);

        tabLayout = findViewById(R.id.tab_layout);

        otpLayout = findViewById(R.id.otpLayout);

        phoneNumberLayout = findViewById(R.id.signUpLayout);

        userName = findViewById(R.id.userNameEdt);

        phoneNumber = findViewById(R.id.phoneNumberEdt);

        jobTitle = findViewById(R.id.jobTitleEdt);

        otp = findViewById(R.id.otpEdt);

        sendOtpBtn = findViewById(R.id.sendOtpBtn);

        sendPhoneNumberBtn = findViewById(R.id.sendPhoneNumberBtn);

        typOfPageLayout =findViewById(R.id.typOfPage);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line,pageType);
        typeOfPageTxt =(AutoCompleteTextView)findViewById(R.id.txtTypeOfPage);
        typeOfPageTxt.setThreshold(1);
        typeOfPageTxt.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("ثبت نام"));
        tabLayout.addTab(tabLayout.newTab().setText("ورود"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 1) {
                    requestType="signIn";
                    userName.setVisibility(View.GONE);
                    jobTitle.setVisibility(View.GONE);
                    typOfPageLayout.setVisibility(View.GONE);

                }
                if (tabLayout.getSelectedTabPosition() == 0) {
                    requestType="signUp";
                    userName.setVisibility(View.VISIBLE);
                    jobTitle.setVisibility(View.VISIBLE);
                    typOfPageLayout.setVisibility(View.VISIBLE);
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


                if(requestType.equals("signUp")){



                    UserModel userModel=new UserModel(
                            Objects.requireNonNull(phoneNumber.getText()).toString(),
                            typeOfPageTxt.getText().toString(),
                            Objects.requireNonNull(jobTitle.getText()).toString(),
                            Objects.requireNonNull(userName.getText()).toString());
                    signUp(userModel);

                }else if(requestType.equals("signIn")){

                    SignInRequestModel signInRequestModel=new
                            SignInRequestModel(Objects.requireNonNull(phoneNumber.getText()).toString());
                    signIn(signInRequestModel);

                }


            }
        });

        sendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AdUserDtoModel adUserDtoModel=new AdUserDtoModel(
                            Objects.requireNonNull(userName.getText()).toString(),
                            Objects.requireNonNull(phoneNumber.getText()).toString(),
                            typeOfPageTxt.getText().toString(),
                            Objects.requireNonNull(jobTitle.getText()).toString(),
                            Objects.requireNonNull(otp.getText()).toString()
                    );
                    signUpPasswordCheck(adUserDtoModel);
                }

        });
    }


    private void signUp(UserModel userModel){
       retrofit.signUp(userModel,new Callback<ResponseBody>() {
           @Override
           public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

               if (response.isSuccessful()) {
                   try {
                       // Convert response body to string
                       assert response.body() != null;
                       String responseBody = response.body().string();
                       phoneNumberLayout.setVisibility(View.GONE);
                       otpLayout.setVisibility(View.VISIBLE);

                   } catch (IOException e) {
                       e.printStackTrace();
                       Toast.makeText(context,"خطایی رخ داده است",Toast.LENGTH_SHORT).show();
                   }
               } else {
                   if (response.code()==429){
                       Toast.makeText(context,"بعد از یک دقیقه تلاش کنید",Toast.LENGTH_SHORT).show();

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

               Toast.makeText(context,"خطایی رخ داده است",Toast.LENGTH_SHORT).show();

           }
       });

    }

    private void signIn(SignInRequestModel signInRequestModel){
        retrofit.signIn(signInRequestModel, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        // Convert response body to string
                        assert response.body() != null;
                        String responseBody = response.body().string();
                        phoneNumberLayout.setVisibility(View.GONE);
                        otpLayout.setVisibility(View.VISIBLE);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(context,"خطایی رخ داده است",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (response.code()==429){
                        Toast.makeText(context,"بعد از یک دقیقه تلاش کنید",Toast.LENGTH_SHORT).show();

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
                Toast.makeText(context,"خطایی رخ داده است",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void signUpPasswordCheck(AdUserDtoModel adUserDtoModel){
        retrofit.signUpPasswordCheck(adUserDtoModel, new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {
                if (response.isSuccessful()) {

                    // Convert response body to string
                    assert response.body() != null;
                    String responseBody = response.body().getToken();
                    myEdit.putString("token", responseBody);
                    myEdit.commit();

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
            public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                Toast.makeText(context,"خطایی رخ داده است",Toast.LENGTH_SHORT).show();

            }
        });



    }


   /* private String signIn(){

    }*/
}
