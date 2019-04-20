package edu.monash.smile.data;

import java.util.Map;
import java.util.Set;

import edu.monash.smile.data.model.ObservationType;
import edu.monash.smile.data.model.PatientReference;

public interface HealthService {
    Set<PatientReference> loadPatientReferences(Integer practionerId);

    Map<PatientReference, Float> readMostRecentPatientObservation(
            PatientReference patientReference,
            ObservationType type
    );
}
