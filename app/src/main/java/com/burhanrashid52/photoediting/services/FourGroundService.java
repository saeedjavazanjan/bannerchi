package com.burhanrashid52.photoediting.services;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.burhanrashid52.photoediting.R;

import java.io.File;

public class FourGroundService extends Service {
    String package_URL;
    String package_Name;
    public static final String main_Directory="BestDesign";
    File dir;
    String NOTIFICATION_CHANNEL_ID="default";
    private NotificationManager notificationManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel=new NotificationChannel(NOTIFICATION_CHANNEL_ID,"download",NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      package_URL=  intent.getStringExtra("package_url");
      package_Name=intent.getStringExtra("package_name");

        Notification notification=new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("دانلود پکیج...")
                .build();
        startForeground(101,notification);
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                      DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(package_URL);
                if(CreateDirectory()) {

                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setTitle(getString(R.string.download) + package_Name);
                    request.setDescription("Downloading");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                  //  request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS+ File.separator, package_Name);
                    request.setDestinationUri(Uri.parse("file://" + main_Directory + "/"+package_Name));

                    downloadmanager.enqueue(request);
                }else{
                    Toast.makeText(FourGroundService.this, getString(R.string.public_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
        thread.start();





       // stopForeground(true);
      //  stopSelf();



        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private void notificationUpdate(){
        Notification notification=new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("دانلود پکیج...")
                .setProgress(100,0,false)
                .build();
        startForeground(101,notification);
          notificationManager.notify(101,notification);


    }
    private boolean CreateDirectory() {
        boolean ret = false;
        dir = new File(Environment.getExternalStorageDirectory() + main_Directory);
        if (!dir.exists()) {
            try {
                dir.mkdirs();
                ret = true;

            } catch (Exception e) {
                ret = false;
                e.printStackTrace();
            }
        }
        return ret;
    }

}
