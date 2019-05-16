package edu.monash.smile.dashboard.statusTab.observedPatient.alertable;

import java.util.List;

import edu.monash.smile.data.safeheartsModel.observation.ShObservation;

/**
 * Strategy used to determine if an alert should be shown given the observations.
 */
public interface AlertStrategy {
    boolean isAlertRequired(List<? extends ShObservation> observations);
}
