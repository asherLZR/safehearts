package edu.monash.smile.data.safeheartsModel;

import java.util.Objects;

public class QuantitativeObservation {
    final private String value;
    final private String description;

    public QuantitativeObservation(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
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
