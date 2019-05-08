package edu.monash.smile.dashboard.alertTab;

import java.util.ArrayList;
import java.util.List;

import edu.monash.smile.dashboard.PatientsMonitor;
import edu.monash.smile.data.safeheartsModel.AlertPatient;
import edu.monash.smile.observerPattern.Subject;

public class AlertController extends Subject {
    private PatientsMonitor patientsMonitor;

    AlertController(PatientsMonitor patientsMonitor) {
        this.patientsMonitor = patientsMonitor;
    }

    void setUp() {
        loadPatientData();
        notifyObservers();
    }


    private void loadPatientData() {
        // TODO
    }

    List<AlertPatient> getAlertPatients() {
        // TODO: Find patients with high blood pressure
        return new ArrayList<>();
    }
}
