package com.burhanrashid52.photoediting.activitys

import android.annotation.SuppressLint
import android.app.Application
import android.app.LocaleManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.LocaleList
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.burhanrashid52.photoediting.DataFilledCallback
import com.burhanrashid52.photoediting.InAppBill
import com.burhanrashid52.photoediting.PhotoApp
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

    private val PREF_SELECTED_LANGUAGE = "selectedLanguage"

    @SuppressLint("CommitPrefEdits", "MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharedPreferences = this.getSharedPreferences("MySharedPref", MODE_PRIVATE)
        myEdit = sharedPreferences.edit()

        if (!isLanguagePreferenceSet()) {
            // If language preference is not set, show language selection dialog
            showLanguageSelectionDialog()
        } else {
            PhotoApp.setLocale(this)

            var bazar = InAppBill(this, this)

            bazar.connectForSubscribe(this)
            progressBar = findViewById(R.id.progressBar)
            versionText = findViewById(R.id.txtVersin)

            versionText.text = "version: ${getAppVersionName()}"

            retrofit = RetrofitApiImpl.getInstance(this)
            var handler = Handler()

            handler.postDelayed(Runnable {
                kotlin.run {

                    checkVersion()
                    // overridePendingTransition(R.anim.fade_in,R.anim.fade_out)

                }

            }, 2000)

        }
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
     alertDialogBuilder.setTitle(getString(R.string.update_title))
     alertDialogBuilder.setMessage(getString(R.string.update_text))
     alertDialogBuilder.setCancelable(false)
     // Set a positive button and its click listener
     alertDialogBuilder.setPositiveButton(getString(R.string.update)) { dialog, which ->
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
     alertDialogBuilder.setNegativeButton(getString(R.string.later)) { dialog, which ->
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

             Toast.makeText(this@Splash,getString(R.string.connection_error),Toast.LENGTH_SHORT).show()

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


    private fun isLanguagePreferenceSet(): Boolean {
        val preference=applicationContext.getSharedPreferences("Language", MODE_PRIVATE)
        return preference.contains(PREF_SELECTED_LANGUAGE)
    }

    private fun showLanguageSelectionDialog() {
        val languages = resources.getStringArray(R.array.languages)

        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle("Select Language")
            .setItems(languages, DialogInterface.OnClickListener { dialog, which ->
                // Save selected language preference
                if(languages[which]=="Persian")

               saveLanguagePreference("fa")
                else
                    saveLanguagePreference("en")


                // Apply selected language and restart activity
                restartActivity()
            })
            .setCancelable(false)
            .show()
    }

    private fun saveLanguagePreference(selectedLanguage: String) {
        val preference=applicationContext.getSharedPreferences("Language", MODE_PRIVATE)
        preference.edit().putString(PREF_SELECTED_LANGUAGE, selectedLanguage).apply()
        PhotoApp.setLocale(this)
    }

    private fun restartActivity() {
        val intent = intent
        finish()
        startActivity(intent)
    }
}

