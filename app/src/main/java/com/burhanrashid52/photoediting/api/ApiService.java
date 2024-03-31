package com.burhanrashid52.photoediting.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.burhanrashid52.photoediting.models.ResponseModel;
import com.burhanrashid52.photoediting.models.TitlesModel;
import com.burhanrashid52.photoediting.models.UserModel;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ApiService {
    Context context;
    RequestQueue requestQueue;
    RequestQueue queue;
    int pageSize=10;

    private static ApiService single_instance = null;


    String BaseUrl="http://192.168.1.167:5249/";//"https://devejumpgroup.ir/";

    private ApiService(Context context) {
        this.context = context;
        requestQueue= Volley.newRequestQueue(context);
        queue=Volley.newRequestQueue(context);
        VolleyLog.DEBUG = true;

    }

    public static synchronized ApiService getInstance(Context context)
    {
        if (single_instance == null)
            single_instance = new ApiService(context);

        return single_instance;
    }







public void getAllResponse(int pageNumber,
                                  String searchString,
                                  String type,
                                  Response.Listener<List<ResponseModel>> listener,
                                  Response.ErrorListener errorListener){

        GsonRequest<List<ResponseModel>> gsonRequest=new GsonRequest<List<ResponseModel>>(Request.Method.GET,
                BaseUrl+"packages/filteredResult?pageNumber="+pageNumber+
                        "&search="+searchString+
                        "&type="+type,
                new TypeToken<List<ResponseModel>>(){}.getType(),listener,errorListener);

        requestQueue.add(gsonRequest);


    }



    public void setDownloadCount(int id,
                                 int count,
                                 Response.Listener<String>listener,
                                 Response.ErrorListener errorListener){

        GsonRequest<String> gsonRequest=new GsonRequest<>(
                Request.Method.GET,
                BaseUrl+"download_counting.php?id="+id+"&count="+count
        ,new TypeToken<String>(){}.getType(),listener,errorListener);

       /* JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("count",1);
        gsonRequest.setJsonObject(jsonObject);
     */
        requestQueue.add(gsonRequest);



    }

 public void getOccasion(Response.Listener<List<TitlesModel>> listener,Response.ErrorListener errorListener){

        GsonRequest<List<TitlesModel>>
                gsonRequest=new GsonRequest<>(
                        Request.Method.GET,
                BaseUrl+"occasions/",
                new TypeToken<List<TitlesModel>>(){}.getType(),listener,errorListener);
        requestQueue.add(gsonRequest);

    }
    public void getTemplatesCategory(Response.Listener<List<TitlesModel>> listener,
                                     Response.ErrorListener errorListener){

        GsonRequest<List<TitlesModel>> gsonRequest=new GsonRequest<>(
                Request.Method.GET,
                BaseUrl+"categories/",
                new TypeToken<List<TitlesModel>>(){}.getType(),listener,errorListener);
        requestQueue.add(gsonRequest);

    }

   /* public void getSearchedResponse(String search,
                                    Response.Listener<List<ResponseModel>> listener,
                                    Response.ErrorListener errorListener){

        GsonRequest<List<ResponseModel>> gsonRequest=new GsonRequest<List<ResponseModel>>(Request.Method.GET,
                "http://devejumpgroup.ir/search.php?search="+search,
                new TypeToken<List<ResponseModel>>(){}.getType(),listener,errorListener);

        requestQueue.add(gsonRequest);


    }*/
    /*   public void getPostersResponse(String occasion,
 Response.Listener<List<ResponseModel>> listener, Response.ErrorListener errorListener){

        GsonRequest<List<ResponseModel>> gsonRequest=new GsonRequest<List<ResponseModel>>(Request.Method.GET,
                "https://devejumpgroup.ir/show_news.php?occasion="+occasion,
                new TypeToken<List<ResponseModel>>(){}.getType(),listener,errorListener);

        requestQueue.add(gsonRequest);


    }*/
  /*     public void signUpRequest(UserModel user, Response.Listener<JSONObject> listener,
                              Response.ErrorListener errorListener){
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("Name", "saeed");
            jsonBody.put("PhoneNumber","09193480263");
            jsonBody.put("TypeOfPage", "شخصی");
            jsonBody.put("JobTitle","عمومی");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                BaseUrl+ "users/signUp",
                jsonBody,
                listener,errorListener
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);


    }*/
 public void getSearchedResponse(String search,int pageNumber,
                                    Response.Listener<List<ResponseModel>> listener,
                                    Response.ErrorListener errorListener){

        GsonRequest<List<ResponseModel>> gsonRequest=new GsonRequest<List<ResponseModel>>(Request.Method.GET,
                BaseUrl+"packages/search?query="
                        +search+"&pageNumber="+pageNumber+"&pageSize="+pageSize,
                new TypeToken<List<ResponseModel>>(){}.getType(),listener,errorListener);

        requestQueue.add(gsonRequest);


    }

    public void getResponseByCategory(String category,int pageNumber,
                                    Response.Listener<List<ResponseModel>> listener,
                                    Response.ErrorListener errorListener){

        GsonRequest<List<ResponseModel>> gsonRequest=new GsonRequest<List<ResponseModel>>(Request.Method.GET,
                BaseUrl+"packages/category?category="
                        +category+"&pageNumber="+pageNumber+"&pageSize="+pageSize,
                new TypeToken<List<ResponseModel>>(){}.getType(),listener,errorListener);

        requestQueue.add(gsonRequest);


    }

   // public void sendPhoneNumberAndUserName(Str)

/*  public void getOccasion(Response.Listener<List<TitlesModel>> listener,Response.ErrorListener errorListener){

        GsonRequest<List<TitlesModel>> gsonRequest=
        new GsonRequest<>(Request.Method.GET,
        "http://devejumpgroup.ir/titles.php",
        new TypeToken<List<TitlesModel>>(){}.getType(),listener,errorListener);
        requestQueue.add(gsonRequest);

    }*/
public void getAllTemplatesResponse(int pageNumber,
                                    Response.Listener<List<ResponseModel>> listener,
                                    Response.ErrorListener errorListener){

    GsonRequest<List<ResponseModel>> gsonRequest=new GsonRequest<List<ResponseModel>>(Request.Method.GET,
            BaseUrl+"packages/temps?pageNumber="+pageNumber+"&pageSize="+pageSize,
            new TypeToken<List<ResponseModel>>(){}.getType(),listener,errorListener);

    requestQueue.add(gsonRequest);


}
    public void getAllPostersResponse(int pageNumber,
                                      Response.Listener<List<ResponseModel>> listener,
                                      Response.ErrorListener errorListener){

        GsonRequest<List<ResponseModel>> gsonRequest=new GsonRequest<List<ResponseModel>>(Request.Method.GET,
                BaseUrl+"packages/posters?pageNumber="+pageNumber+"&pageSize="+pageSize,
                new TypeToken<List<ResponseModel>>(){}.getType(),listener,errorListener);

        requestQueue.add(gsonRequest);


    }
}
