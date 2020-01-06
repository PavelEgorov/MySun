package com.egorovsoft.mysun.services.currentdata;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.egorovsoft.mysun.MainPresenter;
import com.egorovsoft.mysun.geo.Geo;
import com.egorovsoft.mysun.services.connections.ConnectionToWheatherServer;
import com.egorovsoft.mysun.services.sensors.HumiditySensor;
import com.egorovsoft.mysun.services.sensors.TemperatureSensor;

import androidx.annotation.Nullable;

public class UpdateWheatherService extends Service {
    private static final String TAG = "UPDATE_WHEATHER_SERVICE";

    private Thread thread;
    private String city;
    private String country;
    private int useLocation;
    private double latitude;
    private double longitude;
    private Runnable runnable;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");

        country = intent.getStringExtra(MainPresenter.EXTRA_COUNTRY);
        city = intent.getStringExtra(MainPresenter.EXTRA_CITY);
        useLocation = intent.getIntExtra(MainPresenter.EXTRA_USE_LOCATION, MainPresenter.USE_LOCATION);
        latitude = intent.getDoubleExtra(MainPresenter.EXTRA_LOCATION_X, 0.0);
        longitude = intent.getDoubleExtra(MainPresenter.EXTRA_LOCATION_Y, 0.0);

        ///{{ Запускаем определение локации
        if (useLocation == MainPresenter.USE_LOCATION) {
            if (!Geo.getInstance(getApplicationContext()).isActive()) {
                Log.d(TAG, "run: requestLocation register");

                Geo.getInstance(getApplicationContext()).setIsActive(true);
                Geo.getInstance(getApplicationContext()).requestLocation();
            }
        }

        destroyThread();

        thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");

        runnable = new Runnable() {
            @Override
            public void run() {
                while (thread != null) {
                    if (thread == null) break;

                    Log.d(TAG, "run: " + thread.toString());

                    try {
                        if (useLocation == MainPresenter.USE_CITY) {
                            Log.d(TAG, "run: USE_CITY");
                            ConnectionToWheatherServer conn = new ConnectionToWheatherServer();
                            conn.refreshDataRetrofit(city, country);
                            conn.close();
                        }
                        if (useLocation == MainPresenter.USE_LOCATION) {
                            Log.d(TAG, "run: USE_LOCATION");

                            ConnectionToWheatherServer conn = new ConnectionToWheatherServer();
                            conn.refreshDataRetrofitLocation(latitude, longitude);
                            conn.close();
                        }
                        if (useLocation == MainPresenter.USE_SENSOR) {
                            Log.d(TAG, "run: USE_SENSOR");
                            MainPresenter.getInstance().setWind(0); /// Нет датчика ветра

                            if (TemperatureSensor.getInstance(getApplicationContext()).phoneHaveSensor()
                                    && !TemperatureSensor.getInstance(getApplicationContext()).isActive()) {
                                TemperatureSensor.getInstance(getApplicationContext()).registerListener();
                            }
                            if (HumiditySensor.getInstance(getApplicationContext()).phoneHaveSensor()
                                    && !HumiditySensor.getInstance(getApplicationContext()).isActive()) {
                                HumiditySensor.getInstance(getApplicationContext()).registerListener();
                            }
                        }

                        MainPresenter.getInstance().getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                MainPresenter.getInstance().updateActivity();
                            }
                        });

                        thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");

        destroyThread();

        ///{{ если сенсоры остались висеть нужно их отключить
        if (HumiditySensor.getInstance(getApplicationContext()).phoneHaveSensor()
                && HumiditySensor.getInstance(getApplicationContext()).isActive()){
            HumiditySensor.getInstance(getApplicationContext()).unregisterListener();
        }
        if (TemperatureSensor.getInstance(getApplicationContext()).phoneHaveSensor()
                && TemperatureSensor.getInstance(getApplicationContext()).isActive()){
            TemperatureSensor.getInstance(getApplicationContext()).unregisterListener();
        }

        ///{{ Останавливаем определение локации
        if (Geo.getInstance(getApplicationContext()).isActive()) {
            Log.d(TAG, "run: requestLocation unregister");

            Geo.getInstance(getApplicationContext()).setIsActive(false);
            Geo.getInstance(getApplicationContext()).unregisterListener();
        }

        super.onDestroy();
    }

    private void destroyThread() {
        if (thread != null) { /// если поток не инициализарован нет смысла его останавливать
            Thread dummy = thread;
            thread = null;
            dummy.interrupt();
        }
    }
}
