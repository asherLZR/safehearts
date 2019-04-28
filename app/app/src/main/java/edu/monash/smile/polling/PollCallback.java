package edu.monash.smile.polling;

/**
 * Classes which implement this interface can receive poll notifications each time the poll timer
 * fires.
 */
public interface PollCallback {
    void callback();
}
