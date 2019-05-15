package edu.monash.smile.data.safeheartsModel.observation;

import java.util.Date;

public class SmokingObservation extends StatusObservation {
    public SmokingObservation(String smokingStatus, String description, Date dateObserved) {
        super(description, dateObserved, ObservationType.SMOKING, smokingStatus);
    }
}
