package edu.monash.smile.data;

public class HealthServiceProducer {
    public static HealthService getService(HealthServiceType type){
        if (type == HealthServiceType.FHIR) {
            return new FhirService();
        }
        return null;
    }
}
