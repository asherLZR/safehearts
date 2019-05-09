package edu.monash.smile.data.safeheartsModel.observation;

import java.util.Date;

public class SmokingObservation extends Observation {
    private final String smokingStatus;

    public SmokingObservation(String smokingStatus, String description, Date dateObserved) {
        super(description, dateObserved);
        this.smokingStatus = smokingStatus;
    }

    public String getSmokingStatus() {
        return smokingStatus;
    }
}
