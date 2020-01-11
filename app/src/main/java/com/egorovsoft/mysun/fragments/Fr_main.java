package com.egorovsoft.mysun.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.egorovsoft.mysun.R;
import com.egorovsoft.mysun.observers.Observer;
import com.egorovsoft.mysun.observers.Publisher;
import com.egorovsoft.mysun.recyclers.Rv_Five_Days;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class Fr_main extends Fragment implements Observer {
    private final static String TAG = "Fr_main";

    public Fr_main() {
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

        return inflater.inflate(R.layout.fragment_fr_main, container, false);
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

    @Override
    public void updateCity(String text) {
        Log.d(TAG, "updateCity: ");

        ((TextView)getActivity().findViewById(R.id.tv_city)).setText(text);
    }

    @Override
    public void updateTemperature(String text) {
        Log.d(TAG, "updateTemperature: ");

        ((TextView)getActivity().findViewById(R.id.tv_temp)).setText(text);
    }

    @Override
    public void updateHumidity(String text) {
        Log.d(TAG, "updateHumidity: ");

        ((TextView)getActivity().findViewById(R.id.tv_humidity)).setText(getResources().getString(R.string.humidity) + text);
    }

    @Override
    public void updateWind(String text) {
        Log.d(TAG, "updateWind: ");

        ((TextView)getActivity().findViewById(R.id.tv_wind)).setText(getResources().getString(R.string.wind) + text);
    }

    @Override
    public void updateDescription(String text) {
        Log.d(TAG, "updateDescription: ");

        int resid = getResources().getIdentifier(text, "string", "com.egorovsoft.mysun");
        ((TextView)getActivity().findViewById(R.id.tv_description)).setText(
                resid != 0? getResources().getText(resid):text
        );
    }

    @Override
    public void updateFiveDays(ArrayList<Rv_Five_Days> five_days) {
    }
}
