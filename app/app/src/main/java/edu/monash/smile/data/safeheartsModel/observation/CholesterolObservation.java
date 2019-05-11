package edu.monash.smile.data.safeheartsModel.observation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class CholesterolObservation extends QuantitativeObservation {
    /**
     * A single cholesterol observation of a patient.
     *
     * @param value        The metric recorded in the observation
     * @param unit
     * @param description  A description of the observation
     * @param dateObserved
     */
    public CholesterolObservation(BigDecimal value, String unit, String description, Date dateObserved) {
        super(value, unit, description, dateObserved, ObservationType.CHOLESTEROL);
    }

    @Override
    public BigDecimal getValue() {
        // Rounds the cholesterol measurement
        return super.getValue().setScale(0, RoundingMode.CEILING);
    }
}
