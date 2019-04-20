package edu.monash.smile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import edu.monash.smile.data.FhirService;
import edu.monash.smile.data.HealthService;
import edu.monash.smile.data.model.ObservationType;
import edu.monash.smile.data.model.ObservedPatient;
import edu.monash.smile.data.model.PatientReference;
import edu.monash.smile.data.model.QuantitativeObservation;
import edu.monash.smile.observerPattern.Subject;

class PatientController extends Subject {
    private static final String TAG = "PatientController";
    private Set<PatientReference> patientReferences;
    private HashMap<PatientReference, List<QuantitativeObservation>> observations;
    private HealthService healthService;

    PatientController() {
        this.healthService = new FhirService();
        this.patientReferences = new HashSet<>();
        this.observations = new HashMap<>();
    }

    /**
     * Loads data about the practitioner.
     * Whenever the practitioner changes, this method should be called.
     *
     * @param practitionerId The ID of the practitioner
     */
    void setUp(int practitionerId) {
        // All network operations need to run on a separate thread to avoid blocking the
        // main thread.
        Executors.newSingleThreadExecutor().submit(() -> {
            fetchPatients(practitionerId);
            loadPatientData();
            notifyObservers();
        });
    }

    /**
     * Populates data containing the IDs of the patients of the practitioner.
     *
     * @param practitionerId The ID of the practitioner
     */
    private void fetchPatients(int practitionerId) {
        patientReferences = healthService.loadPatientReferences(practitionerId);
    }

    /**
     * Populates observations of the patients of the practitioner.
     */
    private void loadPatientData() {
        // Remove stale patient data (if any)
        observations.clear();

        // Get data for ALL patients (the assignment only needs data for the selected subset)
        for (PatientReference patient : patientReferences) {
            List<QuantitativeObservation> results = healthService
                    .readPatientQuantitativeObservations(patient, ObservationType.cholesterol);
            // Currently using all results for the patient
            if (results.size() != 0) {
                observations.put(patient, results);
            }
        }
    }

    /**
     * Links each patient to its observations.
     *
     * @return A list of patients with their metrics (e.g. a patient and cholesterol readings)
     */
    List<ObservedPatient> getObservedPatients() {
        List<ObservedPatient> observedPatients = new ArrayList<>();

        for (PatientReference p : observations.keySet()) {
            observedPatients.add(new ObservedPatient(observations.get(p), p));
        }

        return observedPatients;
    }

    List<PatientReference> getPatientReferences() {
        return new ArrayList<>(patientReferences);
    }
}
