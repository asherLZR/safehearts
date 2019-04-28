package edu.monash.smile.polling;

import android.os.Handler;

import java.util.ArrayList;

public class Poll {
    private Handler handler;
    private int interval;
    private Runnable runnable;
    private ArrayList<PollCallback> callbackList;

    public Poll(int interval){
        this.interval = interval;
        this.handler = new Handler();
        this.callbackList = new ArrayList<>();
    }

    public void addCallback(PollCallback pollCallback){
        pollCallback.callback();
        this.callbackList.add(pollCallback);
    }

    public void initialisePolling() {
        this.runnable = createRunnable();
        runnable.run();
    }

    private void execute(){
        for (PollCallback pollCallback : callbackList){
            pollCallback.callback();
        }
    }

    private Runnable createRunnable() {
        return () -> {
            try {
                execute();
            } finally {
                handler.postDelayed(runnable, interval);
            }
        };
    }

    public void stopRepeatingTask() {
        handler.removeCallbacks(runnable);
    }
}
