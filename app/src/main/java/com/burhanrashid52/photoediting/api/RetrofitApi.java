package com.burhanrashid52.photoediting.api;

import com.burhanrashid52.photoediting.models.AdUserDtoModel;
import com.burhanrashid52.photoediting.models.SignInRequestModel;
import com.burhanrashid52.photoediting.models.UserLoginResponse;
import com.burhanrashid52.photoediting.models.UserModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitApi {

    @POST("users/signUp")
        //on below line we are creating a method to post our data.
    Call<ResponseBody> signUp(@Body UserModel userModel);
    @POST("users/signIn")
        //on below line we are creating a method to post our data.
    Call<ResponseBody> signIn(@Body SignInRequestModel signInRequestModel);
@POST("users/signUpPasswordCheck")
        //on below line we are creating a method to post our data.
    Call<UserLoginResponse> signUpPasswordCheck(@Body AdUserDtoModel adUserDtoModel);
}
