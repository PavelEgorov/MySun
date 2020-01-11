package com.egorovsoft.mysun.services.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public abstract class WheatherSensorManager {
    private final static String TAG = "WheatherSensorManager";
    private static Object syncObj = new Object();

    private boolean isActive;
    private boolean phoneHaveSensor;
    private SensorManager sensorManager;
    private Sensor sensor;
    private String sensorData;


    public boolean isActive(){
        synchronized (syncObj) {
            return isActive;
        }
    };

    public boolean phoneHaveSensor(){
        synchronized (syncObj) {
            return phoneHaveSensor;
        }
    };

    public Sensor getSensor() {
        return sensor;
    };

    public String getSensorData(){
        synchronized (syncObj) {
            return sensorData;
        }
    };

    public void registerListener(SensorEventListener sensorEventListener){
        Log.d(TAG, "registerListener: " + sensorEventListener);

        sensorManager.registerListener(sensorEventListener, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);

        isActive = true;
    };

    public void unregisterListener(SensorEventListener sensorEventListener){
        Log.d(TAG, "unregisterListener: " + sensorEventListener);

        sensorManager.unregisterListener(sensorEventListener, sensor);

        isActive = false;
    };

    public void setSensorManager(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public void setSensorData(String sensorData) {
        this.sensorData = sensorData;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setPhoneHaveSensor(boolean phoneHaveSensor) {
        this.phoneHaveSensor = phoneHaveSensor;
    }

    public boolean isPhoneHaveSensor() {
        return phoneHaveSensor;
    }

    public SensorManager getSensorManager() {
        return sensorManager;
    }
}
