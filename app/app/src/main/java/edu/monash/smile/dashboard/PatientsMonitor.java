package edu.monash.smile.dashboard;

import android.content.Context;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import edu.monash.smile.data.HealthServiceType;
import edu.monash.smile.data.safeheartsModel.ObservationType;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;
import edu.monash.smile.preferences.SharedPreferencesHelper;

public class PatientsMonitor {
    private Map<ObservationType, Set<String>> monitoredPatients = new HashMap<>();
    private Context context;
    private HealthServiceType healthServiceType;

    /**
     * Creates a controller for the tracking of patients by a specific observation type.
     * @param context the Android context used for persistence
     */
    PatientsMonitor(Context context) {
        this.context = context;
        this.healthServiceType = DashboardActivity.HEALTH_SERVICE_TYPE;
        restoreMonitoredPatientsList();
    }

    /**
     * Finds the references of all patients who are currently being monitored for a specific type.
     * @param type the observation type
     * @return patient references of those monitored for the type
     */
    public Set<ShPatientReference> getMonitoredPatientsByType(ObservationType type) {
        Set<ShPatientReference> results = new HashSet<>();

        if (!monitoredPatients.containsKey(type)) {
            return results;
        }

        for (String patientId : Objects.requireNonNull(monitoredPatients.get(type))) {
            results.add(new ShPatientReference(patientId, this.healthServiceType));
        }

        return results;
    }

    /**
     * Finds whether a patient is being monitored for a given observation type.
     * @param patientId the patient id of interest
     * @param type the type of interest
     * @return true, when the patient is monitored for a specific type, and false otherwise.
     */
    public boolean isPatientMonitored(String patientId, ObservationType type) {
        if (!monitoredPatients.containsKey(type)) {
            return false;
        }
        Set<String> patientList = monitoredPatients.get(type);
        assert patientList != null;
        return patientList.contains(patientId);
    }

    /**
     * Tracks a patient for a specific observation type.
     * @param patientId the patient ID to track
     * @param type the observation type to track the patient on
     */
    public void monitorPatient(String patientId, ObservationType type) {
        Set<String> patientList = monitoredPatients.getOrDefault(type, new HashSet<>());
        assert patientList != null;
        patientList.add(patientId);
        monitoredPatients.put(type, patientList);
        saveMonitoredPatientsList();
    }

    /**
     * Untracks a patient for a specific observation type.
     * @param patientId the patient ID to untrack
     * @param type the observation type to untrack the patient on
     */
    public void unmonitorPatient(String patientId, ObservationType type) {
        Set<String> patientList = monitoredPatients.getOrDefault(type, new HashSet<>());
        assert patientList != null;
        patientList.remove(patientId);
        monitoredPatients.put(type, patientList);
        saveMonitoredPatientsList();
    }

    /**
     * Persists the monitored patients to Android.
     */
    private void saveMonitoredPatientsList() {
        SharedPreferencesHelper.writeMonitoredPatients(context, monitoredPatients);
    }

    /**
     * Loads existing monitored patients from Android.
     */
    private void restoreMonitoredPatientsList() {
        this.monitoredPatients = SharedPreferencesHelper.readMonitoredPatients(context);
    }
}
