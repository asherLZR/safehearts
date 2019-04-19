package edu.monash.smile.data.model;

import androidx.annotation.NonNull;

public class PatientReference {
    private String reference;

    public PatientReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @NonNull
    @Override
    public String toString() {
        return "Patient = " + reference;
    }
}
