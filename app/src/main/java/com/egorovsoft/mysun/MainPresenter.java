package com.egorovsoft.mysun;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.egorovsoft.mysun.observers.Publisher;
import com.egorovsoft.mysun.preference.SPreference;
import com.egorovsoft.mysun.recyclers.Rv_Five_Days;
import com.egorovsoft.mysun.services.api.Citys;
import com.egorovsoft.mysun.services.api.WeatherRequest;
import com.egorovsoft.mysun.services.currentdata.UpdateWheatherService;

import java.util.ArrayList;
import java.util.TreeMap;

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
    public static final String EXTRA_FIVE_DAYS = "FIVE_DAYS";

    public static final String EXTRA_DAY = "DAY";

    private boolean needLoadPreference;
    private int currentLanguage;
    private int currentSettings;
    private String currentCity;
    private String currentCountry;
    private double currentLatitude;
    private double currentLongitude;

    private String currentDescription;

    private Intent intentService;

    private Handler handler;
    private Thread threadSP;

    private float temperature;
    private float wind;
    private int humidity;
    private int error;

    private boolean permission_enternet;
    private boolean permission_location;

    private Citys citys;

    private boolean five_day;
    private ArrayList<Rv_Five_Days> rv_five_days;
    private TreeMap<String, Rv_Five_Days> future_five_days;

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

        five_day = false;
        rv_five_days = new ArrayList<Rv_Five_Days>();
        future_five_days = new TreeMap<String, Rv_Five_Days>();

        needLoadPreference = true;
    }

    public static MainPresenter getInstance() {
        synchronized (sync){
            if (instance == null) instance = new MainPresenter();

            Log.d(TAG, instance.toString() + " getInstance: ");
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
            intentService.putExtra(MainPresenter.EXTRA_FIVE_DAYS, isFive_day());

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

        setFive_day(preference.readFiveDays());

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
                    preference.commit(currentSettings, currentLanguage, currentCountry, currentCity, permission_enternet, permission_location, five_day);

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

    public String getCurrentDescription() {
        Log.d(TAG, "getCurrentDescription: " + currentDescription);
        return currentDescription;
    }

    public void setCurrentDescription(String currentDescription) {
        Log.d(TAG, "setCurrentDescription: " + currentDescription);
        synchronized (sync) {
            this.currentDescription = currentDescription;
        }
    }

    public void updateActivity() {
        Log.d(TAG, this.toString() + " updateActivity: ");
        ///{{ Обновляем активити
        String str_city = getCurrentCity();
        String str_temperature = String.format("%.1f", getTemperature()) + "°C";
        String str_humidity = String.format("%d",getHumidity());
        String str_wind = String.format("%.1f",getWind());
        String str_description = getCurrentDescription();

        int err = getError();
        if (!(err >= 200 && err<=299)){
            str_city  =  "Error";
            str_temperature = String.format("%d",err);
            str_humidity = "";
            str_wind = "";
            str_description = "";
        }

        Publisher.getInstance().notifyCity(str_city);
        Publisher.getInstance().notifyTemperature(str_temperature);
        Publisher.getInstance().notifyHumidity(str_humidity);
        Publisher.getInstance().notifyWind(str_wind);
        Publisher.getInstance().notifyDescription(str_description);

        Publisher.getInstance().notifyFiveDays(rv_five_days);
    }

    public void loadCitys(Context context) {
        Log.d(TAG, "loadCitys: ");

        ///{{ Файл большой, плохая идея его кажый раз грузить, нужен запрос на сайт и выборка частичная(например по геолокации)
        /// Либо нужно попробовать обратиться к файлу через ретрофит. Пока идею с выпадающим списком городов оставлю.
        /// т.к. нет понимания оптимального решения

//        try {
//            String file_citys = FileReader.readFile(context, R.raw.city);
//            Gson gson = new Gson();
//            citys = gson.fromJson(file_citys, Citys.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public Citys getCitys(){
        Log.d(TAG, this.toString() + " getCitys: ");

        return this.citys;
    }

    public boolean isFive_day() {
        Log.d(TAG, this.toString() + " isFive_day: " + five_day);

        return five_day;
    }

    public void setFive_day(boolean five_day) {
        Log.d(TAG, this.toString() + " setFive_day: " + five_day);

        synchronized (sync) {
            if (!five_day){
                future_five_days.clear();
                rv_five_days.clear();
            }

            this.five_day = five_day;
        }
    }

    public ArrayList<Rv_Five_Days> getRv_five_days() {
        Log.d(TAG, this.toString() + " getRv_five_days: ");

        return (rv_five_days);
    }

    public void loadFiveDaysWeather(WeatherRequest[] weather){
        Log.d(TAG, this.toString() + " loadFiveDaysWeather: ");

        if (rv_five_days == null) rv_five_days = new ArrayList<Rv_Five_Days>();
        if (future_five_days == null) future_five_days = new TreeMap<>();

        rv_five_days.clear();
        future_five_days.clear();

        for (WeatherRequest elem : weather
             ) {
            Rv_Five_Days five = new Rv_Five_Days();

            five.setDescription(elem.getWeather()[0].getMain());
            five.setTemperature(elem.getMain().getTemp());
            five.setDay(elem.getDt_txt());

            five.setWind(elem.getWind().getSpeed());
            five.setHumidity(elem.getMain().getHumidity());

            rv_five_days.add(five);

            String key = five.getDay().substring(0, 10);

            if (!future_five_days.containsKey(key)
                    && future_five_days.size() < 4) future_five_days.put(key, five);
        }
    }

    public void createFiveDayError(int error_code) {
        Log.d(TAG, this.toString() + " createFiveDayError: ");

        if (rv_five_days == null) return;

        for (int i=0; i < rv_five_days.size(); i++){
            Rv_Five_Days elem;

            if (rv_five_days.get(i) == null) elem = new Rv_Five_Days();
            else elem = rv_five_days.get(i);

            elem.setDay("error");
            elem.setTemperature(error_code);
            elem.setDescription("");
            elem.setWind(0);
            elem.setHumidity(0);
        }
    }

    public TreeMap<String, Rv_Five_Days> getFuture() {
        Log.d(TAG, this.toString() + " getFuture: ");
        return future_five_days;
    }

    public ArrayList<Rv_Five_Days> getRv_day(String day) {
        ArrayList<Rv_Five_Days> weather = new ArrayList<>();
        for (Rv_Five_Days w: rv_five_days
             ) {
            String key = w.getDay().substring(0, 10);
            if (key.equals(day)){
                Rv_Five_Days f = new Rv_Five_Days();
                f.setDay(w.getDay());
                f.setDescription(w.getDescription());
                f.setTemperature(w.getTemperature());
                f.setWind(w.getWind());
                f.setHumidity(w.getHumidity());

                weather.add(f);
            }
        }
        return weather;
    }
}
