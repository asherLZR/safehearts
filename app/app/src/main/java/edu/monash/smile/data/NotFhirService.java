package edu.monash.smile.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.monash.smile.data.safeheartsModel.ShPatient;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;
import edu.monash.smile.data.safeheartsModel.observation.CholesterolObservation;
import edu.monash.smile.data.safeheartsModel.observation.ObservationType;
import edu.monash.smile.data.safeheartsModel.observation.ShObservation;
import edu.monash.smile.data.safeheartsModel.observation.SmokingObservation;
import edu.monash.smile.data.sqlProvider.PatientFileProvider;
import edu.monash.smile.data.sqlProvider.SchemePatientFile;

/**
 * A HealthService that pulls data from a pre-filled SQLite database in the app, for demonstration
 * purposes. Some of the data here like CholesterolObservation measurement unit are hardcoded.
 * */
public class NotFhirService extends HealthService {
    private Context context;

    NotFhirService(Context context) {
        super(HealthServiceType.SQL_NOT_FHIR);
        this.context = context;
    }

    /**
     * Loads all the patient IDs that a practitioner is responsible for.
     *
     * @param practitionerId The practitioner of interest
     * @return A set of unique patient IDs that the practitioner has seen
     */
    @Override
    public Set<ShPatientReference> loadPatientReferences(int practitionerId) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(
                SchemePatientFile.PatientFile.CONTENT_URI,
                null,
                SchemePatientFile.PatientFile.CLINICIAN_ID + " = ?",
                new String[] {String.valueOf(practitionerId)},
                null);
        Set<ShPatientReference> references = new HashSet<>();
        assert cursor != null;
        try{
            while (cursor.moveToNext()){
                int patientId = cursor.getInt(cursor.getColumnIndex(SchemePatientFile.PatientFile.PATIENT_ID));
                references.add(new ShPatientReference(patientId+"", this.healthServiceType));
            }
        }finally {
            cursor.close();
        }
        return references;
    }

    /**
     * Reads any observation of a particular patient, specified by type.
     *
     * @param observationType The type of the observation to be read
     * @param reference The ID of the patient
     * @return A list with all observations for the given type
     */
    @Override
    public List<? extends ShObservation> readObservationsByType(ObservationType observationType, ShPatientReference reference) {
        switch (observationType){
            case CHOLESTEROL:
                return readCholesterol(reference);
            case SMOKING:
                return readSmokingStatus(reference);
            default:
                return new ArrayList<>();
        }
    }

    /**
     * Returns a cursor for the patient with the given id.
     * @param reference The ID of the patient
     * @return A cursor to iterate through.
     */
    private Cursor getCursorByPatientId(ShPatientReference reference){
        ContentResolver resolver = context.getContentResolver();
        String patientId = reference.getId();
        return resolver.query(
                SchemePatientFile.PatientFile.CONTENT_URI,
                null,
                SchemePatientFile.PatientFile.PATIENT_ID + " = ?",
                new String[] {patientId},
                null);
    }

    /**
     * Reads only the latest historical observations for cholesterol.
     * @param reference The ID of the patient
     * @return A list with all observations for the given type
     */
    private List<CholesterolObservation> readCholesterol(ShPatientReference reference){
        List<CholesterolObservation> results = new ArrayList<>();
        Cursor cursor = getCursorByPatientId(reference);
        assert cursor != null;
        try{
            while (cursor.moveToNext()){
                int cholesterolValue = cursor.getInt(cursor.getColumnIndex(SchemePatientFile.PatientFile.CHOLESTEROL));
                results.add(new CholesterolObservation(
                        BigDecimal.valueOf(cholesterolValue),
                        "mg/dl",
                        "Total Cholesterol",
                        null)
                );
            }
        }finally {
            cursor.close();
        }
        return results;
    }

    /**
     * Reads only the latest historical observations for smoking for a patient
     *
     * @param reference The ID of the patient
     * @return A list with all observations for the given type
     */
    private List<SmokingObservation> readSmokingStatus(ShPatientReference reference){
        List<SmokingObservation> results = new ArrayList<>();

        Cursor cursor = getCursorByPatientId(reference);
        assert cursor != null;
        try{
            while (cursor.moveToNext()){
                String smokingStatus = cursor.getString(cursor.getColumnIndex(SchemePatientFile.PatientFile.SMOKING));
                results.add(new SmokingObservation(smokingStatus, null, null));
            }
        }finally {
            cursor.close();
        }
        return results;
    }

    /**
     * Creates a mapping of patient references (IDs) to specific patients.
     * This is used to discover patient details based on their ID.
     * @param references the patient IDs to find the patient details of
     * @return a mapping from the ID to the patient details
     */
    @Override
    public HashMap<ShPatientReference, ShPatient> getAllPatients(Set<ShPatientReference> references) {
        HashMap<ShPatientReference, ShPatient> shPatients = new HashMap<>();
        for (ShPatientReference reference : references) {
            Cursor cursor = getCursorByPatientId(reference);
            assert cursor != null;
            try{
                while (cursor.moveToNext()){
                    String patientName = cursor.getString(cursor.getColumnIndex(SchemePatientFile.PatientFile.PATIENT_NAME));
                    shPatients.put(reference, new ShPatient(reference,patientName));
                }
            }finally {
                cursor.close();
            }
        }
        return shPatients;
    }
}
