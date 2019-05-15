package edu.monash.smile.dashboard.statusTab;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import edu.monash.smile.charting.ObservationLineChart;
import edu.monash.smile.dashboard.DashboardActivity;
import edu.monash.smile.dashboard.PatientsMonitor;
import edu.monash.smile.dashboard.statusTab.alertable.Alertable;
import edu.monash.smile.dashboard.statusTab.alertable.NoAlert;
import edu.monash.smile.dashboard.statusTab.chartable.Chartable;
import edu.monash.smile.dashboard.statusTab.chartable.NoChart;
import edu.monash.smile.data.HealthService;
import edu.monash.smile.data.HealthServiceProducer;
import edu.monash.smile.data.safeheartsModel.ShPatient;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;
import edu.monash.smile.data.safeheartsModel.observation.BloodPressureObservation;
import edu.monash.smile.data.safeheartsModel.observation.CholesterolObservation;
import edu.monash.smile.data.safeheartsModel.observation.ObservationType;
import edu.monash.smile.data.safeheartsModel.observation.QuantitativeObservation;
import edu.monash.smile.data.safeheartsModel.observation.ShObservation;
import edu.monash.smile.data.safeheartsModel.observation.SmokingObservation;
import edu.monash.smile.observerPattern.Subject;

class PatientObservationController extends Subject {
    private HashMap<ShPatientReference, List<CholesterolObservation>> cholesterolObservations = new HashMap<>();
    private HashMap<ShPatientReference, List<SmokingObservation>> smokingObservations = new HashMap<>();
    private HashMap<ShPatientReference, List<BloodPressureObservation>> bloodPressureObservations = new HashMap<>();
    private HealthService healthService;
    private PatientsMonitor patientsMonitor;
    private HashMap<ShPatientReference, ShPatient> shPatients;

    PatientObservationController(PatientsMonitor patientsMonitor) {
        this.patientsMonitor = patientsMonitor;
        this.healthService = HealthServiceProducer.getService(DashboardActivity.HEALTH_SERVICE_TYPE);
    }

    /**
     * Loads data about the patients and notifies observers when loading is complete.
     *
     * Network operations need to run on a separate thread to avoid blocking the main thread.
     */
    void setUp() {
        loadPatientData();
        notifyObservers();
    }

    /**
     * Populates cholesterolObservations of the patients of the practitioner.
     */
    private void loadPatientData() {
        // Remove stale patient data (if any)
        cholesterolObservations.clear();
        smokingObservations.clear();
        bloodPressureObservations.clear();

        // Get data for ALL patients (the assignment only needs data for the selected subset)
        for (
                ShPatientReference patient :
                patientsMonitor.getMonitoredPatientsByType(ObservationType.CHOLESTEROL)
        ) {
            List<CholesterolObservation> cholesterolResult = healthService.readCholesterol(patient);
            if (cholesterolResult.size() != 0) {
                cholesterolObservations.put(patient, cholesterolResult);
            }
        }

        for (
                ShPatientReference patient :
                patientsMonitor.getMonitoredPatientsByType(ObservationType.SMOKING)
        ) {
            List<SmokingObservation> smokingResult = healthService.readSmokingStatus(patient);
            if (smokingResult.size() != 0) {
                smokingObservations.put(patient, smokingResult);
            }
        }

        for (
                ShPatientReference patient :
                patientsMonitor.getMonitoredPatientsByType(ObservationType.BLOOD_PRESSURE)
        ) {
            List<BloodPressureObservation> bloodPressureResult = healthService.readBloodPressureTimeSeries(patient);
            if (bloodPressureResult.size() != 0) {
                bloodPressureObservations.put(patient, bloodPressureResult);
            }
        }

        this.shPatients = healthService.getAllPatients(this.patientsMonitor.getAllMonitoredPatients());
    }

    List<ObservedPatient<? extends ShObservation>> getAllObservationCollections(){
        List<ObservedPatient<? extends ShObservation>> observationCollectionList = new ArrayList<>();
        observationCollectionList.addAll(getObservedCholesterolPatients());
        observationCollectionList.addAll(getObservedSmokingPatients());
        observationCollectionList.addAll(getObservedBloodPressurePatients());
        return observationCollectionList;
    }

    /**
     * Links each patient to its cholesterolObservations.
     *
     * @return A list of patients with their metrics (e.g. a patient and CHOLESTEROL readings)
     */
    private List<ObservedPatient<CholesterolObservation>> getObservedCholesterolPatients() {
        List<ObservedPatient<CholesterolObservation>> observedPatients = new ArrayList<>();

        for (ShPatientReference p : cholesterolObservations.keySet()) {
            observedPatients.add(new ObservedPatient<>(
                    cholesterolObservations.get(p),
                    p,
                    Objects.requireNonNull(shPatients.get(p)).getName(),
                    new NoAlert(),
                    new NoChart()
            ));
        }

        return observedPatients;
    }

    private List<ObservedPatient<SmokingObservation>> getObservedSmokingPatients() {
        List<ObservedPatient<SmokingObservation>> observedPatients = new ArrayList<>();

        for (ShPatientReference p : smokingObservations.keySet()) {
            observedPatients.add(new ObservedPatient<>(
                    smokingObservations.get(p),
                    p,
                    Objects.requireNonNull(shPatients.get(p)).getName(),
                    new NoAlert(),
                    new NoChart())
            );
        }

        return observedPatients;
    }

    private List<ObservedPatient<BloodPressureObservation>> getObservedBloodPressurePatients() {
        List<ObservedPatient<BloodPressureObservation>> observedPatients = new ArrayList<>();

        Alertable alertable = observations -> {
            BloodPressureObservation observation = (BloodPressureObservation) observations.get(0);
            QuantitativeObservation systolic = observation.getSystolicObservation();
            QuantitativeObservation diastolic = observation.getDiastolicObservation();
            // If at any point in time, the patient exceeds normal thresholds, display an alert
            return systolic.getValue().intValue() > 180 || diastolic.getValue().intValue() > 120;
        };

        Chartable chartable = (timeSeriesViewHolder, observations) -> {
            List<QuantitativeObservation> systolicObservations = new ArrayList<>();
            List<QuantitativeObservation> diastolicObservations = new ArrayList<>();
            for (int i = 0; i < observations.size(); i++) {
                BloodPressureObservation observation = (BloodPressureObservation) observations.get(i);
                QuantitativeObservation systolic = observation.getSystolicObservation();
                QuantitativeObservation diastolic = observation.getDiastolicObservation();

                systolicObservations.add(systolic);
                diastolicObservations.add(diastolic);
            }
            ObservationLineChart observationLineChart = new ObservationLineChart(timeSeriesViewHolder.getLineChartView());
            observationLineChart.createLineDataSet(
                    systolicObservations,
                    "Systolic Blood Pressure",
                    Color.BLUE
            );
            observationLineChart.createLineDataSet(
                    diastolicObservations,
                    "Diastolic Blood Pressure",
                    Color.BLACK
            );
            observationLineChart.plot();
        };
        for (ShPatientReference p : bloodPressureObservations.keySet()) {
            observedPatients.add(new ObservedPatient<>(
                    bloodPressureObservations.get(p),
                    p,
                    Objects.requireNonNull(shPatients.get(p)).getName(),
                    alertable,
                    chartable
            ));
        }
        return observedPatients;
    }
}
