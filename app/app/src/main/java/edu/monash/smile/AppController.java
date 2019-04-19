package edu.monash.smile;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.monash.smile.data.FhirCallback;
import edu.monash.smile.data.FhirService;
import edu.monash.smile.data.model.PatientReference;
import edu.monash.smile.observerPattern.Subject;

class AppController extends Subject {
    private static final String TAG = "AppController";
    private Set<PatientReference> patientReferences = new HashSet<>();
    private FhirService fhirService = new FhirService(new OkHttpClient());

    void fetchPatients(int practitionerId) {
        fhirService.loadPatientReferences(practitionerId, new FhirCallback<Set<PatientReference>>() {
            @Override
            public void onResponse(Set<PatientReference> response) {
                patientReferences = response;
                notifyObservers();
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "onFailure: ", e);
            }
        });
    }

    List<PatientReference> getPatientReferences() {
        return new ArrayList<>(patientReferences);
    }
}
