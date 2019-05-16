package edu.monash.smile.dashboard.statusTab.observedPatient.chartable;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import edu.monash.smile.charting.ObservationLineChart;
import edu.monash.smile.dashboard.statusTab.adapterDelegates.TimeSeriesListItemAdapterDelegate;
import edu.monash.smile.data.safeheartsModel.observation.BloodPressureObservation;
import edu.monash.smile.data.safeheartsModel.observation.QuantitativeObservation;
import edu.monash.smile.data.safeheartsModel.observation.ShObservation;

/**
 * Strategy to create a line chart for blood pressure readings (both systolic and diastolic).
 */
public class BloodPressureChartStrategy implements ChartStrategy {
    @Override
    public void buildChart(TimeSeriesListItemAdapterDelegate.TimeSeriesViewHolder timeSeriesViewHolder, List<? extends ShObservation> observations) {
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
}
