package edu.monash.smile.data;

import static edu.monash.smile.data.HealthServiceUrl.HEALTH_SERVICE_TYPE;

public class HealthServiceProducer {
    public static HealthService getService(HEALTH_SERVICE_TYPE type){
        if (type == HEALTH_SERVICE_TYPE.FHIR) {
            return new FhirService();
        }
        return null;
    }
}
