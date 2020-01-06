package com.egorovsoft.mysun.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import com.egorovsoft.mysun.MainPresenter;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class Permission implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 10;
    private static final int PERMISSION_REQUEST_CODE_INTERNET = 20;
    private static final String TAG = "Permission";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: ");

        if (requestCode == PERMISSION_REQUEST_CODE_LOCATION) {
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                MainPresenter.getInstance().setPermission_location(true);
            }
        }
        if (requestCode == PERMISSION_REQUEST_CODE_INTERNET) {
            if (permissions[0] == Manifest.permission.INTERNET
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                MainPresenter.getInstance().setPermission_enternet(true);
            }
        }
    }

    public void checkPermissionLoacation(Activity activity){
        Log.d(TAG, "checkPermissionLoacation: ");
        
        if (ActivityCompat.checkSelfPermission(activity.getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity.getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            MainPresenter.getInstance().setPermission_location(true);
            Log.d(TAG, "checkPermissionLoacation: true");
        } else {
            Log.d(TAG, "checkPermissionLoacation: false");
            MainPresenter.getInstance().setPermission_location(false);

            requestLocationPermissions(activity);
        }
    }

    private void requestLocationPermissions(Activity activity) {
        Log.d(TAG, "requestLocationPermissions: ");

        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                && !ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE_LOCATION);
        }
    }

    public void checkPermissionInternet(Activity activity){
        Log.d(TAG, "checkPermissionInternet: ");

        if (ActivityCompat.checkSelfPermission(activity.getApplication(), Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            MainPresenter.getInstance().setPermission_enternet(true);
            Log.d(TAG, "checkPermissionInternet: true");
        } else {
            Log.d(TAG, "checkPermissionInternet: false");
            MainPresenter.getInstance().setPermission_enternet(false);

            requestInternetPermissions(activity);
        }
    }

    private void requestInternetPermissions(Activity activity) {
        Log.d(TAG, "requestInternetPermissions: ");

        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.INTERNET)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            Manifest.permission.INTERNET
                    },
                    PERMISSION_REQUEST_CODE_INTERNET);
        }
    }
}
