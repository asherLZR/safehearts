package edu.monash.smile.data.safeheartsModel.observation;

import java.math.BigDecimal;
import java.util.Date;

public class SystolicObservation extends QuantitativeObservation {
    public SystolicObservation(BigDecimal value, String unit, String description, Date dateObserved) {
        super(value, unit, description, dateObserved, ObservationType.BLOOD_PRESSURE);
    }
}
