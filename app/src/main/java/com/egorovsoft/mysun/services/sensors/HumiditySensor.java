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

public final class HumiditySensor extends WheatherSensorManager {
    private final static String TAG = "HUMIDITY_SENSOR";

    private static HumiditySensor instance = null;
    private static Object syncObj = new Object();

   private SensorEventListener sensorEventListener;


    public HumiditySensor(Context context){
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
        MainPresenter.getInstance().setHumidity(Math.round(Float.valueOf(sensorData)));
    }

    public static HumiditySensor getInstance(Context context) {
        synchronized (syncObj) {
            if (instance == null) {
                instance = new HumiditySensor(context);
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
