package edu.monash.smile.data.model;

import androidx.annotation.NonNull;

public class Patient {
    private final PatientReference reference;
    private final String emailAddress;

    public Patient(PatientReference reference, String emailAddress) {
        this.reference = reference;
        this.emailAddress = emailAddress;
    }

    public PatientReference getReference() {
        return reference;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @NonNull
    @Override
    public String toString() {
        return "Patient = " + reference.toString();
    }
}
