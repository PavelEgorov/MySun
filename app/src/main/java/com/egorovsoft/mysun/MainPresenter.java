package com.egorovsoft.mysun;

import android.content.Context;
import android.util.Log;

public class MainPresenter {
    private final static String TAG = "MainPresenter";
    
    private static MainPresenter instance;
    private final static Object sync = new Object();

    public final static int RESULT_CODE_SETTINGS = 1001;

    private final static int USE_LOCATION = 2001;
    private final static int USE_SENSOR = 2002;
    private final static int USE_CITY = 2003;

    private boolean needLoadPreference;

    private MainPresenter(){
        Log.d(TAG, "MainPresenter: ");

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
    }

    public void stopServices() {
        Log.d(TAG, "stopServices: ");
        ///{{ Отанавливает все запущенные сервисы
    }

    public void loadPreference(Context context) {
        Log.d(TAG, "loadPreference: ");
        ///{{ загружаем настройки из SharedPreference
        if (!needLoadPreference) return;

        needLoadPreference = false;
    }

    public void savePreference(Context context) {
        Log.d(TAG, "savePreference: ");
        ///{{ сохраняем настройки из SharedPreference

    }
}
