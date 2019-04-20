package edu.monash.smile.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    private static final String TAG = "SharedPreferencesHelper";
    private static final String SMILE_PREFERENCES = "smilePreferences";
    private static final String MONITORED_PATIENTS_KEY = "monitoredPatients";

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

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SMILE_PREFERENCES, Context.MODE_PRIVATE);
    }
}
