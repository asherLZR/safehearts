package edu.monash.smile.dashboard.statusTab;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import edu.monash.smile.dashboard.DashboardActivity;
import edu.monash.smile.dashboard.PatientsMonitor;
import edu.monash.smile.dashboard.statusTab.observedPatient.ObservedPatient;
import edu.monash.smile.dashboard.statusTab.observedPatient.ObservedPatientFactory;
import edu.monash.smile.data.HealthService;
import edu.monash.smile.data.HealthServiceProducer;
import edu.monash.smile.data.safeheartsModel.ShPatient;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;
import edu.monash.smile.data.safeheartsModel.observation.ObservationType;
import edu.monash.smile.data.safeheartsModel.observation.ShObservation;
import edu.monash.smile.observerPattern.Subject;

class PatientObservationController extends Subject {
    private final HealthService healthService;
    private final PatientsMonitor patientsMonitor;
    private final List<ObservedPatient<? extends ShObservation>> observedPatients = new ArrayList<>();

    PatientObservationController(PatientsMonitor patientsMonitor, Context context) {
        this.patientsMonitor = patientsMonitor;
        this.healthService = HealthServiceProducer.getService(DashboardActivity.healthServiceType, context);
    }

    /**
     * Loads data about the patients, then notifies observers when loading is complete.
     *
     * When using this method, keep in mind that network operations need to run on a separate
     * thread to avoid blocking the main thread. Use AsyncTask to run this method in a background
     * thread.
     */
    void setUp() {
        loadPatientData();
        notifyObservers();
    }

    /**
     * Populates observations of the patients of the practitioner.
     */
    private void loadPatientData() {
        // Remove stale patient data (if any).
        this.observedPatients.clear();

        HashMap<ShPatientReference, ShPatient> shPatients = healthService.getAllPatients(
                this.patientsMonitor.getAllMonitoredPatients());

        // For each observation type, we fetch the latest data.
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
