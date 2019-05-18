package edu.monash.smile.data.sqlProvider;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

class NotFhirDbHelper extends SQLiteAssetHelper {
    private final static String DB_NAME = "NotFhir.db";
    private final static int DB_VERSION = 1;

    private final static String PATIENT_FILE_TABLE_NAME = SchemePatientFile.PatientFile.TABLE_NAME;

    NotFhirDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
}
