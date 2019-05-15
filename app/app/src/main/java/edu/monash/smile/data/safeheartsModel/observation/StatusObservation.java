package edu.monash.smile.data.safeheartsModel.observation;

import java.util.Date;

public class StatusObservation extends ShObservation {
    private final String status;

    StatusObservation(String description, Date dateObserved, ObservationType observationType, String status) {
        super(description, dateObserved, observationType);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
