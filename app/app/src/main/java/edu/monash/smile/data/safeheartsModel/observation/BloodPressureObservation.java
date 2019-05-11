package edu.monash.smile.data.safeheartsModel.observation;

import java.util.Date;

public class BloodPressureObservation extends ShObservation {
    private final SystolicObservation systolicObservation;
    private final DiastolicObservation diastolicObservation;

    public BloodPressureObservation(SystolicObservation systolicObservation, DiastolicObservation diastolicObservation, String description, Date dateObserved,
                                    QuantityVariableType quantityVariableType) {
        super(description, dateObserved, quantityVariableType);
        this.systolicObservation = systolicObservation;
        this.diastolicObservation = diastolicObservation;
    }

    public SystolicObservation getSystolicObservation() {
        return systolicObservation;
    }

    public DiastolicObservation getDiastolicObservation() {
        return diastolicObservation;
    }
}
