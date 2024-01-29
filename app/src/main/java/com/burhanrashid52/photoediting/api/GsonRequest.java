package com.burhanrashid52.photoediting.api;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

public class GsonRequest<T> extends Request<T> {
    private Type type;
    private Response.Listener<T> listener;
    JsonObject jsonObject;


    public GsonRequest(int method, String url, Type type, Response.Listener listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.type=type;
        this.listener=listener;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {


            String json = new String(response.data);
            Gson gson = new Gson();
            T result = gson.fromJson(json, type);

            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {

            return Response.error(new VolleyError());


        }


    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if(jsonObject!=null){
         return jsonObject.toString().getBytes();
        }else {
            return super.getBody();
        }
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);



    }
}
