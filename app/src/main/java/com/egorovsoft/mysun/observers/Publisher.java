package com.egorovsoft.mysun.observers;

import com.egorovsoft.mysun.recyclers.Rv_Five_Days;

import java.util.ArrayList;
import java.util.List;

public class Publisher {
    private static Publisher instance = null;
    private static Object syncObj = new Object();

    private List<Observer> observers;

    private Publisher() {
        observers = new ArrayList<>();
    }

    public static Publisher getInstance(){
        synchronized (syncObj) {
            if (instance == null) {
                instance = new Publisher();
            }

            return instance;
        }
    }

    public void subscribe(Observer observer) {
        synchronized (syncObj) {
            observers.add(observer);
        }
    }

    public void unsubscribe(Observer observer) {
        synchronized (syncObj) {
            observers.remove(observer);
        }
    }

    public void notifyCity(String text) {
        for (Observer observer : observers) {
            observer.updateCity(text);
        }
    }

    public void notifyTemperature(String text) {
        for (Observer observer : observers) {
            observer.updateTemperature(text);
        }
    }

    public void notifyHumidity(String text) {
        for (Observer observer : observers) {
            observer.updateHumidity(text);
        }
    }

    public void notifyWind(String text) {
        for (Observer observer : observers) {
            observer.updateWind(text);
        }
    }

    public void notifyDescription(String text) {
        for (Observer observer : observers) {
            observer.updateDescription(text);
        }
    }

    public void notifyFiveDays(ArrayList<Rv_Five_Days> five_days) {
        for (Observer observer : observers) {
            observer.updateFiveDays(five_days);
        }
    }
}
