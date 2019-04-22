package edu.monash.smile.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.monash.smile.data.safeheartsModel.ObservationType;
import edu.monash.smile.data.safeheartsModel.ShPatient;

public class SharedPreferencesHelper {
    private static final String TAG = "SharedPreferencesHelper";
    private static final String SMILE_PREFERENCES = "smilePreferences";
    private static final String MONITORED_PATIENTS_KEY = "monitoredPatients";
    private static final String ALL_PATIENTS = "allPatients";

    public static void writeMonitoredPatients(Context context, Map<ObservationType, Set<String>> monitoredPatients) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(MONITORED_PATIENTS_KEY, new Gson().toJson(monitoredPatients));
        editor.commit();
    }

    public static HashMap<ObservationType, Set<String>> readMonitoredPatients(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        String serializedString = preferences.getString(MONITORED_PATIENTS_KEY, "{}");
        return new Gson().fromJson(
                serializedString,
                new TypeToken<HashMap<ObservationType, Set<String>>>() {
                }.getType()
        );
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.edit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SMILE_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static void removeAllSharedPreferences(Context context){
        getEditor(context).clear().commit();
    }

    public static void writeAllPatients(Context context, ArrayList<ShPatient> patient){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(ALL_PATIENTS, new Gson().toJson(new Gson().toJson(patient)));
        editor.apply();
    }
}
