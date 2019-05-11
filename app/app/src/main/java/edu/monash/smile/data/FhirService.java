package edu.monash.smile.data;

import android.util.Log;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Reference;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import edu.monash.smile.data.safeheartsModel.ShPatient;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;
import edu.monash.smile.data.safeheartsModel.observation.BloodPressureObservation;
import edu.monash.smile.data.safeheartsModel.observation.CholesterolObservation;
import edu.monash.smile.data.safeheartsModel.observation.DiastolicObservation;
import edu.monash.smile.data.safeheartsModel.observation.ObservationType;
import edu.monash.smile.data.safeheartsModel.observation.QuantityVariableType;
import edu.monash.smile.data.safeheartsModel.observation.SmokingObservation;
import edu.monash.smile.data.safeheartsModel.observation.SystolicObservation;

import static edu.monash.smile.data.HealthServiceType.FHIR;

class FhirService extends HealthService {
    final private IGenericClient client;
    final static private int TIME_SERIES_LENGTH = 100;

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
     *
     * @param references the patient IDs to find the patient details of
     * @return a mapping from the ID to the patient details
     */
    @Override
    public HashMap<ShPatientReference, ShPatient> getAllPatients(Set<ShPatientReference> references) {
        HashMap<ShPatientReference, ShPatient> shPatients = new HashMap<>();
        for (ShPatientReference reference : references) {
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

    private List<Observation> readObservations(
            ShPatientReference shPatientReference,
            ObservationType type,
            int count
    ) {
        // Request an observation for a patient
        Bundle b = this.client
                .search()
                .forResource(Observation.class)
                .where(new ReferenceClientParam("subject")
                        .hasId(shPatientReference.getId()))
                .where(new TokenClientParam("code")
                        .exactly()
                        .code(observationCodeToFhirCode(type))
                )
                .count(count)
                .returnBundle(Bundle.class)
                .execute();

        List<Observation> observations = new ArrayList<>();

        for (Bundle.BundleEntryComponent e : b.getEntry()) {
            observations.add((Observation) e.getResource());
        }

        return observations;
    }

    @Override
    public List<BloodPressureObservation> readBloodPressureTimeSeries(ShPatientReference reference) {
        List<Observation> observations = readObservations(reference, ObservationType.BLOOD_PRESSURE, TIME_SERIES_LENGTH);

        List<BloodPressureObservation> results = new ArrayList<>();

        for (Observation o : observations) {
            results.add(convertToBloodPressureObservation(o));
        }

        return results;
    }

    private BloodPressureObservation convertToBloodPressureObservation(Observation observation) {
        List<Observation.ObservationComponentComponent> components = observation.getComponent();

        Date date = observation.getEffectiveDateTimeType().getValue();

        Observation.ObservationComponentComponent fhirSystolic = components.get(1);
        Quantity systolicQuantity = (Quantity) fhirSystolic.getValue();
        SystolicObservation systolicObservation = new SystolicObservation(
                systolicQuantity.getValue(),
                systolicQuantity.getUnit(),
                fhirSystolic.getCode().getText(),
                date,
                QuantityVariableType.TIME_SERIES
        );

        Observation.ObservationComponentComponent fhirDiastolic = components.get(0);
        Quantity diastolicQuantity = (Quantity) fhirDiastolic.getValue();
        DiastolicObservation diastolicObservation = new DiastolicObservation(
                diastolicQuantity.getValue(),
                diastolicQuantity.getUnit(),
                fhirDiastolic.getCode().getText(),
                date,
                QuantityVariableType.TIME_SERIES
        );

        return new BloodPressureObservation(
                systolicObservation,
                diastolicObservation,
                "Blood pressure",
                date, // Date observed
                QuantityVariableType.TIME_SERIES
        );
    }

    @Override
    public List<SmokingObservation> readSmokingStatus(ShPatientReference reference) {
        List<Observation> observations = readObservations(reference, ObservationType.SMOKING, 1);

        List<SmokingObservation> results = new ArrayList<>();

        for (Observation o : observations) {
            results.add(convertToSmokingObservation(o));
        }

        return results;
    }

    /**
     * Extracts information from FHIR into a SmokingObservation
     * @param observation An observation from the FHIR library
     * @return A SmokingObservation
     */
    private SmokingObservation convertToSmokingObservation(Observation observation) {
        CodeableConcept codeableConcept = observation.getValueCodeableConcept();
        return new SmokingObservation(
                codeableConcept.getText(), // Unit
                observation.getCode().getText(), // Description
                observation.getEffectiveDateTimeType().getValue(), // Date observed
                QuantityVariableType.SINGLE_TEXT
        );
    }

    /**
     * Reads only the latest historical observations for a given type (e.g. CHOLESTEROL), for a patient
     *
     * @param reference The ID of the patient
     * @return A list with all observations for the given type
     */
    public List<CholesterolObservation> readCholesterol(ShPatientReference reference) {
        List<Observation> observations = readObservations(reference, ObservationType.CHOLESTEROL, 1);

        List<CholesterolObservation> results = new ArrayList<>();

        for (Observation o : observations) {
            results.add(convertToCholesterolObservation(o));
        }

        return results;
    }

    /**
     * Extracts information from FHIR into a CholesterolObservation
     * @param observation An observation from the FHIR library
     * @return A CholesterolObservation
     */
    private CholesterolObservation convertToCholesterolObservation(Observation observation) {
        Quantity quantity = (Quantity) observation.getValue();
        return new CholesterolObservation(
                quantity.getValue(), // Value
                quantity.getUnit(), // Unit
                observation.getCode().getText(), // Description
                observation.getEffectiveDateTimeType().getValue(), // Date observed
                QuantityVariableType.SINGLE_NUMERIC
        );
    }

    /**
     * An internal class that maps from the ObservationType to the internal FHIR code string
     *
     * @param type the observation type
     * @return FHIR code representing the observation type
     */
    private String observationCodeToFhirCode(ObservationType type) {
        switch (type) {
            case CHOLESTEROL:
                return "2093-3";
            case BLOOD_PRESSURE:
                return "55284-4";
            case SMOKING:
                return "72166-2";
            default:
                return null;
        }
    }
}
