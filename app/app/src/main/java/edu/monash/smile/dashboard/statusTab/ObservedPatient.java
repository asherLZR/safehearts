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

public class ObservedPatient<T extends ShObservation> implements Alertable, Chartable {
    private List<T> observations;
    private ShPatientReference shPatientReference;
    private String patientName;
    private Alertable alertAction;
    private Chartable chartable;

    /**
     * A detailed patient linked with its observations and ID.
     */
    ObservedPatient(
            List<T> observations,
            ShPatientReference shPatientReference,
            String patientName
    ){
        this.observations = observations;
        this.shPatientReference = shPatientReference;
        this.patientName = patientName;
        this.initialiseAlertable();
        this.initialiseChartable();
    }

    private void initialiseAlertable(){
        ObservationType type = this.getObservationType();
        if (type == ObservationType.BLOOD_PRESSURE){
            this.alertAction = () -> {
                BloodPressureObservation observation = (BloodPressureObservation) observations.get(0);
                QuantitativeObservation systolic = observation.getSystolicObservation();
                QuantitativeObservation diastolic = observation.getDiastolicObservation();
                // If at any point in time, the patient exceeds normal thresholds, display an alert
                return systolic.getValue().intValue() > 180 || diastolic.getValue().intValue() > 120;
            };
        }else{
            this.alertAction = new NoAlert();
        }
    }

    private void initialiseChartable() {
        ObservationType type = this.getObservationType();
        if (type == ObservationType.BLOOD_PRESSURE){
            this.chartable = timeSeriesViewHolder -> {
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
        }else{
            this.chartable = new NoChart();
        }
    }

    @Override
    public boolean alertIf(){
        return alertAction.alertIf();
    }

    @Override
    public void chart(TimeSeriesListItemAdapterDelegate.TimeSeriesViewHolder timeSeriesViewHolder) {
        this.chartable.chart(timeSeriesViewHolder);
    }

    public List<T> getObservations() {
        return observations;
    }

    private ObservationType getObservationType() {
        if (observations.size() == 0) {
            throw new IllegalStateException("Observed patient has no observations");
        }
        return observations.get(0).getObservationType();
    }

    public ShPatientReference getShPatientReference() {
        return shPatientReference;
    }

    public String getPatientName() {
        return patientName;
    }
}
