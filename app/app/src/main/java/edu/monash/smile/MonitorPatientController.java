package edu.monash.smile;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.monash.smile.data.model.ObservationType;

public class MonitorPatientController {
    private static final String MONITORED_PATIENTS_PREFERENCE_KEY = "monitoredPatients";
    private Map<ObservationType, List<String>> monitoredPatients = new HashMap<>();
    private SharedPreferences preferences;

    MonitorPatientController(SharedPreferences preferences) {
        this.preferences = preferences;
        restoreMonitoredPatientsList();
    }

    public void monitorPatient(String patientId, ObservationType type) {
        List<String> patientList = monitoredPatients.getOrDefault(type, new ArrayList<>());
        assert patientList != null;
        patientList.add(patientId);
        monitoredPatients.put(type, patientList);
        saveMonitoredPatientsList();
    }

    public void unmonitorPatient(String patientId, ObservationType type) {
        List<String> patientList = monitoredPatients.getOrDefault(type, new ArrayList<>());
        assert patientList != null;
        patientList.remove(patientId);
        monitoredPatients.put(type, patientList);
        saveMonitoredPatientsList();
    }

    private void saveMonitoredPatientsList() {
        Gson gson = new Gson();
        String json = gson.toJson(monitoredPatients);
        preferences.edit().putString(MONITORED_PATIENTS_PREFERENCE_KEY, json).apply();
    }

    private void restoreMonitoredPatientsList() {
        if (preferences.contains(MONITORED_PATIENTS_PREFERENCE_KEY)) {
            Gson gson = new Gson();
            String json = preferences.getString(MONITORED_PATIENTS_PREFERENCE_KEY, "{}");
            this.monitoredPatients = gson.fromJson(
                    json,
                    new TypeToken<HashMap<ObservationType, List<String>>>() {
                    }.getType()
            );
        }
    }
}
