package com.egorovsoft.mysun.services.api;

public class Weather {
    private String main;
    private String description;
    private String icon; //http://openweathermap.org/img/wn/10d@2x.png

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }
}
