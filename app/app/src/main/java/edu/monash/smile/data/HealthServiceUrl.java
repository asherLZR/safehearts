package edu.monash.smile.data;

import java.util.EnumMap;

public final class HealthServiceUrl {
    private static EnumMap<HEALTH_SERVICE_TYPE, String> stateMap;

    public enum HEALTH_SERVICE_TYPE {
        FHIR
    }

    String getServiceUrl(HEALTH_SERVICE_TYPE type){
        return stateMap.get(type);
    }

    HealthServiceUrl(){
        stateMap = new EnumMap<>(HEALTH_SERVICE_TYPE.class);
        stateMap.put(HEALTH_SERVICE_TYPE.FHIR, "http://hapi-fhir.erc.monash.edu:8080/baseDstu3/");
    }
}

