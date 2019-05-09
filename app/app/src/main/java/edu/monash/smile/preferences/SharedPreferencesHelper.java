package edu.monash.smile.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.monash.smile.data.safeheartsModel.observation.ObservationType;
import edu.monash.smile.data.safeheartsModel.ShPatient;

public class SharedPreferencesHelper {
    /**
     * Private keys which tell Android where to store the specific preferences.
     */
    private static final String SMILE_PREFERENCES = "smilePreferences";
    private static final String MONITORED_PATIENTS_KEY = "monitoredPatients";
    private static final String ALL_PATIENTS_KEY = "allPatients";

    /**
     * Creates the SharedPreferences for the app.
     * Since the preferences are private, the preferences are only accessible within the app.
     *
     * @param context the Android context
     * @return Android's SharedPreferences object
     */
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SMILE_PREFERENCES, Context.MODE_PRIVATE);
    }

    /**
     * Puts the SharedPreferences into editing mode.
     * @param context the Android context
     * @return SharedPreferences editor, which allows mutations to SharedPreferences
     */
    private static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.edit();
    }

    /**
     * Persist the monitored patients.
     * @param context the Android context
     * @param monitoredPatients A mapping of observation types (e.g. cholestrol) to the patients tracked for that given type.
     */
    public static void writeMonitoredPatients(Context context, Map<ObservationType, Set<String>> monitoredPatients) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(MONITORED_PATIENTS_KEY, new Gson().toJson(monitoredPatients));
        editor.commit();
    }

    /**
     * Restores the monitored patients.
     * @param context the Android context
     * @return the persisted monitored patients
     */
    public static HashMap<ObservationType, Set<String>> readMonitoredPatients(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        String serializedString = preferences.getString(MONITORED_PATIENTS_KEY, "{}");
        return new Gson().fromJson(
                serializedString,
                new TypeToken<HashMap<ObservationType, Set<String>>>() {
                }.getType()
        );
    }

    /**
     * Deletes all SharedPreferences.
     * @param context the Android context
     */
    public static void removeAllSharedPreferences(Context context){
        getEditor(context).clear().commit();
    }

    // TODO: Method unused
    public static void writeAllPatients(Context context, HashMap<String, ShPatient> patients){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(ALL_PATIENTS_KEY, new Gson().toJson(patients));
        editor.apply();
    }

    // TODO: Method unused
    public static HashMap<String, ShPatient> readAllPatients(Context context){
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return new Gson().fromJson(sharedPreferences.getString(ALL_PATIENTS_KEY, ""),
                new TypeToken<HashMap<String, ShPatient>>(){}.getType());
    }
}
