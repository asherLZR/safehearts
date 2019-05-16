package edu.monash.smile.dashboard.statusTab.observedPatient.alertable;

import java.util.List;

import edu.monash.smile.data.safeheartsModel.observation.ShObservation;

/**
 * Always returns false for alert state.
 * */
public class NoAlert implements Alertable {
    @Override
    public boolean alertIf(List<? extends ShObservation> observations) {
        return false;
    }
}
