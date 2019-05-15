package edu.monash.smile.dashboard.statusTab.alertable;

/**
 * Always returns false for alert state.
 * */
public class NoAlert implements Alertable {
    @Override
    public boolean alertIf() {
        return false;
    }
}
