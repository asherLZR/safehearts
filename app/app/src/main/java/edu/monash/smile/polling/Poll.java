package edu.monash.smile.polling;

import android.os.Handler;

public class Poll {
    private Handler mHandler;
    private int mInterval;
    private Runnable runnable;
    private PollCallback pollCallback;

    public Poll(int interval, PollCallback pollCallback){
        this.mInterval = interval;
        this.mHandler = new Handler();
        this.pollCallback = pollCallback;
    }

    public void initialisePolling() {
        this.runnable = createRunnable();
        runnable.run();
    }

    private Runnable createRunnable() {
        return () -> {
            try {
                pollCallback.callback();
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
