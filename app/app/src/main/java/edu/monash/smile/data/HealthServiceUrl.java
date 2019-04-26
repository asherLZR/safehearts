package edu.monash.smile.data;

import java.util.EnumMap;

public final class HealthServiceUrl {
    private static EnumMap<HealthServiceType, String> stateMap;

    public enum HealthServiceType {
        FHIR
    }

    String getServiceUrl(HealthServiceType type){
        return stateMap.get(type);
    }

    HealthServiceUrl(){
        stateMap = new EnumMap<>(HealthServiceType.class);
        stateMap.put(HealthServiceType.FHIR, "http://hapi-fhir.erc.monash.edu:8080/baseDstu3/");
    }
}

