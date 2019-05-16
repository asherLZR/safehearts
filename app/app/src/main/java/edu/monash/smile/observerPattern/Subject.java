package edu.monash.smile.observerPattern;

import java.util.HashSet;

/**
 * A Subject emits events by notifying observers.
 * Reference: FIT3077 Software engineering: Architecture and design S1 2019 (Moodle)
 */
public abstract class Subject {
    private final HashSet<Observer> observers = new HashSet<>();

    /**
     * Adds an observer.
     *
     * @param o an observer
     */
    public void attach(Observer o) {
        observers.add(o);
    }

    /**
     * Removes an observer.
     * @param o an observer
     */
    public void detach(Observer o) {
        observers.remove(o);
    }

    /**
     * Notifies observers of some change to data.
     */
    protected void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }

}
