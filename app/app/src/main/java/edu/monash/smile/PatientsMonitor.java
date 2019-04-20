package edu.monash.smile;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.monash.smile.data.model.ObservationType;
import edu.monash.smile.data.model.PatientReference;
import edu.monash.smile.preferences.SharedPreferencesHelper;

public class PatientsMonitor {
    private Map<ObservationType, Set<String>> monitoredPatients = new HashMap<>();
    private Context context;

    PatientsMonitor(Context context) {
        this.context = context;
        restoreMonitoredPatientsList();
    }

    public Set<PatientReference> getMonitoredPatientsByType(ObservationType type) {
        Set<PatientReference> results = new HashSet<>();

        if (!monitoredPatients.containsKey(type)) {
            return results;
        }

        for (String patientId : monitoredPatients.get(type)) {
            results.add(new PatientReference(patientId));
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
        Gson gson = new Gson();
        String json = gson.toJson(monitoredPatients);
        SharedPreferencesHelper.writeMonitoredPatients(context, json);
    }

    private void restoreMonitoredPatientsList() {
        Gson gson = new Gson();
        String json = SharedPreferencesHelper.readMonitoredPatients(context);
        this.monitoredPatients = gson.fromJson(
                json,
                new TypeToken<HashMap<ObservationType, Set<String>>>() {
                }.getType()
        );
    }
}
