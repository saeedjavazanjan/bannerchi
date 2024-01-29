package com.burhanrashid52.photoediting;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    Context context;

    public Preferences(Context context) {
        this.context = context;
    }

   public  SharedPreferences sharedPref = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
}
