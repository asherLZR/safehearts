package edu.monash.smile.data;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Reference;

import java.util.HashSet;
import java.util.Set;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import edu.monash.smile.data.model.PatientReference;

public class FhirService {
    private static final String TAG = "FhirService";
    final private IGenericClient client;
    private static String BASE_URL = "http://hapi-fhir.erc.monash.edu:8080/baseDstu3/";

    public FhirService() {
        this.client = (FhirContext.forDstu3()).newRestfulGenericClient(BASE_URL);
    }

    public Set<PatientReference> loadPatientReferences(Integer practitionerId) {
        // Request all encounters by the practitioner
        Bundle b = client.search().forResource(Encounter.class)
                .where(new ReferenceClientParam("participant")
                        .hasId("Practitioner/" + practitionerId))
                .returnBundle(Bundle.class)
                .execute();

        // Retain unique patients the practitioner has seen
        Set<PatientReference> references = new HashSet<>();
        for (Bundle.BundleEntryComponent entry : b.getEntry()) {
            Encounter encounter = (Encounter) entry.getResource();
            Reference subject = encounter.getSubject();
            String id = subject.getReference();
            references.add(new PatientReference(id));
        }

        return references;
    }
}
