package com.burhanrashid52.photoediting.firebase

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.burhanrashid52.photoediting.R
import com.burhanrashid52.photoediting.activitys.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId="notification_channel"
const val channelName="BannerChy"
class MyFirebaseMessagingService:FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {

        if(message.notification !=null){
            generateNotification(message.notification!!.title!!,message.notification!!.body!!)

        }



    }
@SuppressLint("RemoteViewLayout")
fun getRemoteView(title: String,massage: String):RemoteViews{
    val remotView=RemoteViews("com.saeed.zanjan.bestDesign",R.layout.notification)
    remotView.setTextViewText(R.id.title,title)
    remotView.setTextViewText(R.id.message,massage)
    remotView.setImageViewResource(R.id.icon,R.drawable.ic_baseline_notifications_active_24)

    return remotView
}
    fun generateNotification(title:String, massage:String){
        val intent= Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)
        var builder:NotificationCompat.Builder=NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder=builder.setContent(getRemoteView(title,massage))

        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel= NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)

        }
        notificationManager.notify(0,builder.build())


    }
}