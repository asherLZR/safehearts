package edu.monash.smile.dashboard.statusTab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import edu.monash.smile.dashboard.PatientsMonitor;
import edu.monash.smile.data.HealthService;
import edu.monash.smile.data.HealthServiceProducer;
import edu.monash.smile.data.HealthServiceType;
import edu.monash.smile.data.safeheartsModel.ObservationType;
import edu.monash.smile.data.safeheartsModel.ObservedPatient;
import edu.monash.smile.data.safeheartsModel.QuantitativeObservation;
import edu.monash.smile.data.safeheartsModel.ShPatient;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;
import edu.monash.smile.observerPattern.Subject;

class PatientObservationController extends Subject {
    private HashMap<ShPatientReference, List<QuantitativeObservation>> observations;
    private HealthService healthService;
    private PatientsMonitor patientsMonitor;
    private HashMap<ShPatientReference, ShPatient> shPatients;

    PatientObservationController(PatientsMonitor patientsMonitor) {
        this.patientsMonitor = patientsMonitor;
        // FIXME: HARDCODED SERVICE!
        this.healthService = HealthServiceProducer.getService(HealthServiceType.FHIR);
        this.observations = new HashMap<>();
    }

    void setUp() {
        // All network operations need to run on a separate thread to avoid blocking the
        // main thread.
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
            List<QuantitativeObservation> results = healthService
                    .readPatientQuantitativeObservations(patient, ObservationType.CHOLESTEROL);
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
