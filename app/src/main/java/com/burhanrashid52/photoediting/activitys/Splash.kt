package com.burhanrashid52.photoediting.activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.burhanrashid52.photoediting.DataFilledCallback
import com.burhanrashid52.photoediting.InAppBill
import com.burhanrashid52.photoediting.R
import com.burhanrashid52.photoediting.api.RetrofitApiImpl
import com.google.android.material.textview.MaterialTextView
import ir.cafebazaar.poolakey.entity.PurchaseInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Splash : AppCompatActivity(),DataFilledCallback {
    private lateinit var sharedPreferences:SharedPreferences
    private lateinit var myEdit:SharedPreferences.Editor
    private lateinit var retrofit:RetrofitApiImpl

    lateinit var progressBar: ProgressBar
    lateinit var versionText:MaterialTextView

    @SuppressLint("CommitPrefEdits", "MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)




        var bazar=InAppBill(this,this)

         bazar.connectForSubscribe(this)
        sharedPreferences = this.getSharedPreferences("MySharedPref", MODE_PRIVATE)
         myEdit = sharedPreferences.edit()
        progressBar=findViewById(R.id.progressBar)
        versionText=findViewById(R.id.txtVersin)

        versionText.text="version: ${getAppVersionName()}"

        retrofit=RetrofitApiImpl.getInstance(this)
        var handler = Handler()

        handler.postDelayed(Runnable {
                                     kotlin.run {

                                         checkVersion()
                                        // overridePendingTransition(R.anim.fade_in,R.anim.fade_out)

                                     }

        },2000)

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onDataFilled(dataList: MutableList<PurchaseInfo>?) {

        dataList.let {
            if(it!!.isNotEmpty())
            myEdit.putString("PURCHASE_TOKEN", it[0].purchaseToken)
            myEdit.apply()

        }
    }


 fun checkVersion(){

     val alertDialogBuilder = AlertDialog.Builder(this)

     // Set the dialog title and message
     alertDialogBuilder.setTitle("Update")
     alertDialogBuilder.setMessage("آیا مایل به آپدیت برنامه هستید؟ ")

     // Set a positive button and its click listener
     alertDialogBuilder.setPositiveButton("به روز رسانی") { dialog, which ->
         // Action to perform when the OK button is clicked
         // For example, dismiss the dialog
         val intent = Intent(Intent.ACTION_VIEW)
         intent.data = Uri.parse("bazaar://details?id=com.saeed.zanjan.bestDesign") // Replace com.yourpackagename with your app's package name
         if (intent.resolveActivity(packageManager) != null) {
             startActivity(intent)
             finish()
         } else {
             // Bazaar not installed, handle accordingly
         }
         dialog.dismiss()

     }

     // Set a negative button and its click listener
     alertDialogBuilder.setNegativeButton("بعدا") { dialog, which ->
         // Action to perform when the Cancel button is clicked
         // For example, dismiss the dialog
         dialog.dismiss()
         intent= Intent(this@Splash,MainActivity::class.java)
         startActivity(intent)
         finish()
     }

     // Create and show the AlertDialog
     val alertDialog = alertDialogBuilder.create()

     alertDialog.setOnShowListener {
         alertDialog.window?.decorView?.layoutDirection = View.LAYOUT_DIRECTION_RTL
     }




     retrofit.getVersion(object : Callback<String?> {
         override fun onResponse(call: Call<String?>, response: Response<String?>) {
             Log.i("versionXXX",getAppVersionName())

             if (getAppVersionName() == response.body()){
                 progressBar.visibility = View.GONE
                 intent= Intent(this@Splash,MainActivity::class.java)
                 startActivity(intent)
                 finish()

             }else{
                 alertDialog.show()
             }

         }
         override fun onFailure(call: Call<String?>, t: Throwable) {
             progressBar.visibility = View.GONE

             Toast.makeText(this@Splash,"خطا در ارتباط",Toast.LENGTH_SHORT).show()

         }
     })
 }

    private fun getAppVersionName(): String {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "Unknown"
        }
    }
}

