package edu.monash.smile.data;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Reference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import edu.monash.smile.data.safeheartsModel.ObservationType;
import edu.monash.smile.data.safeheartsModel.QuantitativeObservation;
import edu.monash.smile.data.safeheartsModel.ShPatient;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;

import static edu.monash.smile.data.HealthServiceType.FHIR;

class FhirService extends HealthService {
    final private IGenericClient client;

    FhirService(String url) {
        super(FHIR);
        this.client = (FhirContext.forDstu3()).newRestfulGenericClient(url);
    }

    /**
     * Loads all the patient IDs that a practitioner is responsible for.
     *
     * @param practitionerId The practitioner of interest
     * @return A set of unique patient IDs that the practitioner has seen
     */
    @Override
    public Set<ShPatientReference> loadPatientReferences(int practitionerId) {
        // Request all encounters by the practitioner
        Bundle b = this.client.search().forResource(Encounter.class)
                .where(new ReferenceClientParam("participant")
                        .hasId("Practitioner/" + practitionerId))
                .returnBundle(Bundle.class)
                .count(10000)           // needed to bypass default page count returned
                .execute();

        // Retain unique patients the practitioner has seen
        Set<ShPatientReference> references = new HashSet<>();
        for (Bundle.BundleEntryComponent entry : b.getEntry()) {
            Encounter encounter = (Encounter) entry.getResource();
            Reference subject = encounter.getSubject();
            String id = subject.getReference();
            references.add(new ShPatientReference(id, this.healthServiceType));
        }

        return references;
    }

    /**
     * Creates a mapping of patient references (IDs) to specific patients.
     * This is used to discover patient details based on their ID.
     * @param references the patient IDs to find the patient details of
     * @return a mapping from the ID to the patient details
     */
    @Override
    public HashMap<ShPatientReference, ShPatient> getAllPatients(Set<ShPatientReference> references){
        HashMap<ShPatientReference, ShPatient> shPatients = new HashMap<>();
        for (ShPatientReference reference: references){
            Bundle patientBundle = this.client.search().forResource(Patient.class)
                    .where(Patient.RES_ID.exactly().identifier(reference.getId()))
                    .returnBundle(Bundle.class)
                    .execute();
            Bundle.BundleEntryComponent entry = patientBundle.getEntry().get(0);
            Patient patient = (Patient) entry.getResource();

            HumanName humanName = patient.getName().get(0);

            assert humanName != null;

            shPatients.put(reference,
                    new ShPatient(reference,
                    humanName.getPrefixAsSingleString() + " " +
                            humanName.getGivenAsSingleString() + " " +
                            humanName.getFamily(),
                    patient.getBirthDate()
            ));
        }
        return shPatients;
    }

    /**
     * Reads all historical observations for a given type (e.g. CHOLESTEROL), for a patient
     *
     * @param shPatientReference The ID of the patient
     * @param type               The type of the observation
     * @return A list with all observations for the given type
     */
    @Override
    public List<QuantitativeObservation> readPatientQuantitativeObservations(
            ShPatientReference shPatientReference,
            ObservationType type
    ) {
        // Request an observation for a patient
        Bundle b = this.client.search().forResource(Observation.class)
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

    /**
     * An internal class that maps from the ObservationType to the internal FHIR code string
     * @param type the observation type
     * @return FHIR code representing the observation type
     */
    private String observationCodeToFhirCode(ObservationType type) {
        if (type == ObservationType.CHOLESTEROL) {
            return "2093-3";
        }
        return null;
    }
}
