package com.egorovsoft.mysun.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.egorovsoft.mysun.MainPresenter;

public class SPreference {
    private static final String TAG = "SPreference";

    private static final String BASE_NAME = "Settings";
    private static final String CITY = "city";
    private static final String COUNTRY = "country";
    private static final String USE_SETTINGS = "use_settings";
    private static final String USE_LANGUAGE = "language";
    private static final String PM_INTERNET = "pm_internet";
    private static final String PM_LOCATION = "pm_location";
    private static final String FIVE_DAY = "five_day";

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

    public void commit(int settings, int language, String country, String city, boolean internet, boolean location, boolean fiveDay){
        Log.d(TAG, "commit: ");

        sharedPreferences.edit().putString(COUNTRY, country).commit();
        sharedPreferences.edit().putString(CITY, city).commit();
        sharedPreferences.edit().putInt(USE_SETTINGS, settings).commit();
        sharedPreferences.edit().putInt(USE_LANGUAGE, language).commit();

        sharedPreferences.edit().putBoolean(PM_INTERNET, internet).commit();
        sharedPreferences.edit().putBoolean(PM_LOCATION, location).commit();

        sharedPreferences.edit().putBoolean(FIVE_DAY, fiveDay).commit();
    }

    public String readCity(){
        Log.d(TAG, "readCity: ");

        return sharedPreferences.getString(CITY, "");
    }

    public String readCountry(){
        Log.d(TAG, "readCountry: ");

        return sharedPreferences.getString(COUNTRY, "");
    }

    public int readSettings(){
        Log.d(TAG, "readSettings: ");

        return sharedPreferences.getInt(USE_SETTINGS,  MainPresenter.USE_SENSOR);
    }

    public int readLanguage(){
        Log.d(TAG, "readLanguage: ");

        return sharedPreferences.getInt(USE_LANGUAGE, MainPresenter.LN_ENGLISH);
    }

    public boolean readPermissionInternet(){
        Log.d(TAG, "readPermissionInternet: ");

        return sharedPreferences.getBoolean(PM_INTERNET, false);
    }

    public boolean readPermissionLocation(){
        Log.d(TAG, "readPermissionInternet: ");

        return sharedPreferences.getBoolean(PM_LOCATION, false);
    }

    public boolean readFiveDays(){
        Log.d(TAG, "readFiveDays: ");

        return sharedPreferences.getBoolean(FIVE_DAY, false);
    }
}
