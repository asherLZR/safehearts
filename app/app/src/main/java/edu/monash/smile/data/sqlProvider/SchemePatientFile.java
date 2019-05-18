package edu.monash.smile.data.sqlProvider;

import android.net.Uri;

public class SchemePatientFile {
    static final String CONTENT_AUTHORITY = "edu.monash.smile.data.sqlProvider";

    //Content URIs will use the following as their base
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class PatientFile {
        static final String PATH_VERSION = "PATIENT_FILE";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_VERSION).build();

        static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/edu.monash.smile.data.sqlProvider";
        // Use MIME type prefix android.cursor.item/ for returning a single item //*not used in this app
        static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/edu.monash.smile.data.sqlProvider";

        //Table name
        static final String TABLE_NAME = "PATIENT_FILE";

        //Table Column names
        static final String ID = "_id"; //CursorAdapters will not work if this column with this name is not present
        public static final String CLINICIAN_ID = "CLINICIAN_ID";
        public static final String PATIENT_ID = "PATIENT_ID";
        public static final String PATIENT_NAME = "PATIENT_NAME";
        public static final String SMOKING =  "SMOKING";
        public static final String CHOLESTEROL = "CHOLESTEROL";

        // To prevent someone from accidentally instantiating the contract class, make the constructor private.
        private PatientFile(){}
    }
}
