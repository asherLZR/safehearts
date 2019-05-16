package edu.monash.smile.dashboard.statusTab.observedPatient.alertable;

import java.util.List;

import edu.monash.smile.data.safeheartsModel.observation.ShObservation;

/**
 * Provides a condition to perform alerting on.
 * */
public interface AlertStrategy {
    boolean isAlertRequired(List<? extends ShObservation> observations);
}
