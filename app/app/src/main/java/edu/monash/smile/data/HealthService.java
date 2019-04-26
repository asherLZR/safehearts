package edu.monash.smile.data;

import android.content.Context;

import java.util.List;
import java.util.Set;

import edu.monash.smile.data.safeheartsModel.ObservationType;
import edu.monash.smile.data.safeheartsModel.QuantitativeObservation;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;

public abstract class HealthService {
    private HealthServiceType tag;

    HealthService(HealthServiceType tag){
        this.tag = tag;
    }

    /**
     * Loads all the patient IDs that a practitioner is responsible for.
     *
     * @param practitionerId The practitioner of interest
     * @return A set of unique patient IDs that the practitioner has seen
     */
    public abstract Set<ShPatientReference> loadPatientReferences(Context context, Integer practitionerId);

    /**
     * Reads all historical observations for a given type (e.g. cholesterol), for a patient
     *
     * @param shPatientReference The ID of the patient
     * @param type               The type of the observation
     * @return A list with all observations for the given type
     */
    public abstract List<QuantitativeObservation> readPatientQuantitativeObservations(
            ShPatientReference shPatientReference,
            ObservationType type
    );
}
