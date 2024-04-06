package com.burhanrashid52.photoediting.api;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.burhanrashid52.photoediting.models.AdUserDtoModel;
import com.burhanrashid52.photoediting.models.SignInRequestModel;
import com.burhanrashid52.photoediting.models.UserLoginResponse;
import com.burhanrashid52.photoediting.models.UserModel;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiImpl {
    Context context;

    private static RetrofitApiImpl single_instance = null;
    RetrofitApi retrofitAPI ;


    private RetrofitApiImpl(Context context) {
        this.context = context;
        retrofitAPI= retrofit.create(RetrofitApi.class);
    }
    public static synchronized RetrofitApiImpl getInstance(Context context)
    {
        if (single_instance == null)
            single_instance = new RetrofitApiImpl(context);

        return single_instance;
    }



    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.1.167:5249/")
            // as we are sending data in json format so
            // we have to add Gson converter factory
            .addConverterFactory(GsonConverterFactory.create())
            // at last we are building our retrofit builder.
            .build();



    public void signUp(UserModel userModel ,Callback<ResponseBody> callback){
        Call<ResponseBody> call = retrofitAPI.signUp(userModel);

        call.enqueue(callback);
    }
    public void signIn(SignInRequestModel signInRequestModel , Callback<ResponseBody> callback){
        Call<ResponseBody> call = retrofitAPI.signIn(signInRequestModel);

        call.enqueue(callback);
    }
 public void signUpPasswordCheck(AdUserDtoModel adUserDtoModel , Callback<ResponseBody> callback){
        Call<ResponseBody> call = retrofitAPI.signUpPasswordCheck(adUserDtoModel);
        call.enqueue(callback);
    }
public void signInPasswordCheck(SignInRequestModel signInRequestModel , Callback<ResponseBody> callback){
        Call<ResponseBody> call = retrofitAPI.signInPasswordCheck(signInRequestModel);
        call.enqueue(callback);
    }


    public void getVersion(Callback<String>callback){
        Call<String> call= retrofitAPI.getVersion();
        call.enqueue(callback);
    }

}
