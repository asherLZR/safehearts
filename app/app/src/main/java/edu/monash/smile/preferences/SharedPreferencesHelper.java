package edu.monash.smile.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

import edu.monash.smile.data.safeheartsModel.ShPatient;

public class SharedPreferencesHelper {
    private static final String TAG = "SharedPreferencesHelper";
    private static final String SMILE_PREFERENCES = "smilePreferences";
    private static final String MONITORED_PATIENTS_KEY = "monitoredPatients";
    private static final String ALL_PATIENTS = "allPatients";

    public static void writeMonitoredPatients(Context context, String monitoredPatients) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(MONITORED_PATIENTS_KEY, monitoredPatients);
        editor.commit();
    }

    public static String readMonitoredPatients(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(MONITORED_PATIENTS_KEY, "{}");
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.edit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SMILE_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static void writeAllPatients(Context context, ArrayList<ShPatient> patient){
        Gson gson = new Gson();
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(ALL_PATIENTS, gson.toJson(patient));
        editor.apply();
    }

    public static String readPatient(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(ALL_PATIENTS, "");
    }
}
