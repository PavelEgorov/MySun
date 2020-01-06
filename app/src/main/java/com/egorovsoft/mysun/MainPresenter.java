package com.egorovsoft.mysun;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.egorovsoft.mysun.observers.Publisher;
import com.egorovsoft.mysun.preference.SPreference;
import com.egorovsoft.mysun.services.currentdata.UpdateWheatherService;

public class MainPresenter {
    private final static String TAG = "MainPresenter";
    
    private static MainPresenter instance;
    private final static Object sync = new Object();

    public final static int RESULT_CODE_SETTINGS = 1001;

    public final static int USE_LOCATION = 2001;
    public final static int USE_SENSOR = 2002;
    public final static int USE_CITY = 2003;

    public final static int LN_ENGLISH = 3001;
    public final static int LN_RUSSIAN = 3002;

    public static final String EXTRA_COUNTRY = "COUNTRY";
    public static final String EXTRA_CITY = "CITY";
    public static final String EXTRA_USE_LOCATION = "USE_LOCATION";
    public static final String EXTRA_LOCATION_X = "LOCATION_X";
    public static final String EXTRA_LOCATION_Y = "LOCATION_Y";


    private boolean needLoadPreference;
    private int currentLanguage;
    private int currentSettings;
    private String currentCity;
    private String currentCountry;
    private double currentLatitude;
    private double currentLongitude;

    private Intent intentService;

    private Handler handler;
    private Thread threadSP;

    private float temperature;
    private float wind;
    private int humidity;
    private int error;

    private boolean permission_enternet;
    private boolean permission_location;

    private MainPresenter(){
        Log.d(TAG, "MainPresenter: ");

        currentCity = "";
        currentCountry = "";

        currentLanguage = LN_ENGLISH;
        currentSettings = USE_SENSOR;

        currentLatitude = 0.0;
        currentLongitude = 0.0;

        temperature = 0;
        wind = 0;
        humidity = 0;
        error = 200;

        handler = new Handler(Looper.getMainLooper());

        permission_enternet = false;
        permission_location = false;

        needLoadPreference = true;
    }

    public static MainPresenter getInstance() {
        Log.d(TAG, "getInstance: ");
        synchronized (sync){
            if (instance == null) instance = new MainPresenter();
            return instance;
        }
    }

    public void startServices(Context context){
        Log.d(TAG, this.toString() + " startServices: ");
        ///{{ Запускает необходимые сервисы
        synchronized (sync){
            if ((currentSettings == MainPresenter.USE_CITY
                    || currentSettings == MainPresenter.USE_LOCATION)
                    && !isPermission_enternet()) {
                Log.d(TAG, this.toString() + " startServices: not permission internet");
                return;
            }
            
            intentService = new Intent(context, UpdateWheatherService.class);
            intentService.putExtra(MainPresenter.EXTRA_COUNTRY, getCurrentCountry());
            intentService.putExtra(MainPresenter.EXTRA_CITY, getCurrentCity());
            intentService.putExtra(MainPresenter.EXTRA_USE_LOCATION, getCurrentSettings());
            intentService.putExtra(MainPresenter.EXTRA_LOCATION_X, getCurrentLatitude());
            intentService.putExtra(MainPresenter.EXTRA_LOCATION_Y, getCurrentLongitude());

            context.startService(intentService);
        }
    }

    public void stopServices(Context context) {
        Log.d(TAG, this.toString() + " stopServices: ");
        ///{{ Отанавливает все запущенные сервисы
        synchronized (sync){
            if (intentService != null) {
                context.stopService(intentService);
                intentService = null;
            }
        }
    }

    public void loadPreference(Context context) {
        Log.d(TAG, this.toString() + " loadPreference: ");
        ///{{ загружаем настройки из SharedPreference
        if (!needLoadPreference) return;

        final SPreference preference = new SPreference(context);

        setCurrentSettings(preference.readSettings());
        setCurrentLanguage(preference.readLanguage());
        setCurrentCountry(preference.readCountry());
        setCurrentCity(preference.readCity());

        setPermission_enternet(preference.readPermissionInternet());
        setPermission_location(preference.readPermissionLocation());

        setNeedLoadPreference(false);

        Publisher.getInstance().notifyCity(getCurrentCity());
    }

    public void savePreference(Context context) {
        Log.d(TAG, this.toString() + " savePreference: ");
        ///{{ сохраняем настройки из SharedPreference
        synchronized (sync){
            if (threadSP != null){
                ///{{ Если поток уже сохраняет, нам нужно будет перезаписать. Остановим поток т.к. данные уже не актуальны.
                dropThread(threadSP);
            }

            final SPreference preference = new SPreference(context);

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    preference.commit(currentSettings, currentLanguage, currentCountry, currentCity);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ///{{ Передаю в основной поток
                            dropThread(threadSP);
                        }
                    });
                }
            };

            threadSP = new Thread(runnable);
            threadSP.setDaemon(true);
            threadSP.start();
        }
    }

    private void dropThread(Thread tr){
        Log.d(TAG, this.toString() + " dropThread: " + tr.toString());

        if (tr == null) return;

        Thread dummy = tr;
        tr = null;
        dummy.interrupt();
    }

    private void setNeedLoadPreference(boolean needLoadPreference) {
        Log.d(TAG, this.toString() + " setNeedLoadPreference: " + needLoadPreference);

        synchronized (sync) {
            this.needLoadPreference = needLoadPreference;
        }
    }

    public void setCurrentLanguage(int currentLanguage) {
        Log.d(TAG, this.toString() + " setCurrentLanguage: " + currentLanguage);

        synchronized (sync) {
            this.currentLanguage = currentLanguage;
        }
    }

    public void setCurrentSettings(int currentSettings) {
        Log.d(TAG, this.toString() + " setCurrentSettings: " + currentSettings);

        synchronized (sync) {
            this.currentSettings = currentSettings;
        }
    }

    public void setCurrentCity(String currentCity) {
        Log.d(TAG, this.toString() + " setCurrentCity: " + currentCity);

        synchronized (sync) {
            this.currentCity = currentCity;
        }
    }

    public void setCurrentCountry(String currentCountry) {
        Log.d(TAG, this.toString() + " setCurrentCountry: " + currentCountry);

        synchronized (sync) {
            this.currentCountry = currentCountry;
        }
    }

    public void setCurrentLatitude(double currentLatitude) {
        Log.d(TAG, this.toString() + " setCurrentLatitude: " + currentLatitude);

        synchronized (sync) {
            this.currentLatitude = currentLatitude;
        }
    }

    public void setCurrentLongitude(double currentLongitude) {
        Log.d(TAG, this.toString() + " setCurrentLongitude: " + currentLongitude);

        synchronized (sync) {
            this.currentLongitude = currentLongitude;
        }
    }

    public float getTemperature() {
        Log.d(TAG, this.toString() + " getTemperature: " + temperature);

        return temperature;
    }

    public void setTemperature(float temperature) {
        Log.d(TAG, this.toString() + " setTemperature: " + temperature);

        synchronized (sync) {
            this.temperature = temperature;
        }
    }

    public float getWind() {
        Log.d(TAG, this.toString() + " getWind: " + wind);

        return wind;
    }

    public void setWind(float wind) {
        Log.d(TAG, this.toString() + " getWind: " + wind);

        synchronized (sync) {
            this.wind = wind;
        }
    }

    public int getHumidity() {
        Log.d(TAG, this.toString() + " getHumidity: " + humidity);

        return humidity;
    }

    public int getError() {
        Log.d(TAG, this.toString() + " getError: " + error);

        return error;
    }

    public void setError(int error) {
        Log.d(TAG, this.toString() + " setError: " + error);

        this.error = error;
    }

    public void setHumidity(int humidity) {
        Log.d(TAG, this.toString() + " setHumidity: " + humidity);

        synchronized (sync) {
            this.humidity = humidity;
        }
    }

    public int getCurrentLanguage() {
        Log.d(TAG, this.toString() + " getCurrentLanguage: " + currentLanguage);

        return currentLanguage;
    }

    public int getCurrentSettings() {
        Log.d(TAG, this.toString() + " getCurrentSettings: " + currentSettings);

        return currentSettings;
    }

    public String getCurrentCity() {
        Log.d(TAG, this.toString() + " getCurrentCity: " + currentCity);

        return currentCity;
    }

    public String getCurrentCountry() {
        Log.d(TAG, this.toString() + " getCurrentCountry: " + currentCountry);

        return currentCountry;
    }

    public double getCurrentLatitude() {
        Log.d(TAG, this.toString() + " getCurrentLatitude: " + currentLatitude);

        return currentLatitude;
    }

    public double getCurrentLongitude() {
        Log.d(TAG, this.toString() + " getCurrentLongitude: " + currentLongitude);

        return currentLongitude;
    }

    public Handler getHandler() {
        Log.d(TAG, this.toString() + " getHandler: " + handler.toString());

        return handler;
    }

    public boolean isPermission_enternet() {
        Log.d(TAG, this.toString() + " isPermission_enternet: " + permission_enternet);

        return permission_enternet;
    }

    public void setPermission_enternet(boolean permission_enternet) {
        Log.d(TAG, this.toString() + " setPermission_enternet: " + permission_enternet);

        synchronized (sync) {
            this.permission_enternet = permission_enternet;
        }
    }

    public boolean isPermission_location() {
        Log.d(TAG, this.toString() + " isPermission_location: " + permission_location);

        return permission_location;
    }

    public void setPermission_location(boolean permission_location) {
        Log.d(TAG, this.toString() + " setPermission_location: " + permission_location);

        synchronized (sync) {
            this.permission_location = permission_location;
        }
    }

    public void updateActivity() {
        Log.d(TAG, this.toString() + " updateActivity: ");
        ///{{ Обновляем активити
        String str_city = getCurrentCity();
        String str_temperature = String.format("%.1f", getTemperature()) + "°C";
        String str_humidity = String.format("%d",getHumidity());
        String str_wind = String.format("%.1f",getWind());

        int err = getError();
        if (!(err >= 200 && err<=299)){
            str_city  =  "Error";
            str_temperature = String.format("%d",err);
            str_humidity = "";
            str_wind = "";
        }

        Publisher.getInstance().notifyCity(str_city);
        Publisher.getInstance().notifyTemperature(str_temperature);
        Publisher.getInstance().notifyHumidity(str_humidity);
        Publisher.getInstance().notifyWind(str_wind);
    }
}
