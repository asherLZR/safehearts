package edu.monash.smile.observerPattern;

import java.util.HashSet;

// Code based on code presented in class.
// Reference: FIT3077 Software engineering: Architecture and design S1 2019 (Moodle)
public abstract class Subject {
    private HashSet<Observer> observers = new HashSet<>();

    public void attach(Observer o) {
        observers.add(o);
    }

    public void detach(Observer o) {
        observers.remove(o);
    }

    protected void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }

}
