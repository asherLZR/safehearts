package edu.monash.smile.data;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import edu.monash.smile.data.safeheartsModel.ObservationType;
import edu.monash.smile.data.safeheartsModel.QuantitativeObservation;
import edu.monash.smile.data.safeheartsModel.ShPatient;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;

public abstract class HealthService {
    HealthServiceType healthServiceType;

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
     * Reads all historical observations for a given type (e.g. CHOLESTEROL), for a patient
     *
     * @param shPatientReference The ID of the patient
     * @param type               The type of the observation
     * @return A list with all observations for the given type
     */
    public abstract List<QuantitativeObservation> readPatientQuantitativeObservations(
            ShPatientReference shPatientReference,
            ObservationType type
    );

    /**
     * Store all patients the practitioner has seen in SharedPreferences
     * @param references The PatientReferences of the Patients to return
     * @return A dictionary of Patient objects identified by their full reference
     */
    public abstract HashMap<ShPatientReference, ShPatient> getAllPatients(Set<ShPatientReference> references);
}
