package edu.monash.smile.dashboard.statusTab.alertable;

import java.util.List;

import edu.monash.smile.data.safeheartsModel.observation.ShObservation;

/**
 * Provides a condition to perform alerting on.
 * */
public interface Alertable {
    boolean alertIf(List<? extends ShObservation> observations);
}
