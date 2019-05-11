package edu.monash.smile.data.safeheartsModel.observation;

import java.util.List;

public class ObservationCollection {
    private ObservationType observationType;
    private List<ObservedPatient> observations;

    public ObservationCollection(
            ObservationType observationType,
            List<ObservedPatient> observations
    ){
        this.observationType = observationType;
        this.observations = observations;
    }

    public List<ObservedPatient> getObservations() {
        return observations;
    }

    public ObservationType getObservationType() {
        return observationType;
    }
}
