package edu.monash.smile.data.sqlProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PatientFileProvider extends ContentProvider {
    private NotFhirDbHelper notFhirDbHelper;
    private static final UriMatcher uriMatcher = createUriMatcher();
    private static final int PATIENT_FILE = 100;
    private static final int PATIENT_FILE_ID = 200;

    /**
     * Creates a UriMatcher to map URIs to their respective DB object type.
     * @return The UriMatcher for the database
     */
    private static UriMatcher createUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SchemePatientFile.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, SchemePatientFile.PatientFile.PATH_VERSION, PATIENT_FILE);
        uriMatcher.addURI(authority, SchemePatientFile.PatientFile.PATH_VERSION + "/#", PATIENT_FILE_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        this.notFhirDbHelper = new NotFhirDbHelper(getContext());
        return true;
    }

    /**
     * Reads only the latest historical observations for cholesterol.
     * @param uri URI for the data
     * @param projection SQL projection (column selection) parameters
     * @param selection SQL selection (row selection) query
     * @param selectionArgs the arguments passed to the selection query
     * @param sortOrder the order by which to sort the resultant rows
     * @return The cursor for the query result
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Use SQLiteQueryBuilder for querying db
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(SchemePatientFile.PatientFile.TABLE_NAME);

        // Record id
        String id;

        // Match Uri pattern
        int uriType = uriMatcher.match(uri);

        switch (uriType) {
            case PATIENT_FILE:
                break;
            case PATIENT_FILE_ID: //trailing row id
                selection = SchemePatientFile.PatientFile.ID + " = ? ";
                id = uri.getLastPathSegment();
                selectionArgs = new String[]{id};
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        SQLiteDatabase db = notFhirDbHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    /**
     * Converts a URI to a database object type.
     * @param uri The URI to match to a type
     * @return The type of the URI
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case PATIENT_FILE:
                return SchemePatientFile.PatientFile.CONTENT_TYPE;
            case PATIENT_FILE_ID:
                return SchemePatientFile.PatientFile.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
