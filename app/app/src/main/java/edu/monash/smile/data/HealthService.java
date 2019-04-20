package edu.monash.smile.data;

import java.util.List;
import java.util.Set;

import edu.monash.smile.data.model.ObservationType;
import edu.monash.smile.data.model.PatientReference;
import edu.monash.smile.data.model.QuantitativeObservation;

public interface HealthService {
    /**
     * Loads all the patient IDs that a practitioner is responsible for.
     *
     * @param practitionerId The practitioner of interest
     * @return A set of unique patient IDs that the practitioner has seen
     */
    Set<PatientReference> loadPatientReferences(Integer practitionerId);

    /**
     * Reads all historical observations for a given type (e.g. cholesterol), for a patient
     *
     * @param patientReference The ID of the patient
     * @param type             The type of the observation
     * @return A list with all observations for the given type
     */
    List<QuantitativeObservation> readPatientQuantitativeObservations(
            PatientReference patientReference,
            ObservationType type
    );
}
