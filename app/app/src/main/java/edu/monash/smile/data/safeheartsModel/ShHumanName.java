package edu.monash.smile.data.safeheartsModel;

import java.util.List;

public class ShHumanName {
    private final List<String> prefix;
    private final List<String> givenName;
    private final String familyName;

    public ShHumanName(List<String> prefix, List<String> givenName, String familyName){
        this.prefix = prefix;
        this.givenName = givenName;
        this.familyName = familyName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getGivenName() {
        return givenName.get(0);
    }

    public String getPrefix() {
        return prefix.get(0);
    }

    public String getFullName() {
        return prefix + " " + givenName + " " + familyName;
    }
}
