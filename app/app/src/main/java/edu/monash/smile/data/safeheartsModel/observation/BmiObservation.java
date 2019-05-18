package edu.monash.smile.data.safeheartsModel.observation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class BmiObservation extends QuantitativeObservation {
    /**
     * Model class for a measurement for BMI.
     */
    public BmiObservation(BigDecimal value, String unit, String description, Date dateObserved) {
        super(value, unit, description, dateObserved);
    }

    @Override
    public BigDecimal getValue() {
        // Rounds the cholesterol measurement
        return super.getValue().setScale(2, RoundingMode.CEILING);
    }
}
