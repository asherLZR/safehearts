package edu.monash.smile.data.safeheartsModel.observation;

import java.util.Date;

public class SmokingObservation extends ShObservation {
    private final String smokingStatus;

    public SmokingObservation(String smokingStatus, String description, Date dateObserved) {
        super(description, dateObserved, ObservationType.SMOKING);
        this.smokingStatus = smokingStatus;
    }

    public String getSmokingStatus() {
        return smokingStatus;
    }
}
