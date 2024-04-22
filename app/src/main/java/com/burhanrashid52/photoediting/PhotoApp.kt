package com.burhanrashid52.photoediting

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.Locale


/**
 * Created by Burhanuddin Rashid on 1/23/2018.
 */
class PhotoApp : Application() {
    private val PREF_SELECTED_LANGUAGE = "selectedLanguage"

    override fun onCreate() {
        super.onCreate()
        photoApp = this
    }

    companion object {
        var photoApp: PhotoApp? = null
            private set
        private val TAG = PhotoApp::class.java.simpleName

        fun setLocale(context: Context) {
            val preferences = context.getSharedPreferences("Language", MODE_PRIVATE)
            val selectedLanguage = preferences.getString("selectedLanguage", "en") ?: "en"
            val localeNew = Locale(selectedLanguage)
            Locale.setDefault(localeNew)

            val res: Resources = context.resources
            val newConfig = Configuration()
            newConfig.locale = localeNew
            newConfig.setLayoutDirection(localeNew)
            res.updateConfiguration(newConfig, res.displayMetrics)

            newConfig.setLocale(localeNew)
            context.createConfigurationContext(newConfig)
            /* val preferences = context.getSharedPreferences("Language", MODE_PRIVATE)
            val selectedLanguage = preferences.getString("selectedLanguage", "en") ?: "en"

            val locale = Locale(selectedLanguage)
            Locale.setDefault(locale)
            val configuration = Configuration(context.resources.configuration)
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale);

            context.resources.updateConfiguration(configuration, context.resources.displayMetrics)*/
        }
    }

  /*  override fun attachBaseContext(base: Context) {
        super.attachBaseContext(updateBaseContextLocale(base))
    }

    private fun updateBaseContextLocale(base: Context): Context {
        val preferences = base.getSharedPreferences("Language", Context.MODE_PRIVATE)
        val selectedLanguage = preferences.getString("selectedLanguage", "en") ?: "en"

        val locale = Locale(selectedLanguage)
        Locale.setDefault(locale)

        val configuration = Configuration(base.resources.configuration)
        configuration.setLocale(locale)
        base.resources.updateConfiguration(configuration, base.resources.displayMetrics)

        return base.createConfigurationContext(configuration)
    }*/
}