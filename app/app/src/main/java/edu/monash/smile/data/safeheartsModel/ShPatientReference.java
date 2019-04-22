package edu.monash.smile.data.safeheartsModel;

import androidx.annotation.NonNull;

public class ShPatientReference {
    private final String id;

    public ShPatientReference(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return "ShPatientReference = " + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShPatientReference other = (ShPatientReference) o;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
