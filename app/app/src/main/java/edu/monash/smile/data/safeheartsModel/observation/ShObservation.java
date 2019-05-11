package edu.monash.smile.data.safeheartsModel.observation;

import java.util.Date;
import java.util.Objects;

public abstract class ShObservation {
    final private String description;
    final private Date dateObserved;
    final private ObservationType observationType;

    /**
     * A single observation of a patient.
     * @param description A description of the observation
     */
    ShObservation(String description, Date dateObserved, ObservationType observationType) {
        this.description = description;
        this.dateObserved = dateObserved;
        this.observationType = observationType;
    }

    public Date getDateObserved() {
        return dateObserved;
    }

    public String getDescription() {
        return description;
    }

    public ObservationType getObservationType() {
        return observationType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, dateObserved);
    }
}
