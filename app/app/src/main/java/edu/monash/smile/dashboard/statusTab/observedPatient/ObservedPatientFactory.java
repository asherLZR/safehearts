package edu.monash.smile.dashboard.statusTab.observedPatient;

import java.util.List;

import edu.monash.smile.dashboard.statusTab.observedPatient.alertable.AlertStrategy;
import edu.monash.smile.dashboard.statusTab.observedPatient.alertable.BloodPressureAlertStrategy;
import edu.monash.smile.dashboard.statusTab.observedPatient.alertable.NoAlertStrategy;
import edu.monash.smile.dashboard.statusTab.observedPatient.chartable.BloodPressureChartStrategy;
import edu.monash.smile.dashboard.statusTab.observedPatient.chartable.ChartStrategy;
import edu.monash.smile.dashboard.statusTab.observedPatient.chartable.NoChartStrategy;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;
import edu.monash.smile.data.safeheartsModel.observation.ObservationType;
import edu.monash.smile.data.safeheartsModel.observation.ShObservation;

public class ObservedPatientFactory {
    /**
     * Creates a ObservedPatient based on the observation type.
     * <p>
     * Creation of ObservedPatient should use this factory, as it selects the correct strategies for displaying charting and alerts.
     *
     * @param type               The type of observation
     * @param observations       The collected observations for the patient
     * @param shPatientReference The patient reference
     * @param patientName        The patient's full name
     * @return An ObservedPatient
     */
    public static <T extends ShObservation> ObservedPatient<T> getObservedPatient(
            ObservationType type,
            List<T> observations,
            ShPatientReference shPatientReference,
            String patientName
    ){
        return new ObservedPatient<>(
                observations,
                shPatientReference,
                patientName,
                selectAlertStrategy(type),
                selectChartStrategy(type)
        );
    }

    private static AlertStrategy selectAlertStrategy(ObservationType type) {
        if (type == ObservationType.BLOOD_PRESSURE) {
            return new BloodPressureAlertStrategy();
        }

        return new NoAlertStrategy();
    }

    private static ChartStrategy selectChartStrategy(ObservationType type) {
        if (type == ObservationType.BLOOD_PRESSURE) {
            return new BloodPressureChartStrategy();
        }

        return new NoChartStrategy();
    }
}
