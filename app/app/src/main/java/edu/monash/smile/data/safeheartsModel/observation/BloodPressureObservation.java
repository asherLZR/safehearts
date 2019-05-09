package edu.monash.smile.data.safeheartsModel.observation;

import java.util.Date;

public class BloodPressureObservation extends ShObservation {
    private final SystolicObservation systolicObservation;
    private final DiastolicObservation diastolicObservation;

    public BloodPressureObservation(SystolicObservation systolicObservation, DiastolicObservation diastolicObservation, String description, Date dateObserved) {
        super(description, dateObserved);
        this.systolicObservation = systolicObservation;
        this.diastolicObservation = diastolicObservation;
    }

    public QuantitativeObservation getSystolicObservation() {
        return systolicObservation;
    }

    public QuantitativeObservation getDiastolicObservation() {
        return diastolicObservation;
    }
}
