package edu.monash.smile.data.safeheartsModel;

import java.util.List;

public class ObservedPatient {
    private List<QuantitativeObservation> observations;
    private ShPatientReference shPatientReference;
    private String patientName;

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

    public void setObservations(List<QuantitativeObservation> observations) {
        this.observations = observations;
    }

    public ShPatientReference getShPatientReference() {
        return shPatientReference;
    }

    public void setShPatientReference(ShPatientReference shPatientReference) {
        this.shPatientReference = shPatientReference;
    }

    public String getPatientName() {
        return patientName;
    }
}
