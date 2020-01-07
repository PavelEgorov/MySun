package com.egorovsoft.mysun.services.api;

public class WeatherRequestFiveDay {
    private City city;
    private Coord coord;
    private String country;
    private WeatherRequest[] list;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public WeatherRequest[] getList() {
        return list;
    }

    public void setList(WeatherRequest[] list) {
        this.list = list;
    }
}
