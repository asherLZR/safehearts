package edu.monash.smile.data.safeheartsModel;

import androidx.annotation.NonNull;

import java.util.Date;

public class ShPatient {
    private final ShPatientReference reference;
    private final Date birthday;
    private final String name;

    /**
     * A Patient model class
     * @param reference the ID of this patient
     * @param name the name of this patient
     * @param birthday the birthday of this patient
     */
    public ShPatient(ShPatientReference reference, String name, Date birthday) {
        this.reference = reference;
        this.birthday = birthday;
        this.name = name;
    }

    public ShPatientReference getReference() {
        return reference;
    }

    public String getName() {return this.name; }

    @NonNull
    @Override
    public String toString() {
        return "ShPatient = " + reference.toString();
    }
}
