package com.burhanrashid52.photoediting.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.burhanrashid52.photoediting.R

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var handler = Handler()

        handler.postDelayed(Runnable {
                                     kotlin.run {

                                         intent= Intent(this,MainActivity::class.java)
                                         startActivity(intent)

                                         finish()
                                        // overridePendingTransition(R.anim.fade_in,R.anim.fade_out)

                                     }

        },2000)

    }


}

