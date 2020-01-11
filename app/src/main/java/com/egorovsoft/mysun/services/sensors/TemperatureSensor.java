package com.egorovsoft.mysun.services.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.egorovsoft.mysun.MainPresenter;
import com.egorovsoft.mysun.R;

import static android.content.Context.SENSOR_SERVICE;

public final class TemperatureSensor extends WheatherSensorManager {
    private final static String TAG = "TEMPERATURE_SENSOR";

    private static TemperatureSensor instance = null;
    private static Object syncObj = new Object();

    private SensorEventListener sensorEventListener;


    public TemperatureSensor(Context context){
        setActive(false);

        setSensorManager((SensorManager)context.getSystemService(SENSOR_SERVICE));
        setSensor(getSensorManager().getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE));

        setPhoneHaveSensor((getSensor() == null)?false:true);
        setSensorData(String.valueOf(R.string.error));

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(event.values[0]);

                setSensorData(stringBuilder.toString());

                Log.d(TAG, "onSensorChanged: " + getSensorData());

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    @Override
    public void setSensorData(String sensorData) {
        super.setSensorData(sensorData);

        MainPresenter.getInstance().setError(200);
        MainPresenter.getInstance().setTemperature(Float.valueOf(sensorData));
    }

    public static TemperatureSensor getInstance(Context context) {
        synchronized (syncObj) {
            if (instance == null) {
                instance = new TemperatureSensor(context);
            }

            return instance;
        }
    }

    public void registerListener() {
        super.registerListener(sensorEventListener);
    }

    public void unregisterListener() {
        super.unregisterListener(sensorEventListener);
    }
}
