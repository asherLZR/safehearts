package edu.monash.smile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import edu.monash.smile.data.FhirService;
import edu.monash.smile.data.model.PatientReference;
import edu.monash.smile.observerPattern.Subject;

class AppController extends Subject {
    private static final String TAG = "AppController";
    private Set<PatientReference> patientReferences;
    private FhirService fhirService;

    AppController() {
        this.fhirService = new FhirService();
        this.patientReferences = new HashSet<>();
    }

    void fetchPatients(int practitionerId) {
        // All network operations need to run on a separate thread to avoid blocking the
        // UI thread.
        Executors.newSingleThreadExecutor().submit(() -> {
            patientReferences = fhirService.loadPatientReferences(practitionerId);
            notifyObservers();
        });
    }

    List<PatientReference> getPatientReferences() {
        return new ArrayList<>(patientReferences);
    }
}
