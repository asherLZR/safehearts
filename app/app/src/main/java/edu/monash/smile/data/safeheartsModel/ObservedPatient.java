package edu.monash.smile.data.safeheartsModel;

import java.util.List;

public class ObservedPatient {
    private List<QuantitativeObservation> observations;
    private ShPatientReference shPatientReference;
    private String patientName;

    /**
     * A detailed patient linked with its observations and ID.
     */
    public ObservedPatient(
            List<QuantitativeObservation> observations,
            ShPatientReference shPatientReference,
            String patientName
    ) {
        this.observations = observations;
        this.shPatientReference = shPatientReference;
        this.patientName = patientName;
    }

    public List<QuantitativeObservation> getObservations() {
        return observations;
    }

    public ShPatientReference getShPatientReference() {
        return shPatientReference;
    }

    public String getPatientName() {
        return patientName;
    }
}
