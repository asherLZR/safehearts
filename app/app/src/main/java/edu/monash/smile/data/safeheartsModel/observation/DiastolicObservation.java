package edu.monash.smile.data.safeheartsModel.observation;

import java.math.BigDecimal;
import java.util.Date;

public class DiastolicObservation extends QuantitativeObservation {
    public DiastolicObservation(BigDecimal value, String unit, String description, Date dateObserved) {
        super(value, unit, description, dateObserved);
    }
}
