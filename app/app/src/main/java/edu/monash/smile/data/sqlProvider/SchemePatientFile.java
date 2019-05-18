package edu.monash.smile.data.sqlProvider;

import android.net.Uri;

public class SchemePatientFile {
    public static final String CONTENT_AUTHORITY = "edu.monash.smile.data.sqlProvider";

    //Content URIs will use the following as their base
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final class PatientFile {
        public static final String PATH_VERSION = "PATIENT_FILE";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_VERSION).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/edu.monash.smile.data.sqlProvider";
        // Use MIME type prefix android.cursor.item/ for returning a single item //*not used in this app
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/edu.monash.smile.data.sqlProvider";

        //Table name
        public static final String TABLE_NAME = "PATIENT_FILE";

        //Table Column names
        public static final String ID = "_id"; //CursorAdapters will not work if this column with this name is not present
        public static final String CLINICIAN_ID = "CLINICIAN_ID";
        public static final String PATIENT_ID = "PATIENT_ID";
        public static final String PATIENT_NAME = "PATIENT_NAME";

        // To prevent someone from accidentally instantiating the contract class, make the constructor private.
        private PatientFile(){}

        public static final String[] PROJECTION = new String[]{
                PatientFile.ID,
                PatientFile.CLINICIAN_ID,
                PatientFile.PATIENT_ID,
                PatientFile.PATIENT_NAME
        };
    }
}
