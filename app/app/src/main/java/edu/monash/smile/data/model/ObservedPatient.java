package edu.monash.smile.data.model;

import java.util.List;

public class ObservedPatient {
    private List<QuantitativeObservation> observations;
    private PatientReference patientReference;

    public ObservedPatient(List<QuantitativeObservation> observations, PatientReference patientReference) {
        this.observations = observations;
        this.patientReference = patientReference;
    }

    public List<QuantitativeObservation> getObservations() {
        return observations;
    }

    public void setObservations(List<QuantitativeObservation> observations) {
        this.observations = observations;
    }

    public PatientReference getPatientReference() {
        return patientReference;
    }

    public void setPatientReference(PatientReference patientReference) {
        this.patientReference = patientReference;
    }
}
