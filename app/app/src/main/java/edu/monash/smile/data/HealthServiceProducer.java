package edu.monash.smile.data;

public class HealthServiceProducer {
    private static final String FHIR_URL = "http://hapi-fhir.erc.monash.edu:8080/baseDstu3/";

    public static HealthService getService(HealthServiceType type){
        if (type == HealthServiceType.FHIR) {
            return new FhirService(FHIR_URL);
        }
        return null;
    }
}
