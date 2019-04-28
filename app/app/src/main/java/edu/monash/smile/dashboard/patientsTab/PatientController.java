package edu.monash.smile.dashboard.patientsTab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.monash.smile.dashboard.DashboardActivity;
import edu.monash.smile.data.HealthService;
import edu.monash.smile.data.HealthServiceProducer;
import edu.monash.smile.data.safeheartsModel.ShPatient;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;
import edu.monash.smile.observerPattern.Subject;

class PatientController extends Subject{
    private Set<ShPatientReference> shPatientReferences;
    private HashMap<ShPatientReference, ShPatient> shPatients;
    private HealthService healthService;

    PatientController() {
        this.healthService = HealthServiceProducer.getService(DashboardActivity.HEALTH_SERVICE_TYPE);
        this.shPatientReferences = new HashSet<>();
    }

    /**
     * Loads data about the practitioner.
     *
     * Whenever the practitioner changes, this method should be called.
     *
     * This method should not be run on the UI thread, as it will block all interactions.
     *
     * @param practitionerId The ID of the practitioner
     */
    void setUp(int practitionerId) {
        fetchPatientsFromService(practitionerId);
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

    List<ShPatient> getShPatients() {
        return new ArrayList<>(shPatients.values());
    }
}
