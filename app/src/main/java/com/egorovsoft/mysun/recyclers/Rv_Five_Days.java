package com.egorovsoft.mysun.recyclers;

public class Rv_Five_Days {
    private String Day;
    private float temperature;
    private String description;

    private float wind;
    private int humidity;

    private int id;

    public float getWind() {
        return wind;
    }

    public void setWind(float wind) {
        this.wind = wind;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
