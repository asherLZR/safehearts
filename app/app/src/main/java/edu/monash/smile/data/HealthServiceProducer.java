package edu.monash.smile.data;

public class HealthServiceProducer {
    /**
     * The FHIR URL for the Monash FHIR server.
     */
    private static final String FHIR_URL = "http://hapi-fhir.erc.monash.edu:8080/baseDstu3/";

    /**
     * Instantiates a health service based on the provided type.
     * @param type the preferred data provider
     * @return a configured health service
     */
    public static HealthService getService(HealthServiceType type){
        if (type == HealthServiceType.FHIR) {
            return new FhirService(FHIR_URL);
        }
        return null;
    }
}
