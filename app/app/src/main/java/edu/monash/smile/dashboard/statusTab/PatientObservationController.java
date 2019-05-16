package edu.monash.smile.dashboard.statusTab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import edu.monash.smile.dashboard.DashboardActivity;
import edu.monash.smile.dashboard.PatientsMonitor;
import edu.monash.smile.data.HealthService;
import edu.monash.smile.data.HealthServiceProducer;
import edu.monash.smile.data.safeheartsModel.ShPatient;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;
import edu.monash.smile.data.safeheartsModel.observation.BloodPressureObservation;
import edu.monash.smile.data.safeheartsModel.observation.CholesterolObservation;
import edu.monash.smile.data.safeheartsModel.observation.ObservationType;
import edu.monash.smile.data.safeheartsModel.observation.ShObservation;
import edu.monash.smile.data.safeheartsModel.observation.SmokingObservation;
import edu.monash.smile.observerPattern.Subject;

class PatientObservationController extends Subject {
    private HealthService healthService;
    private PatientsMonitor patientsMonitor;
    private List<ObservedPatient<? extends ShObservation>> observedPatients = new ArrayList<>();

    PatientObservationController(PatientsMonitor patientsMonitor) {
        this.patientsMonitor = patientsMonitor;
        this.healthService = HealthServiceProducer.getService(DashboardActivity.HEALTH_SERVICE_TYPE);
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
     * Populates cholesterolObservations of the patients of the practitioner.
     */
    private void loadPatientData() {
        // Remove stale patient data (if any)
        this.observedPatients.clear();

        HashMap<ShPatientReference, ShPatient> shPatients = healthService.getAllPatients(
                this.patientsMonitor.getAllMonitoredPatients());

        // Get data for ALL patients (the assignment only needs data for the selected subset)
        for (ObservationType observationType : ObservationType.values()){
            for (
                    ShPatientReference patient :
                    patientsMonitor.getMonitoredPatientsByType(observationType)
            ) {
                List<? extends ShObservation> result = this.healthService.readObservationsByType(
                            observationType,
                            patient
                        );
                if (result.size() != 0) {
                    this.observedPatients.add(ObservedPatientFactory.getObservedPatient(
                            observationType,
                            result,
                            patient,
                            Objects.requireNonNull(shPatients.get(patient)).getName()
                    ));
                }
            }
        }
    }

    List<ObservedPatient<? extends ShObservation>> getAllObservationCollections(){
        return this.observedPatients;
    }
}
