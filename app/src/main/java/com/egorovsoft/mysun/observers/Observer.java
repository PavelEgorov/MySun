package com.egorovsoft.mysun.observers;

import com.egorovsoft.mysun.recyclers.Rv_Five_Days;

import java.util.ArrayList;

public interface Observer {
    void updateCity(String text);
    void updateTemperature(String text);
    void updateHumidity(String text);
    void updateWind(String text);
    void updateDescription(String text);
    void updateFiveDays(ArrayList<Rv_Five_Days> five_days);
}
