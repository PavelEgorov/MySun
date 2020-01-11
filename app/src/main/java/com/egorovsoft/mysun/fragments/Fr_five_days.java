package com.egorovsoft.mysun.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.egorovsoft.mysun.MainPresenter;
import com.egorovsoft.mysun.R;
import com.egorovsoft.mysun.activitys.Act_day;
import com.egorovsoft.mysun.filereaders.FileReader;
import com.egorovsoft.mysun.observers.Observer;
import com.egorovsoft.mysun.observers.Publisher;
import com.egorovsoft.mysun.recyclers.Rv_Five_Days;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

public class Fr_five_days extends Fragment implements Observer {
    private final static String TAG = "Fr_five_days";

    private View root;
    private GridLayout gridLayout;

    public Fr_five_days() {
        Log.d(TAG, "Fr_five_days: ");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");

        Publisher.getInstance().subscribe(this);

        root  = inflater.inflate(R.layout.fragment_fr_five_days, container, false);

        //initRecyclerView(root);
        initGreedLayout();

        return root;
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: ");

        super.onAttach(context);
     }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: ");

        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");

        Publisher.getInstance().unsubscribe(this);

        super.onDestroyView();
    }

    private void initGreedLayout() {
        Log.d(TAG, "initGreedLayout: ");

        gridLayout  =   (GridLayout) root.findViewById(R.id.gl_weekend);
        gridLayout.setColumnCount(2);
        gridLayout.setUseDefaultMargins(true);
        gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout.setPadding(16,16,16,16);

        updateElemGridLayout(MainPresenter.getInstance().getFuture());
    }

    private void updateElemGridLayout(TreeMap<String, Rv_Five_Days> future) {
        gridLayout.removeAllViews();

        for (Map.Entry<String, Rv_Five_Days> m: future.entrySet()) {
            Log.d(TAG, "updateElemGridLayout: Map key: " + m.getKey()+" value: "+m.getValue());

            int resid = getResources().getIdentifier(m.getValue().getDescription(), "string", "com.egorovsoft.mysun");

            View cardView = getLayoutInflater().inflate(R.layout.rv_five_days_card_view, gridLayout, false);

            try {
                Date date = FileReader.StringToDate("yyyy-MM-dd", m.getKey());
                SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM E");

                ((TextView)cardView.findViewById(R.id.tv_card_day)).setText(formatForDateNow.format(date));
            } catch (ParseException e) {
                ((TextView)cardView.findViewById(R.id.tv_card_day)).setText(m.getKey());
                e.printStackTrace();
            }
            ((TextView)cardView.findViewById(R.id.tv_card_temperature)).setText(String.format("%.1f", m.getValue().getTemperature()) + "°C");
            ((TextView)cardView.findViewById(R.id.tv_card_description)).setText(resid != 0 ? getResources().getString(resid) :m.getValue().getDescription());

            cardView.setPadding(16,16,16,16);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: " + m.getKey());
                    ///Откроем подробный прогноз
                    Intent intent = new Intent(root.getContext(), Act_day.class);
                    intent.putExtra(MainPresenter.EXTRA_DAY, m.getKey());
                    startActivity(intent);
                }
            });

            gridLayout.addView(cardView);
        }
    }

    @Override
    public void updateCity(String text) {

    }

    @Override
    public void updateTemperature(String text) {

    }

    @Override
    public void updateHumidity(String text) {

    }

    @Override
    public void updateWind(String text) {

    }

    @Override
    public void updateDescription(String text) {

    }

    @Override
    public void updateFiveDays(ArrayList<Rv_Five_Days> five_days) {
        Log.d(TAG, "updateFiveDays: ");

        updateElemGridLayout(MainPresenter.getInstance().getFuture());
    }
}
