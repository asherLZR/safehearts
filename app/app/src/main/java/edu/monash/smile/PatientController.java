package edu.monash.smile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import edu.monash.smile.data.FhirService;
import edu.monash.smile.data.HealthService;
import edu.monash.smile.data.model.PatientReference;
import edu.monash.smile.observerPattern.Subject;

class PatientController extends Subject {
    private static final String TAG = "PatientController";
    private Set<PatientReference> patientReferences;
    private HealthService healthService;

    PatientController() {
        this.healthService = new FhirService();
        this.patientReferences = new HashSet<>();
    }

    void fetchPatients(int practitionerId) {
        // All network operations need to run on a separate thread to avoid blocking the
        // UI thread.
        Executors.newSingleThreadExecutor().submit(() -> {
            patientReferences = healthService.loadPatientReferences(practitionerId);
            notifyObservers();
        });
    }

    List<PatientReference> getPatientReferences() {
        return new ArrayList<>(patientReferences);
    }
}
