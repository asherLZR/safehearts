package edu.monash.smile.dashboard.patientsTab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.monash.smile.dashboard.DashboardActivity;
import edu.monash.smile.data.HealthService;
import edu.monash.smile.data.HealthServiceProducer;
import edu.monash.smile.data.safeheartsModel.ObservationType;
import edu.monash.smile.data.safeheartsModel.QuantitativeObservation;
import edu.monash.smile.data.safeheartsModel.ShPatient;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;
import edu.monash.smile.observerPattern.Subject;

class PatientController extends Subject{
    private Set<ShPatientReference> shPatientReferences;
    private HashMap<ShPatientReference, ShPatient> shPatients;
    private HealthService healthService;
    //    private HashMap<ShPatientReference, List<QuantitativeObservation>> observations;

    PatientController() {
        this.healthService = HealthServiceProducer.getService(DashboardActivity.HEALTH_SERVICE_TYPE);
        this.shPatientReferences = new HashSet<>();
//        this.observations = new HashMap<>();
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
        fetchPatientsFromService(practitionerId);
//        loadPatientData();
        notifyObservers();
    }

    /**
     * Populates data containing the IDs of the patients of the practitioner and stores an instance
     * of all such ShPatients.
     *
     * @param practitionerId The ID of the practitioner
     */
    private void fetchPatientsFromService(int practitionerId) {
        this.shPatientReferences = healthService.loadPatientReferences(practitionerId);
        this.shPatients = healthService.getAllPatients(this.shPatientReferences);
    }

//    /**
//     * Populates observations of the patients of the practitioner.
//     */
//    private void loadPatientData() {
//        // Remove stale patient data (if any)
//        observations.clear();
//
//        // Get data for ALL patients (the assignment only needs data for the selected subset)
//        for (ShPatientReference patient : shPatientReferences) {
//            List<QuantitativeObservation> results = healthService
//                    .readPatientQuantitativeObservations(patient, ObservationType.CHOLESTEROL);
//            // Currently using all results for the patient
//            if (results.size() != 0) {
//                observations.put(patient, results);
//            }
//        }
//    }

    List<ShPatient> getShPatients() {
        return new ArrayList<>(shPatients.values());
    }

//    /**
//     * Links each patient to its observations.
//     *
//     * @return A list of patients with their metrics (e.g. a patient and CHOLESTEROL readings)
//     */
//    List<ObservedPatient> getObservedPatients() {
//        List<ObservedPatient> observedPatients = new ArrayList<>();
//
//        for (ShPatientReference p : observations.keySet()) {
//            observedPatients.add(new ObservedPatient(
//                    observations.get(p),
//                    p,
//                    Objects.requireNonNull(shPatients.get(p)).getName()));
//        }
//
//        return observedPatients;
//    }
}
