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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PatientReference other = (PatientReference) o;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
