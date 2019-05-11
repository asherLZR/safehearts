package edu.monash.smile.data.safeheartsModel.observation;

import java.util.Date;

public class SmokingObservation extends ShObservation {
    private final String smokingStatus;

    public SmokingObservation(String smokingStatus, String description, Date dateObserved,
                              QuantityVariableType quantityVariableType) {
        super(description, dateObserved, quantityVariableType);
        this.smokingStatus = smokingStatus;
    }

    public String getSmokingStatus() {
        return smokingStatus;
    }
}
