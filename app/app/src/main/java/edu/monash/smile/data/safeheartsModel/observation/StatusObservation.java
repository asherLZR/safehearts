package edu.monash.smile.data.safeheartsModel.observation;

import java.util.Date;

public abstract class StatusObservation extends ShObservation {
    private final String status;

    /**
     * Model class for an observation with a single text-based status.
     */
    StatusObservation(String description, Date dateObserved, String status) {
        super(description, dateObserved);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
