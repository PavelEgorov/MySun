package com.egorovsoft.mysun.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.egorovsoft.mysun.MainPresenter;
import com.egorovsoft.mysun.R;
import com.egorovsoft.mysun.filereaders.FileReader;
import com.egorovsoft.mysun.recyclers.Rv_Five_Days;
import com.egorovsoft.mysun.recyclers.adapter.Rv_Five_Days_Adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Act_day extends AppCompatActivity {

    private static final String TAG = "Act_day";

    private RecyclerView recyclerView;
    private Rv_Five_Days_Adapter adapter;
    private ArrayList<Rv_Five_Days> five_days;
    private String day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_day);

        five_days   =   new ArrayList<Rv_Five_Days>();
        day = getIntent().getStringExtra(MainPresenter.EXTRA_DAY);

        findViewById(R.id.btn_act_day_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: ");

        updateElemFiveDays(MainPresenter.getInstance().getRv_day(day));

        recyclerView = (RecyclerView) findViewById(R.id.rv_weekend);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new Rv_Five_Days_Adapter(five_days);
        recyclerView.setAdapter(adapter);

//        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,  LinearLayoutManager.VERTICAL);
//        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator));
//        recyclerView.addItemDecoration(itemDecoration);
    }

    private void updateElemFiveDays(ArrayList<Rv_Five_Days> rv_five_days) {
        this.five_days.clear();
        this.five_days.addAll(rv_five_days);
        for (Rv_Five_Days elem: this.five_days) {
            int resid = getResources().getIdentifier(elem.getDescription(), "string", "com.egorovsoft.mysun");
            elem.setDescription(resid != 0 ? getResources().getString(resid) :elem.getDescription());

            try {
                Date date = FileReader.StringToDate("yyyy-MM-dd HH:mm:ss", elem.getDay());
                SimpleDateFormat formatForDateNow = new SimpleDateFormat("HH.mm");

                elem.setDay(formatForDateNow.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

}
