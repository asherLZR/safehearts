package edu.monash.smile.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StringType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import edu.monash.smile.data.safeheartsModel.ObservationType;
import edu.monash.smile.data.safeheartsModel.ShHumanName;
import edu.monash.smile.data.safeheartsModel.ShPatient;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;
import edu.monash.smile.data.safeheartsModel.QuantitativeObservation;
import edu.monash.smile.preferences.SharedPreferencesHelper;

public class FhirService implements HealthService {
    private static final String TAG = "FhirService";
    final private IGenericClient client;
    private static String BASE_URL = "http://hapi-fhir.erc.monash.edu:8080/baseDstu3/";

    public FhirService() {
        this.client = (FhirContext.forDstu3()).newRestfulGenericClient(BASE_URL);
    }

    public Set<ShPatientReference> loadPatientReferences(Context context, Integer practitionerId) {
        // Request all encounters by the practitioner
        Bundle b = client.search().forResource(Encounter.class)
                .where(new ReferenceClientParam("participant")
                        .hasId("Practitioner/" + practitionerId))
                .returnBundle(Bundle.class)
                .execute();

        // Retain unique patients the practitioner has seen
        Set<ShPatientReference> references = new HashSet<>();
        for (Bundle.BundleEntryComponent entry : b.getEntry()) {
            Encounter encounter = (Encounter) entry.getResource();
            Reference subject = encounter.getSubject();
            String id = subject.getReference();
            references.add(new ShPatientReference(id));
        }

        storeAllPatients(context, references);

        return references;
    }

    // For all patients the practitioner has seen, create ShPatients and store in SP
    private void storeAllPatients(Context context, Set<ShPatientReference> references){
        ArrayList<ShPatient> shPatients = new ArrayList<>();
        for (ShPatientReference reference: references){
            Bundle patientBundle = client.search().forResource(Patient.class)
                    .where(Patient.RES_ID.exactly().identifier(reference.getId()))
                    .returnBundle(Bundle.class)
                    .execute();
            for (Bundle.BundleEntryComponent entry : patientBundle.getEntry()) {
                Patient patient = (Patient) entry.getResource();
                List<HumanName> humanNames = patient.getName();
                ArrayList<ShHumanName> shHumanNames = new ArrayList<>();
                for (HumanName name : humanNames) {
                    // Convert List<StringType> to List<String>
                    List<String> prefixes = name.getPrefix().stream()
                            .map(StringType::toString)
                            .collect(Collectors.toList());
                    List<String> givenNames = name.getGiven().stream()
                            .map(StringType::toString)
                            .collect(Collectors.toList());
                    shHumanNames.add(new ShHumanName(prefixes, givenNames, name.getFamily()));
                }
                shPatients.add(new ShPatient(
                        reference,
                        shHumanNames,
                        patient.getBirthDate()
                ));
            }
            SharedPreferencesHelper.writeAllPatients(context, shPatients);
        }
    }

    public List<QuantitativeObservation> readPatientQuantitativeObservations(
            ShPatientReference shPatientReference,
            ObservationType type
    ) {
        // Request an observation for a patient
        Bundle b = client.search().forResource(Observation.class)
                .where(new ReferenceClientParam("subject")
                        .hasId(shPatientReference.getId()))
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
