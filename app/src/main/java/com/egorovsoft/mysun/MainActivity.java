package com.egorovsoft.mysun;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.egorovsoft.mysun.activitys.Act_setting;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");

        ///{{ Загружаем настройки
        MainPresenter.getInstance().loadPreference(this);
        ///{{ Запускаем сервисы
        MainPresenter.getInstance().startServices();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: ");

        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: ");

        int id = item.getItemId();

        switch (id){
            case R.id.m_item_settings:
                Log.d(TAG, "onOptionsItemSelected: m_item_settings");

                startActivityForResult(new Intent(MainActivity.this, Act_setting.class), MainPresenter.RESULT_CODE_SETTINGS);

                return super.onOptionsItemSelected(item);
            default:
                Log.d(TAG, "onOptionsItemSelected: default");

                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult: ");

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        if (requestCode == MainPresenter.RESULT_CODE_SETTINGS) {
            Log.d(TAG, "onActivityResult: RESULT_CODE_SETTINGS");

            restartActivity();
        }
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");

        MainPresenter.getInstance().stopServices();
        super.onStop();
    }

    private void restartActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
