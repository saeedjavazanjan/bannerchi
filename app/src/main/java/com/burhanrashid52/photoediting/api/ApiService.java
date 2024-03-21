package com.burhanrashid52.photoediting.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.burhanrashid52.photoediting.models.ResponseModel;
import com.burhanrashid52.photoediting.models.TitlesModel;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ApiService {
    Context context;
    RequestQueue requestQueue;

    public ApiService(Context context) {
        this.context = context;
        requestQueue= Volley.newRequestQueue(context);

    }

    public void getPostersResponse(String occasion, Response.Listener<List<ResponseModel>> listener, Response.ErrorListener errorListener){

        GsonRequest<List<ResponseModel>> gsonRequest=new GsonRequest<List<ResponseModel>>(Request.Method.GET,
                "https://devejumpgroup.ir/show_news.php?occasion="+occasion,
                new TypeToken<List<ResponseModel>>(){}.getType(),listener,errorListener);

        requestQueue.add(gsonRequest);


    }

    public void getTemplatesResponse(String category, Response.Listener<List<ResponseModel>> listener, Response.ErrorListener errorListener){

        GsonRequest<List<ResponseModel>> gsonRequest=new GsonRequest<List<ResponseModel>>(Request.Method.GET,
                "https://devejumpgroup.ir/show_news.php?occasion="+category,
                new TypeToken<List<ResponseModel>>(){}.getType(),listener,errorListener);

        requestQueue.add(gsonRequest);


    }
    public void setDownloadCount(int id,int count,Response.Listener<String>listener,Response.ErrorListener errorListener){

        GsonRequest<String> gsonRequest=new GsonRequest<>(Request.Method.GET,"http://devejumpgroup.ir/download_counting.php?id="+id+"&count="+count
        ,new TypeToken<String>(){}.getType(),listener,errorListener);

       /* JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("count",1);
        gsonRequest.setJsonObject(jsonObject);
     */
        requestQueue.add(gsonRequest);



    }
    public void getOccasion(Response.Listener<List<TitlesModel>> listener,Response.ErrorListener errorListener){

        GsonRequest<List<TitlesModel>> gsonRequest=new GsonRequest<>(Request.Method.GET,"http://devejumpgroup.ir/titles.php",new TypeToken<List<TitlesModel>>(){}.getType(),listener,errorListener);
        requestQueue.add(gsonRequest);

    }
    public void getTemplatesCategory(Response.Listener<List<TitlesModel>> listener,Response.ErrorListener errorListener){

        GsonRequest<List<TitlesModel>> gsonRequest=new GsonRequest<>(Request.Method.GET,"http://devejumpgroup.ir/titles.php",new TypeToken<List<TitlesModel>>(){}.getType(),listener,errorListener);
        requestQueue.add(gsonRequest);

    }

    public void getSearchedResponse(String search, Response.Listener<List<ResponseModel>> listener, Response.ErrorListener errorListener){

        GsonRequest<List<ResponseModel>> gsonRequest=new GsonRequest<List<ResponseModel>>(Request.Method.GET,
                "http://devejumpgroup.ir/search.php?search="+search,
                new TypeToken<List<ResponseModel>>(){}.getType(),listener,errorListener);

        requestQueue.add(gsonRequest);


    }



}
