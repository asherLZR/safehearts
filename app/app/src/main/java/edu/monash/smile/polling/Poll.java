package edu.monash.smile.polling;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;

public class Poll {
    private Handler mHandler;
    private int mInterval;
    private Runnable runnable;
    private ArrayList<PollCallback> callbackList;

    public Poll(int interval){
        this.mInterval = interval;
        this.mHandler = new Handler();
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
                Log.i("Debug", "createRunnable: RUN!" + callbackList);
                execute();
            } finally {
                mHandler.postDelayed(runnable, mInterval);
            }
        };
    }

    /**
     * Poll will automatically be destroyed along with any activity. The following is for
     * controlled halting of polling.
     * */
    public void stopRepeatingTask() {
        mHandler.removeCallbacks(runnable);
    }
}
