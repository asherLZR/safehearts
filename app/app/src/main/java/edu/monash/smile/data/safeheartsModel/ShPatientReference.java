package edu.monash.smile.data.safeheartsModel;

import androidx.annotation.NonNull;

import edu.monash.smile.data.HealthServiceType;

public class ShPatientReference {
    private final String id;
    private HealthServiceType healthServiceType;

    // FIXME: temporary overloading - always specify type for new declarations!
    public ShPatientReference(String id) {
        this.id = id;
        this.healthServiceType = null;
    }

    public ShPatientReference(String id, HealthServiceType type) {
        this.id = id;
        this.healthServiceType = type;
    }

    public String getId() {
        return id;
    }

    // Unique key of the format "HealthServiceType/ID"
    public String getFullReference() { return this.healthServiceType + "/" + this.id; }

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
