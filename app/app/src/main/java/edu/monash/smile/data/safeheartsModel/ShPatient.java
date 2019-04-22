package edu.monash.smile.data.safeheartsModel;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;

public class ShPatient {
    private final ShPatientReference reference;
    private final Date birthday;
    private final List<ShHumanName> name;

    public ShPatient(ShPatientReference reference, List<ShHumanName> name, Date birthday) {
        this.reference = reference;
        this.birthday = birthday;
        this.name = name;
    }

    public ShPatientReference getReference() {
        return reference;
    }

    public Date getBirthday() {
        return birthday;
    }

    public List<ShHumanName> getNames() {return this.name; }

    @NonNull
    @Override
    public String toString() {
        return "ShPatient = " + reference.toString();
    }


}
