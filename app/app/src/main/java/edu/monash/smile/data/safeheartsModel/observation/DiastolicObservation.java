package edu.monash.smile.data.safeheartsModel.observation;

import java.math.BigDecimal;
import java.util.Date;

public class DiastolicObservation extends QuantitativeObservation {
    /**
     * Model class for a diastolic measurement for blood pressure.
     */
    public DiastolicObservation(BigDecimal value, String unit, String description, Date dateObserved) {
        super(value, unit, description, dateObserved, ObservationType.BLOOD_PRESSURE);
    }
}
