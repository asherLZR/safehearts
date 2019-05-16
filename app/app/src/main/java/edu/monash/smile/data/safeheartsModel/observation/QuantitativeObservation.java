package edu.monash.smile.data.safeheartsModel.observation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public abstract class QuantitativeObservation extends ShObservation {
    final private BigDecimal value;
    final private String unit;
    final private String description;

    /**
     * A single observation of a patient.
     * @param value The metric recorded in the observation
     * @param description A description of the observation
     */
    QuantitativeObservation(BigDecimal value, String unit, String description, Date dateObserved) {
        super(description, dateObserved);
        this.value = value;
        this.description = description;
        this.unit = unit;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuantitativeObservation other = (QuantitativeObservation) o;
        return Objects.equals(value, other.value) &&
                Objects.equals(description, other.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value, unit);
    }
}
