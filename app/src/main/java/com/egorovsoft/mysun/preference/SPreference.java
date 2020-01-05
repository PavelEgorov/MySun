package com.egorovsoft.mysun.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SPreference {
    private static final String TAG = "SPreference";

    private static final String BASE_NAME = "Settings";
    private static final String CITY = "city";
    private static final String COUNTRY = "country";
    private static final String USE_SETTINGS = "use_settings";
    private static final String USE_LANGUAGE = "language";

    private String name;
    private SharedPreferences sharedPreferences;

    public SPreference(String name, Context c){
        Log.d(TAG, "SPreference: name, context");

        this.name = name;

        setSharedPreference(c);
    }
    public SPreference(Context c){
        Log.d(TAG, "SPreference: context");

        this.name = BASE_NAME;

        setSharedPreference(c);
    }

    private void setSharedPreference(Context context){
        Log.d(TAG, "setSharedPreference: ");

        sharedPreferences = context.getSharedPreferences(this.name, context.MODE_PRIVATE);
    }

    public void apply(int settings, int language, String country, String city){

    }
}
