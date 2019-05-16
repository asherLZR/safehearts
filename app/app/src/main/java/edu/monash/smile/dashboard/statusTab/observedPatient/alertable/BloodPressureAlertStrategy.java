package edu.monash.smile.dashboard.statusTab.observedPatient.alertable;

import java.util.List;

import edu.monash.smile.data.safeheartsModel.observation.BloodPressureObservation;
import edu.monash.smile.data.safeheartsModel.observation.QuantitativeObservation;
import edu.monash.smile.data.safeheartsModel.observation.ShObservation;

public class BloodPressureAlertStrategy implements AlertStrategy {
    @Override
    public boolean isAlertRequired(List<? extends ShObservation> observations) {
        BloodPressureObservation observation = (BloodPressureObservation) observations.get(0);
        QuantitativeObservation systolic = observation.getSystolicObservation();
        QuantitativeObservation diastolic = observation.getDiastolicObservation();
        // If at any point in time, the patient exceeds normal thresholds, display an alert
        return systolic.getValue().intValue() > 180 || diastolic.getValue().intValue() > 120;
    }
}
