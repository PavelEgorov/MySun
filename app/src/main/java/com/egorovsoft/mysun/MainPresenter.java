package com.egorovsoft.mysun;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.egorovsoft.mysun.observers.Publisher;
import com.egorovsoft.mysun.preference.SPreference;

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

    private boolean needLoadPreference;
    private int currentLanguage;
    private int currentSettings;
    private String currentCity;
    private String currentCountry;

    private Handler handler;
    private Thread threadSP;

    private MainPresenter(){
        Log.d(TAG, "MainPresenter: ");

        currentCity = "";
        currentCountry = "";

        currentLanguage = LN_ENGLISH;
        currentSettings = USE_LOCATION;

        handler = new Handler(Looper.getMainLooper());

        needLoadPreference = true;
    }

    public static MainPresenter getInstance() {
        Log.d(TAG, "getInstance: ");
        synchronized (sync){
            if (instance == null) instance = new MainPresenter();
            return instance;
        }
    }

    public void startServices(){
        Log.d(TAG, "startServices: ");
        ///{{ Запускает необходимые сервисы
        synchronized (sync){

        }
    }

    public void stopServices() {
        Log.d(TAG, "stopServices: ");
        ///{{ Отанавливает все запущенные сервисы
        synchronized (sync){

        }
    }

    public void loadPreference(Context context) {
        Log.d(TAG, "loadPreference: ");
        ///{{ загружаем настройки из SharedPreference
        if (!needLoadPreference) return;

        final SPreference preference = new SPreference(context);

        setCurrentSettings(preference.readSettings());
        setCurrentLanguage(preference.readLanguage());
        setCurrentCountry(preference.readCountry());
        setCurrentCity(preference.readCity());

        setNeedLoadPreference(false);

        Publisher.getInstance().notifyCity(getCurrentCity());
    }

    public void savePreference(Context context) {
        Log.d(TAG, "savePreference: ");
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
        if (tr == null) return;

        Thread dummy = tr;
        tr = null;
        dummy.interrupt();
    }

    private void setNeedLoadPreference(boolean needLoadPreference) {
        synchronized (sync) {
            this.needLoadPreference = needLoadPreference;
        }
    }

    public void setCurrentLanguage(int currentLanguage) {
        synchronized (sync) {
            this.currentLanguage = currentLanguage;
        }
    }

    public void setCurrentSettings(int currentSettings) {
        synchronized (sync) {
            this.currentSettings = currentSettings;
        }
    }

    public void setCurrentCity(String currentCity) {
        synchronized (sync) {
            this.currentCity = currentCity;
        }
    }

    public void setCurrentCountry(String currentCountry) {
        synchronized (sync) {
            this.currentCountry = currentCountry;
        }
    }

    public int getCurrentLanguage() {
        return currentLanguage;
    }

    public int getCurrentSettings() {
        return currentSettings;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public String getCurrentCountry() {
        return currentCountry;
    }
}
