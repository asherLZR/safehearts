package edu.monash.smile.data.safeheartsModel.observation;

import java.util.Date;

abstract class StatusObservation extends ShObservation {
    private final String status;

    /**
     * Model class for an observation with a single text-based status.
     */
    StatusObservation(String description, Date dateObserved, ObservationType observationType, String status) {
        super(description, dateObserved, observationType);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
