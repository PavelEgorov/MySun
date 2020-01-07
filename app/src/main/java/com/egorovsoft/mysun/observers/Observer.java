package com.egorovsoft.mysun.observers;

public interface Observer {
    void updateCity(String text);
    void updateTemperature(String text);
    void updateHumidity(String text);
    void updateWind(String text);
    void updateDescription(String text);
}
