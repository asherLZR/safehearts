package edu.monash.smile.data.sqlProvider;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Helper for pre-filled SQLite database.
 */
class NotFhirDbHelper extends SQLiteAssetHelper {
    private final static String DB_NAME = "NotFhir.db";
    private final static int DB_VERSION = 1;

    NotFhirDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
}
