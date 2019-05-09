package edu.monash.smile.data.safeheartsModel.observation;

import java.util.Date;
import java.util.Objects;

abstract class Observation {
    final private String description;
    final private Date dateObserved;

    /**
     * A single observation of a patient.
     * @param description A description of the observation
     */
    Observation(String description, Date dateObserved) {
        this.description = description;
        this.dateObserved = dateObserved;
    }

    public Date getDateObserved() {
        return dateObserved;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, dateObserved);
    }
}
