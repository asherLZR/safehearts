package edu.monash.smile.data.safeheartsModel;

import java.util.Date;
import java.util.Objects;

public class QuantitativeObservation {
    final private String value;
    final private String unit;
    final private String description;
    final private Date dateObserved;

    /**
     * A single observation of a patient.
     * @param value The metric recorded in the observation
     * @param description A description of the observation
     */
    public QuantitativeObservation(String value, String unit, String description, Date dateObserved) {
        this.value = value;
        this.description = description;
        this.dateObserved = dateObserved;
        this.unit = unit;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public String getUnit() {
        return unit;
    }

    public Date getDateObserved(){
        return dateObserved;
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
        return Objects.hash(value, description);
    }
}
