package edu.monash.smile.data.safeheartsModel.observation;

import java.util.Date;

public class BloodPressureObservation extends ShObservation {
    private final SystolicObservation systolicObservation;
    private final DiastolicObservation diastolicObservation;

    /**
     * Model class for a blood pressure measurement composed of both systolic and diastolic
     * measurements.
     */
    public BloodPressureObservation(SystolicObservation systolicObservation,
                                    DiastolicObservation diastolicObservation,
                                    String description,
                                    Date dateObserved) {
        super(description, dateObserved, ObservationType.BLOOD_PRESSURE);
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
