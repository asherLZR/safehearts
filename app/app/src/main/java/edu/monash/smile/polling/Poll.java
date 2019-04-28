package edu.monash.smile.polling;

import android.os.Handler;

import java.util.ArrayList;

public class Poll {
    private Handler handler;
    private int interval;
    private Runnable runnable;
    private ArrayList<PollCallback> callbackList;

    /**
     * Creates a poll which calls all callbacks at regular intervals.
     *
     * @param interval The polling interval in milliseconds to run the callback at.
     */
    public Poll(int interval){
        this.interval = interval;
        this.handler = new Handler();
        this.callbackList = new ArrayList<>();
    }

    /**
     * Adds a callback reference to execute.
     */
    public void addCallback(PollCallback pollCallback){
        pollCallback.callback();
        this.callbackList.add(pollCallback);
    }

    /**
     * Starts running polling for the defined interval, for each callback.
     */
    public void initialisePolling() {
        this.runnable = createRunnable();
        runnable.run();
    }

    /**
     * Calls each callback.
     */
    private void execute(){
        for (PollCallback pollCallback : callbackList){
            pollCallback.callback();
        }
    }

    /**
     * Creates a runnable (i.e. code to run in another thread).
     */
    private Runnable createRunnable() {
        return () -> {
            try {
                execute();
            } finally {
                handler.postDelayed(runnable, interval);
            }
        };
    }

    /**
     * This method removes all runnables held by the Poll.
     *
     * It is important to use this method to remove callback references when the object with the
     * callback is no longer needed.
     *
     * Failing to remove callback references will lead to memory leak, as the Poll class will retain
     * a reference to the class with the callback, preventing garbage collection.
     */
    public void stopRepeatingTask() {
        handler.removeCallbacks(runnable);
    }
}
