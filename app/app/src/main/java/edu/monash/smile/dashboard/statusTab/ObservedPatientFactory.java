package edu.monash.smile.dashboard.statusTab;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import edu.monash.smile.charting.ObservationLineChart;
import edu.monash.smile.dashboard.statusTab.adapterDelegates.TimeSeriesListItemAdapterDelegate;
import edu.monash.smile.dashboard.statusTab.alertable.Alertable;
import edu.monash.smile.dashboard.statusTab.alertable.NoAlert;
import edu.monash.smile.dashboard.statusTab.chartable.Chartable;
import edu.monash.smile.dashboard.statusTab.chartable.NoChart;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;
import edu.monash.smile.data.safeheartsModel.observation.BloodPressureObservation;
import edu.monash.smile.data.safeheartsModel.observation.ObservationType;
import edu.monash.smile.data.safeheartsModel.observation.QuantitativeObservation;
import edu.monash.smile.data.safeheartsModel.observation.ShObservation;

class ObservedPatientFactory {
    static <T extends ShObservation> ObservedPatient<T> getObservedPatient(
            ObservationType type,
            List<T> observationList,
            ShPatientReference shPatientReference,
            String patientName
    ){
        return new ObservedPatient<>(
                observationList,
                shPatientReference,
                patientName,
                selectAlertable(type),
                selectChartable(type)
        );
    }

    private static Alertable selectAlertable(ObservationType type){
        switch (type){
            case CHOLESTEROL:
            case SMOKING:
                return new NoAlert();
            case BLOOD_PRESSURE:
                return new Alertable() {
                    @Override
                    public boolean alertIf(List<? extends ShObservation> observations) {
                        BloodPressureObservation observation = (BloodPressureObservation) observations.get(0);
                        QuantitativeObservation systolic = observation.getSystolicObservation();
                        QuantitativeObservation diastolic = observation.getDiastolicObservation();
                        // If at any point in time, the patient exceeds normal thresholds, display an alert
                        return systolic.getValue().intValue() > 180 || diastolic.getValue().intValue() > 120;
                    }
                } ;
            default:
                throw new IllegalArgumentException("Invalid observation type");
        }
    }

    private static Chartable selectChartable(ObservationType type){
        switch (type){
            case CHOLESTEROL:
            case SMOKING:
                return new NoChart();
            case BLOOD_PRESSURE:
                return new Chartable() {
                    @Override
                    public void chart(TimeSeriesListItemAdapterDelegate.TimeSeriesViewHolder timeSeriesViewHolder, List<? extends ShObservation> observations) {
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
                    }
                };
            default:
                throw new IllegalArgumentException("Invalid observation type");
        }
    }
}
