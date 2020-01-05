package com.egorovsoft.mysun.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.egorovsoft.mysun.R;
import com.egorovsoft.mysun.observers.Observer;
import com.egorovsoft.mysun.observers.Publisher;

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
}
