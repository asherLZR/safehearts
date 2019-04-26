package edu.monash.smile.dashboard.patientsTab;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import edu.monash.smile.data.HealthService;
import edu.monash.smile.data.HealthServiceProducer;
import edu.monash.smile.data.HealthServiceType;
import edu.monash.smile.data.safeheartsModel.ObservationType;
import edu.monash.smile.data.safeheartsModel.ObservedPatient;
import edu.monash.smile.data.safeheartsModel.QuantitativeObservation;
import edu.monash.smile.data.safeheartsModel.ShPatient;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;
import edu.monash.smile.observerPattern.Subject;

class PatientController extends Subject {
    private static final String TAG = "PatientController";
    private Set<ShPatientReference> shPatientReferences;
    private HashMap<ShPatientReference, ShPatient> shPatients;
    private HashMap<ShPatientReference, List<QuantitativeObservation>> observations;
    private HealthService healthService;

    PatientController() {
        // FIXME: HARDCODED SERVICE!
        this.healthService = HealthServiceProducer.getService(HealthServiceType.FHIR);
        this.shPatientReferences = new HashSet<>();
        this.observations = new HashMap<>();
    }

    /**
     * Loads data about the practitioner.
     * Whenever the practitioner changes, this method should be called.
     *
     * @param practitionerId The ID of the practitioner
     */
    void setUp(Context context, int practitionerId) {
        // All network operations need to run on a separate thread to avoid blocking the
        // main thread.
        fetchPatients(context, practitionerId);
        loadPatientData();
        notifyObservers();
    }

    /**
     * Populates data containing the IDs of the patients of the practitioner.
     *
     * @param practitionerId The ID of the practitioner
     */
    private void fetchPatients(Context context, int practitionerId) {
        this.shPatientReferences = healthService.loadPatientReferences(context, practitionerId);
        this.shPatients = healthService.getAllPatients(this.shPatientReferences);
    }

    /**
     * Populates observations of the patients of the practitioner.
     */
    private void loadPatientData() {
        // Remove stale patient data (if any)
        observations.clear();

        // Get data for ALL patients (the assignment only needs data for the selected subset)
        for (ShPatientReference patient : shPatientReferences) {
            List<QuantitativeObservation> results = healthService
                    .readPatientQuantitativeObservations(patient, ObservationType.CHOLESTEROL);
            // Currently using all results for the patient
            if (results.size() != 0) {
                observations.put(patient, results);
            }
        }
    }

    /**
     * Links each patient to its observations.
     *
     * @return A list of patients with their metrics (e.g. a patient and CHOLESTEROL readings)
     */
    List<ObservedPatient> getObservedPatients() {
        List<ObservedPatient> observedPatients = new ArrayList<>();

        for (ShPatientReference p : observations.keySet()) {
            observedPatients.add(new ObservedPatient(
                    observations.get(p),
                    p,
                    Objects.requireNonNull(shPatients.get(p)).getName()));
        }

        return observedPatients;
    }

    List<ShPatientReference> getShPatientReferences() {
        return new ArrayList<>(shPatientReferences);
    }

    List<ShPatient> getShPatients() {
        return new ArrayList<>(shPatients.values());
    }
}
