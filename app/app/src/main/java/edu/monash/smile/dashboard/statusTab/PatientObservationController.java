package edu.monash.smile.dashboard.statusTab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

import edu.monash.smile.dashboard.PatientsMonitor;
import edu.monash.smile.data.FhirService;
import edu.monash.smile.data.HealthService;
import edu.monash.smile.data.model.ObservationType;
import edu.monash.smile.data.model.ObservedPatient;
import edu.monash.smile.data.model.PatientReference;
import edu.monash.smile.data.model.QuantitativeObservation;
import edu.monash.smile.observerPattern.Subject;

class PatientObservationController extends Subject {
    private HashMap<PatientReference, List<QuantitativeObservation>> observations;
    private HealthService healthService;
    private PatientsMonitor patientsMonitor;

    PatientObservationController(PatientsMonitor patientsMonitor) {
        this.patientsMonitor = patientsMonitor;
        this.healthService = new FhirService();
        this.observations = new HashMap<>();
    }

    void setUp() {
        // All network operations need to run on a separate thread to avoid blocking the
        // main thread.
        Executors.newSingleThreadExecutor().submit(() -> {
            loadPatientData();
            notifyObservers();
        });
    }

    /**
     * Populates observations of the patients of the practitioner.
     */
    void loadPatientData() {
        // Remove stale patient data (if any)
        observations.clear();

        // Get data for ALL patients (the assignment only needs data for the selected subset)
        for (
                PatientReference patient :
                patientsMonitor.getMonitoredPatientsByType(ObservationType.cholesterol)
        ) {
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
}
