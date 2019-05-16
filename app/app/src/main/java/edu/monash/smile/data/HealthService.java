package edu.monash.smile.data;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import edu.monash.smile.data.safeheartsModel.ShPatient;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;
import edu.monash.smile.data.safeheartsModel.observation.ObservationType;
import edu.monash.smile.data.safeheartsModel.observation.ShObservation;

public abstract class HealthService {
    final HealthServiceType healthServiceType;

    HealthService(HealthServiceType healthServiceType){
        this.healthServiceType = healthServiceType;
    }

    /**
     * Loads all the patient IDs that a practitioner is responsible for.
     *
     * @param practitionerId The practitioner of interest
     * @return A set of unique patient IDs that the practitioner has seen
     */
    public abstract Set<ShPatientReference> loadPatientReferences(int practitionerId);

    /**
     * Reads any observation of a particular patient, specified by type.
     *
     * @param observationType The type of the observation to be read
     * @param reference The ID of the patient
     * @return A list with all observations for the given type
     */
    public abstract List<? extends ShObservation> readObservationsByType(
            ObservationType observationType,
            ShPatientReference reference
    );

    /**
     * Creates a mapping of patient references (IDs) to specific patients.
     * This is used to discover patient details based on their ID.
     * @param references the patient IDs to find the patient details of
     * @return a mapping from the ID to the patient details
     */
    public abstract HashMap<ShPatientReference, ShPatient> getAllPatients(Set<ShPatientReference> references);
}
