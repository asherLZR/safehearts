package edu.monash.smile.data.safeheartsModel.observation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class CholesterolObservation extends QuantitativeObservation {
    /**
     * Model class for a cholesterol measurement.
     */
    public CholesterolObservation(BigDecimal value, String unit, String description, Date dateObserved) {
        super(value, unit, description, dateObserved);
    }

    @Override
    public BigDecimal getValue() {
        // Rounds the cholesterol measurement
        return super.getValue().setScale(0, RoundingMode.CEILING);
    }
}
