package com.egorovsoft.mysun.geo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.egorovsoft.mysun.MainPresenter;

import static android.content.Context.LOCATION_SERVICE;

public final class Geo {
    private static final String TAG = "GEO";
    private LocationManager locationManager;
    private String provider;

    private static Geo instance = null;
    private static Object syncObj = new Object();

    private Criteria criteria;
    private LocationListener locationListener;
    private boolean isActive;

    private Geo(Context context){
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        provider = locationManager.getBestProvider(criteria, true);

        isActive = false;
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "onLocationChanged: Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude());

                // Широта
                MainPresenter.getInstance().setCurrentLatitude(location.getLatitude());
                // Долгота
                MainPresenter.getInstance().setCurrentLongitude(location.getLongitude());
                // Точность

                MainPresenter.getInstance().startServices(context);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        Log.d(TAG, "Geo");

    }

    public static Geo getInstance(Context context){
        synchronized (syncObj) {
            if (instance == null) {
                instance = new Geo(context);
            }

            return instance;
        }
    }

    public void setIsActive(boolean isActive){
        Log.d(TAG, "setIsActive " + isActive);

        this.isActive = isActive;
    }

    public boolean isActive(){
        Log.d(TAG, "isActive: " + isActive);

        return this.isActive;
    }

    @SuppressLint("MissingPermission")
    public void requestLocation() {
        Log.d(TAG, "requestLocation");

        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 3600000, 30000, locationListener);
        }
    }

    public void unregisterListener(){
        Log.d(TAG, "unregisterListener");

        locationManager.removeUpdates(locationListener);
    }
}
