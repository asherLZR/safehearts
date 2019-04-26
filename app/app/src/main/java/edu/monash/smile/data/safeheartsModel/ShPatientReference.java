package edu.monash.smile.data.safeheartsModel;

import androidx.annotation.NonNull;

import edu.monash.smile.data.HealthServiceType;

public class ShPatientReference {
    private final String id;
    private HealthServiceType healthServiceType;

    public ShPatientReference(String id, HealthServiceType type) {
        this.id = id;
        this.healthServiceType = type;
    }

    public String getId() {
        return id;
    }

    // Unique key of the format "HealthServiceType/ID"
    public String getFullReference() {
        assert this.healthServiceType != null;
        return this.healthServiceType + "/" + this.id;
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
        return this.getFullReference().equals(other.getFullReference());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
