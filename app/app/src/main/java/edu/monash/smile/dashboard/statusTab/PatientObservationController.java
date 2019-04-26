package edu.monash.smile.dashboard.statusTab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.monash.smile.dashboard.PatientsMonitor;
import edu.monash.smile.data.HealthService;
import edu.monash.smile.data.HealthServiceProducer;
import edu.monash.smile.data.HealthServiceUrl;
import edu.monash.smile.data.safeheartsModel.ObservationType;
import edu.monash.smile.data.safeheartsModel.ObservedPatient;
import edu.monash.smile.data.safeheartsModel.QuantitativeObservation;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;
import edu.monash.smile.observerPattern.Subject;

class PatientObservationController extends Subject {
    private HashMap<ShPatientReference, List<QuantitativeObservation>> observations;
    private HealthService healthService;
    private PatientsMonitor patientsMonitor;

    PatientObservationController(PatientsMonitor patientsMonitor) {
        this.patientsMonitor = patientsMonitor;
        this.healthService = HealthServiceProducer.getService(HealthServiceUrl.HealthServiceType.FHIR);
        this.observations = new HashMap<>();
    }

    void setUp() {
        // All network operations need to run on a separate thread to avoid blocking the
        // main thread.
//        Executors.newSingleThreadExecutor().submit(() -> {
        loadPatientData();
        notifyObservers();
//        });
    }

    /**
     * Populates observations of the patients of the practitioner.
     */
    void loadPatientData() {
        // Remove stale patient data (if any)
        observations.clear();

        // Get data for ALL patients (the assignment only needs data for the selected subset)
        for (
                ShPatientReference patient :
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

        for (ShPatientReference p : observations.keySet()) {
            observedPatients.add(new ObservedPatient(observations.get(p), p));
        }

        return observedPatients;
    }
}
