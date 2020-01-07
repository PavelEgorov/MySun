package com.egorovsoft.mysun.recyclers.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.egorovsoft.mysun.recyclers.Rv_Five_Days;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Rv_Five_Days_Adapter extends RecyclerView.Adapter<Rv_Five_Days_Adapter.ViewHolder>{

    private Rv_Five_Days[] rv_five_days;

    public Rv_Five_Days_Adapter(Rv_Five_Days[] data){
        this.rv_five_days = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
