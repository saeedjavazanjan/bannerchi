package com.burhanrashid52.photoediting;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.burhanrashid52.photoediting.activitys.PreviewActivity;
import com.burhanrashid52.photoediting.api.ApiService;
import com.burhanrashid52.photoediting.api.RetrofitApiImpl;
import com.burhanrashid52.photoediting.database.AppDataBase;
import com.burhanrashid52.photoediting.database.LocalModel;
import com.burhanrashid52.photoediting.database.TaskDao;
import com.burhanrashid52.photoediting.fragmens.BottmSheetFragment;
import com.burhanrashid52.photoediting.models.DownloadDetail;
import com.burhanrashid52.photoediting.services.FileDownloadService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class DownloadPackageHelper {
    Context context;
    TaskDao taskDao;
    Bundle bundle;
    String packageUrl,name,designer,occasion,type,downloadCount;
    int price,id;
    LocalModel localModel;
    public NotificationManager notificationManager;
    public String NOTIFICATION_CHANNEL_ID="default";
    public Notification notification;
    PreviewActivity previewActivity;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public DownloadPackageHelper(Context context, Bundle bundle) {
        this.context = context;
        this.bundle = bundle;


    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void downloading() {
        notificationManager= (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel=new NotificationChannel(NOTIFICATION_CHANNEL_ID,"download",NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(notificationChannel);


        packageUrl = bundle.getString("packageUrl");
        name = bundle.getString("name");
        price=bundle.getInt("price");
        designer=bundle.getString("designer");
        occasion=bundle.getString("occasion");
        type=bundle.getString("type");
        downloadCount=bundle.getString("downloadCount");
        id=bundle.getInt("id");

        localingData();
         previewActivity=new PreviewActivity();

        //checking  file is exist or not!
        String unzipPath = FileUtils.getDataDir(context, "Best_Design").getAbsolutePath();
        int endIndex = packageUrl.length();
        String substring = packageUrl.substring(29, endIndex - 4);
        File extracted = new File(unzipPath + "/" + substring);



                FileDownloadService fileDownloadService = new FileDownloadService();
                NotificationManager finalNotificationManager = notificationManager;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String serverFilePath = packageUrl;

                        String path = FileUtils.getDataDir(context, "Best_Design").getAbsolutePath();

                        String fileName = name;
                        File file = new File(path, fileName);

                        String localPath = file.getAbsolutePath();


                        FileDownloadService.DownloadRequest downloadRequest = new FileDownloadService.DownloadRequest(serverFilePath, localPath);
                        downloadRequest.setRequiresUnzip(true);
                        downloadRequest.setDeleteZipAfterExtract(false);
                        downloadRequest.setUnzipAtFilePath(unzipPath);

                        FileDownloadService.OnDownloadStatusListener listener = new FileDownloadService.OnDownloadStatusListener() {

                            @Override
                            public void onDownloadStarted() {
                                BottmSheetFragment.downLoad.setVisibility(View.GONE);
                                BottmSheetFragment.progressBar.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onDownloadCompleted() {
                                checkAndCreateLocalFiles(extracted);
                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        long result = taskDao.addPackage(localModel);
                                        if (result != -1) {

                                        }

                                    }
                                });

                                Toast.makeText(context, context.getString(R.string.downLoad_complete), Toast.LENGTH_SHORT).show();
                                  finalNotificationManager.cancel(101);
                               // downloadCounting();
                                sendDownloadDetailsToServer(name,id);

                                BottmSheetFragment.downLoad.setVisibility(View.VISIBLE);
                                BottmSheetFragment.progressBar.setVisibility(View.GONE);
                                if(price!=0){
                                   // InAppBill.consumeProduct();
                                }
                            }

                            @Override
                            public void onDownloadFailed() {
                                Toast.makeText(context, context.getString(R.string.public_error), Toast.LENGTH_SHORT).show();
                                  finalNotificationManager.cancel(101);
                                BottmSheetFragment.downLoad.setVisibility(View.VISIBLE);
                                BottmSheetFragment.progressBar.setVisibility(View.GONE);

                            }

                            @Override
                            public void onDownloadProgress(int progress) {
                                //  Toast.makeText(PreviewActivity.this, "prog", Toast.LENGTH_SHORT).show();
                                notificationUpdate(progress);

                            }
                        };

                        FileDownloadService.FileDownloader downloader = FileDownloadService.FileDownloader.getInstance(downloadRequest, listener);
                        downloader.download(context);


                    }
                });

        thread.start();


     //   }

}


    public void localingData () {
        //localDatabase
        taskDao = AppDataBase.getAppDataBase(context).getTaskDao();
        localModel = new LocalModel();
        localModel.setDesigner(designer);
        localModel.setName(name);
        localModel.setOcassion(occasion);
        localModel.setType(type);


    }

    public void checkAndCreateLocalFiles (File extracted){

                File[] images = extracted.listFiles();
                String[] imagesPath = new String[images.length];
                for (int i = 0; i < images.length; i++) {
                    imagesPath[i] = images[i].getPath();

                }
                String sample = Arrays.toString(imagesPath);
                StringBuilder build = new StringBuilder(sample);
                build.deleteCharAt(0);
                build.deleteCharAt(sample.length() - 2);
                localModel.setSamples(build.toString());
                localModel.setPackageURI(extracted.getPath());
                localModel.setHeaderURI(imagesPath[0]);



    }

    public void notificationUpdate(int progress) {
          notification = new NotificationCompat.Builder(context, "default")
                .setContentTitle(context.getResources().getString(R.string.app_name))
                  .setSilent(true)
                .setContentText("دانلود پکیج...")
                .setSmallIcon(R.drawable.ic_baseline_save_alt_24)
                .setProgress(100, progress, false)
                .build();
        notificationManager.notify(101, notification);
    }
    public void downloadCounting(){
        ApiService apiService= ApiService.getInstance(context);
        int count= Integer.parseInt(downloadCount)+1;
        apiService.setDownloadCount(id, count, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });



    }

    public void sendDownloadDetailsToServer(String packName,int packId){
        SharedPreferences  sharedPreferences = context.getSharedPreferences("MySharedPref",MODE_PRIVATE);
        String userPurchaseToken =sharedPreferences.getString("PURCHASE_TOKEN","");
        String userToken=sharedPreferences.getString("token","");
        DownloadDetail downloadDetail=new DownloadDetail(
                userPurchaseToken,
                packName,
                packId);

        RetrofitApiImpl retrofit=RetrofitApiImpl.getInstance(context);
        retrofit.sendDownloadDetail(userToken,downloadDetail, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                if(response.isSuccessful()){

                }else {
                    String errMsg = null;
                    ResponseBody errorBody = response.errorBody();
                    if (errorBody != null) {
                        try {
                            String errorBodyString = errorBody.string();
                            JSONObject errorJson = new JSONObject(errorBodyString);
                            errMsg = errorJson.getString("error");
                            Toast.makeText(context,errMsg,Toast.LENGTH_SHORT).show();
                        } catch (IOException | JSONException e) {
                            Toast.makeText(context,context.getString(R.string.public_error),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context,context.getString(R.string.public_error),Toast.LENGTH_SHORT).show();

            }
        });


    }
}

