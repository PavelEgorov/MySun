package com.egorovsoft.mysun.activitys;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.egorovsoft.mysun.MainPresenter;
import com.egorovsoft.mysun.R;

public class Act_setting extends AppCompatActivity {

    private static final String TAG = "Act_setting";
    private EditText et_City;
    private EditText et_Country;

    private RadioButton rb_city_name;
    private RadioButton rb_location;
    private RadioButton rb_sensor;

    private RadioButton rb_english;
    private RadioButton rb_russian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_setting);

        et_City = findViewById(R.id.et_city);
        et_Country = findViewById(R.id.et_country);

        rb_city_name = findViewById(R.id.rb_city_name);
        rb_city_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnabled(true);
            }
        });

        rb_location = findViewById(R.id.rb_location);
        rb_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnabled(false);
            }
        });

        rb_sensor = findViewById(R.id.rb_sensor);
        rb_sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnabled(false);
            }
        });

        rb_english = findViewById(R.id.rb_english);
        rb_russian = findViewById(R.id.rb_russian);

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loadData();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");

        saveData();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");

        super.onBackPressed();
    }

    private void saveData() {
        Log.d(TAG, "saveData: ");

        if (rb_russian.isChecked()) MainPresenter.getInstance().setCurrentLanguage(MainPresenter.LN_RUSSIAN);
        if (rb_english.isChecked()) MainPresenter.getInstance().setCurrentLanguage(MainPresenter.LN_ENGLISH);

        if (rb_sensor.isChecked()) MainPresenter.getInstance().setCurrentSettings(MainPresenter.USE_SENSOR);
        if (rb_location.isChecked()) MainPresenter.getInstance().setCurrentSettings(MainPresenter.USE_LOCATION);
        if (rb_city_name.isChecked()) MainPresenter.getInstance().setCurrentSettings(MainPresenter.USE_CITY);

        MainPresenter.getInstance().setCurrentCity(et_City.getText().toString());
        MainPresenter.getInstance().setCurrentCountry(et_Country.getText().toString());

        MainPresenter.getInstance().savePreference(this);
    }

    private void loadData() {
        Log.d(TAG, "loadData: ");

        if (MainPresenter.getInstance().getCurrentSettings() == MainPresenter.USE_LOCATION) {
            rb_location.setChecked(true);
            rb_city_name.setChecked(false);
            rb_sensor.setChecked(false);
        }

        if (MainPresenter.getInstance().getCurrentSettings() == MainPresenter.USE_CITY) {
            rb_location.setChecked(false);
            rb_city_name.setChecked(true);
            rb_sensor.setChecked(false);
        }

        if (MainPresenter.getInstance().getCurrentSettings() == MainPresenter.USE_SENSOR) {
            rb_location.setChecked(false);
            rb_city_name.setChecked(false);
            rb_sensor.setChecked(true);
        }

        if (MainPresenter.getInstance().getCurrentLanguage() == MainPresenter.LN_ENGLISH) {
            rb_english.setChecked(true);
            rb_russian.setChecked(false);
        }else{
            rb_russian.setChecked(true);
            rb_english.setChecked(false);
        }

        et_City.setText(MainPresenter.getInstance().getCurrentCity());
        et_Country.setText(MainPresenter.getInstance().getCurrentCountry());

        updateVisible();
    }

    private void setEnabled(boolean b){
        Log.d(TAG, "setEnabled: ");

        et_City.setEnabled(b);
        et_Country.setEnabled(b);
    }

    private void updateVisible(){
        if (rb_city_name.isChecked()) setEnabled(true);
        else setEnabled(false);
    }
}
