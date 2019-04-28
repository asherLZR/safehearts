package edu.monash.smile.observerPattern;

/**
 * Implemented by classes which need updates from a Subject.
 * Reference: FIT3077 Software engineering: Architecture and design S1 2019 (Moodle)
 */
public interface Observer {
    void update();
}
