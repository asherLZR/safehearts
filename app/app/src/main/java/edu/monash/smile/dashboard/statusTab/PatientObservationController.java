package edu.monash.smile.dashboard.statusTab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import edu.monash.smile.dashboard.DashboardActivity;
import edu.monash.smile.dashboard.PatientsMonitor;
import edu.monash.smile.data.HealthService;
import edu.monash.smile.data.HealthServiceProducer;
import edu.monash.smile.data.safeheartsModel.observation.ObservationType;
import edu.monash.smile.data.safeheartsModel.observation.ObservedPatient;
import edu.monash.smile.data.safeheartsModel.observation.CholesterolObservation;
import edu.monash.smile.data.safeheartsModel.ShPatient;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;
import edu.monash.smile.observerPattern.Subject;

class PatientObservationController extends Subject {
    private HashMap<ShPatientReference, List<CholesterolObservation>> observations;
    private HealthService healthService;
    private PatientsMonitor patientsMonitor;
    private HashMap<ShPatientReference, ShPatient> shPatients;

    PatientObservationController(PatientsMonitor patientsMonitor) {
        this.patientsMonitor = patientsMonitor;
        this.healthService = HealthServiceProducer.getService(DashboardActivity.HEALTH_SERVICE_TYPE);
        this.observations = new HashMap<>();
    }

    /**
     * Loads data about the patients and notifies observers when loading is complete.
     *
     * Network operations need to run on a separate thread to avoid blocking the main thread.
     */
    void setUp() {
        loadPatientData();
        notifyObservers();
    }

    /**
     * Populates observations of the patients of the practitioner.
     */
    private void loadPatientData() {
        // Remove stale patient data (if any)
        observations.clear();

        // Get data for ALL patients (the assignment only needs data for the selected subset)
        for (
                ShPatientReference patient :
                patientsMonitor.getMonitoredPatientsByType(ObservationType.CHOLESTEROL)
        ) {
            List<CholesterolObservation> results = healthService.readCholesterol(patient);
            // Currently using all results for the patient
            if (results.size() != 0) {
                observations.put(patient, results);
            }
        }

        this.shPatients = healthService.getAllPatients(
                this.patientsMonitor.getMonitoredPatientsByType(ObservationType.CHOLESTEROL));
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
}
