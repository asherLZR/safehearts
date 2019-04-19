package edu.monash.smile.data.model;

import androidx.annotation.NonNull;

public class PatientReference {
    private final String id;

    public PatientReference(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return "PatientReference = " + id;
    }
}
