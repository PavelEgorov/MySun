package com.egorovsoft.mysun.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.egorovsoft.mysun.MainPresenter;
import com.egorovsoft.mysun.R;
import com.egorovsoft.mysun.recyclers.Rv_Five_Days;
import com.egorovsoft.mysun.recyclers.adapter.Rv_Five_Days_Adapter;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Fr_five_days extends Fragment {
    private final static String TAG = "Fr_five_days";

    public Fr_five_days() {
        Log.d(TAG, "Fr_five_days: ");
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: ");

        Rv_Five_Days[] five_days = MainPresenter.getInstance().getRv_five_days();

        RecyclerView recyclerView = getActivity().findViewById(R.id.rv_weekend);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        Rv_Five_Days_Adapter adapter = new Rv_Five_Days_Adapter(five_days);
        recyclerView.setAdapter(adapter);

        if (MainPresenter.getInstance().isFive_day()){
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);

        initRecyclerView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");

        return inflater.inflate(R.layout.fragment_fr_five_days, container, false);
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
}
