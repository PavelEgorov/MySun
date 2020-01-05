package com.egorovsoft.mysun.observers;

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
}
