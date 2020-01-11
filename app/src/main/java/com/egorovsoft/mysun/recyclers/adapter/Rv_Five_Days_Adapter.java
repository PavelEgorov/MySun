package com.egorovsoft.mysun.recyclers.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.egorovsoft.mysun.R;
import com.egorovsoft.mysun.recyclers.Rv_Five_Days;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Rv_Five_Days_Adapter extends RecyclerView.Adapter<Rv_Five_Days_Adapter.ViewHolder>{
    private static final String TAG = "Rv_Five_Days_Adapter";

    private ArrayList<Rv_Five_Days> rv_five_days;

    public Rv_Five_Days_Adapter(ArrayList<Rv_Five_Days> data){
        Log.d(TAG, "Rv_Five_Days_Adapter: ");

        this.rv_five_days = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_day_card, parent, false);

         return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");

        if (rv_five_days.get(position) == null) {
            holder.getDay().setText("");
            holder.getTemp().setText(holder.getTextErr());
            holder.getDesc().setText("");
            if (holder.getWind() != null) holder.getWind().setText("");
            if (holder.getHum() != null) holder.getHum().setText("");
        }else {
            holder.getDay().setText(rv_five_days.get(position).getDay());
            holder.getTemp().setText(String.format("%.1f", rv_five_days.get(position).getTemperature()) + "°C");
            holder.getDesc().setText(rv_five_days.get(position).getDescription());
            if (holder.getWind() != null) holder.getWind().setText(holder.getTextWind() + " " + String.format("%.1f",rv_five_days.get(position).getWind()));
            if (holder.getHum() != null) holder.getHum().setText(holder.getTextHum() + " " + String.format("%d", rv_five_days.get(position).getHumidity()));
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: ");

        return rv_five_days.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView day;
        private TextView temp;
        private TextView desc;

        private String textWind;
        private String textHum;

        private String textErr;

        private TextView wind;
        private TextView hum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "ViewHolder: ");

            setDay((TextView) itemView.findViewById(R.id.tv_card_day));
            setTemp((TextView) itemView.findViewById(R.id.tv_card_temperature));
            setDesc((TextView) itemView.findViewById(R.id.tv_card_description));
            setWind((TextView) itemView.findViewById(R.id.tv_card_wind));
            setHum((TextView) itemView.findViewById(R.id.tv_card_humidity));

            textHum = itemView.getResources().getString(R.string.humidity);
            textWind = itemView.getResources().getString(R.string.wind);
            textErr = itemView.getResources().getString(R.string.error);
        }

        public String getTextErr() {
            return textErr;
        }

        public void setTextErr(String textErr) {
            this.textErr = textErr;
        }

        public void setHum(TextView hum) {
            this.hum = hum;
        }

        public TextView getDay() {
            Log.d(TAG, "getDay: ");

            return day;
        }

        public void setDay(TextView day) {
            Log.d(TAG, "setDay: ");

            this.day = day;
        }

        public TextView getTemp() {
            Log.d(TAG, "getTemp: ");

            return temp;
        }

        public void setTemp(TextView temp) {
            Log.d(TAG, "setTemp: ");

            this.temp = temp;
        }

        public TextView getDesc() {
            Log.d(TAG, "getDesc: ");

            return desc;
        }

        public void setDesc(TextView desc) {
            Log.d(TAG, "setDesc: ");

            this.desc = desc;
        }

        public TextView getWind() {
            return wind;
        }

        public void setWind(TextView wind) {
            this.wind = wind;
        }

        public TextView getHum() {
            return hum;
        }

        public String getTextWind() {
            return textWind;
        }

        public void setTextWind(String textWind) {
            this.textWind = textWind;
        }

        public String getTextHum() {
            return textHum;
        }

        public void setTextHum(String textHum) {
            this.textHum = textHum;
        }
    }
}
