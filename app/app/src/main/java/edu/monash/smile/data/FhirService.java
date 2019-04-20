package edu.monash.smile.data;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Reference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import edu.monash.smile.data.model.ObservationType;
import edu.monash.smile.data.model.PatientReference;
import edu.monash.smile.data.model.QuantitativeObservation;

public class FhirService implements HealthService {
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

    public List<QuantitativeObservation> readPatientQuantitativeObservations(
            PatientReference patientReference,
            ObservationType type
    ) {
        // Request an observation for a patient
        Bundle b = client.search().forResource(Observation.class)
                .where(new ReferenceClientParam("subject")
                        .hasId(patientReference.getId()))
                .where(new TokenClientParam("code")
                        .exactly()
                        .code(observationCodeToFhirCode(type)))
                .returnBundle(Bundle.class)
                .execute();

        List<Observation> observations = new ArrayList<>();

        // Maps from bundle entry into a specific observation
        for (Bundle.BundleEntryComponent e : b.getEntry()) {
            observations.add((Observation) e.getResource());
        }

        List<QuantitativeObservation> quantitativeObservations = new ArrayList<>(observations.size());

        // Extracts information about the information into a QuantitativeObservation
        for (Observation o : observations) {
            String description = o.getCode().getText();
            Quantity quantity = (Quantity) o.getValue();
            String result = quantity.getValue() + " " + quantity.getUnit();
            quantitativeObservations.add(new QuantitativeObservation(result, description));
        }

        return quantitativeObservations;
    }

    private String observationCodeToFhirCode(ObservationType type) {
        if (type == ObservationType.cholesterol) {
            return "2093-3";
        }

        return null;
    }
}
