package edu.monash.smile.dashboard;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import edu.monash.smile.data.HealthService;
import edu.monash.smile.data.HealthServiceType;
import edu.monash.smile.data.safeheartsModel.ObservationType;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;
import edu.monash.smile.preferences.SharedPreferencesHelper;

public class PatientsMonitor {
    private Map<ObservationType, Set<String>> monitoredPatients = new HashMap<>();
    private Context context;
    private HealthServiceType healthServiceType;

    PatientsMonitor(Context context) {
        this.context = context;
        this.healthServiceType = DashboardActivity.HEALTH_SERVICE_TYPE;
        restoreMonitoredPatientsList();
    }

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

    public boolean isPatientMonitored(String patientId, ObservationType type) {
        if (!monitoredPatients.containsKey(type)) {
            return false;
        }
        Set<String> patientList = monitoredPatients.get(type);
        assert patientList != null;
        return patientList.contains(patientId);
    }

    public void monitorPatient(String patientId, ObservationType type) {
        Set<String> patientList = monitoredPatients.getOrDefault(type, new HashSet<>());
        assert patientList != null;
        patientList.add(patientId);
        monitoredPatients.put(type, patientList);
        saveMonitoredPatientsList();
    }

    public void unmonitorPatient(String patientId, ObservationType type) {
        Set<String> patientList = monitoredPatients.getOrDefault(type, new HashSet<>());
        assert patientList != null;
        patientList.remove(patientId);
        monitoredPatients.put(type, patientList);
        saveMonitoredPatientsList();
    }

    private void saveMonitoredPatientsList() {
        SharedPreferencesHelper.writeMonitoredPatients(context, monitoredPatients);
    }

    private void restoreMonitoredPatientsList() {
        this.monitoredPatients = SharedPreferencesHelper.readMonitoredPatients(context);
    }
}
