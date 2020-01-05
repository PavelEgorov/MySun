package com.egorovsoft.mysun.activitys;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.egorovsoft.mysun.R;

public class Act_setting extends AppCompatActivity {

    private static final String TAG = "Act_setting";
    private EditText et_City;
    private EditText et_Country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_setting);

        et_City = findViewById(R.id.et_city);
        et_Country = findViewById(R.id.et_country);

        findViewById(R.id.rb_city_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnabled(true);
            }
        });

        findViewById(R.id.rb_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnabled(false);
            }
        });

        findViewById(R.id.rb_sensor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnabled(false);
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

    private void saveData() {
    }

    private void loadData() {

    }

    private void setEnabled(boolean b){
        et_City.setEnabled(b);
        et_Country.setEnabled(b);
    }
}
