package edu.monash.smile.data.safeheartsModel.observation;

import java.util.List;

import edu.monash.smile.data.safeheartsModel.ShPatientReference;

public class ObservedPatient<T> {
    private List<T> observations;
    private ShPatientReference shPatientReference;
    private String patientName;

    /**
     * A detailed patient linked with its observations and ID.
     */
    public ObservedPatient(
            List<T> observations,
            ShPatientReference shPatientReference,
            String patientName
    ) {
        this.observations = observations;
        this.shPatientReference = shPatientReference;
        this.patientName = patientName;
    }

    public List<T> getObservations() {
        return observations;
    }

    public ShPatientReference getShPatientReference() {
        return shPatientReference;
    }

    public String getPatientName() {
        return patientName;
    }
}
