package edu.monash.smile.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.monash.smile.data.safeheartsModel.ShPatient;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;
import edu.monash.smile.data.safeheartsModel.observation.ObservationType;
import edu.monash.smile.data.safeheartsModel.observation.ShObservation;
import edu.monash.smile.data.sqlProvider.SchemePatientFile;

public class NotFhirService extends HealthService {
    private Context context;

    public NotFhirService(Context context) {
        super(HealthServiceType.SQL_NOT_FHIR);
        this.context = context;
    }

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

    @Override
    public List<? extends ShObservation> readObservationsByType(ObservationType observationType, ShPatientReference reference) {
        return new ArrayList<>();
    }

    @Override
    public HashMap<ShPatientReference, ShPatient> getAllPatients(Set<ShPatientReference> references) {
        HashMap<ShPatientReference, ShPatient> shPatients = new HashMap<>();
        ContentResolver resolver = context.getContentResolver();
        for (ShPatientReference reference : references) {
            String patientId = reference.getId();
            Cursor cursor = resolver.query(
                    SchemePatientFile.PatientFile.CONTENT_URI,
                    null,
                    SchemePatientFile.PatientFile.PATIENT_ID + " = ?",
                    new String[] {patientId},
                    null);
            assert cursor != null;
            try{
                while (cursor.moveToNext()){
                    String patientName = cursor.getString(cursor.getColumnIndex(SchemePatientFile.PatientFile.PATIENT_NAME));
                    references.add(new ShPatientReference(patientId+"", this.healthServiceType));
                    shPatients.put(reference, new ShPatient(reference,patientName));
                }
            }finally {
                cursor.close();
            }
        }
        return shPatients;
    }
}
