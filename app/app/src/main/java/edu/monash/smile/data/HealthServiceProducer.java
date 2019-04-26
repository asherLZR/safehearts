package edu.monash.smile.data;

public class HealthServiceProducer {
    public static HealthService getService(HealthServiceUrl.HealthServiceType type){
        if (type == HealthServiceUrl.HealthServiceType.FHIR) {
            return new FhirService();
        }
        return null;
    }
}
