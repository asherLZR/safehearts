package edu.monash.smile.dashboard.statusTab.observedPatient.chartStrategy;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import edu.monash.smile.charting.ObservationLineChart;
import edu.monash.smile.dashboard.statusTab.adapterDelegates.TimeSeriesListItemAdapterDelegate;
import edu.monash.smile.data.safeheartsModel.observation.BmiObservation;
import edu.monash.smile.data.safeheartsModel.observation.QuantitativeObservation;
import edu.monash.smile.data.safeheartsModel.observation.ShObservation;

public class BmiChartStrategy implements ChartStrategy {
    @Override
    public void buildChart(TimeSeriesListItemAdapterDelegate.TimeSeriesViewHolder timeSeriesViewHolder, List<? extends ShObservation> observations) {
        List<QuantitativeObservation> chartData = new ArrayList<>();
        for (int i = 0; i < observations.size(); i++) {
            BmiObservation observation = (BmiObservation) observations.get(i);
            chartData.add(observation);
        }

        ObservationLineChart observationLineChart = new ObservationLineChart(timeSeriesViewHolder.getLineChartView());

        observationLineChart.createLineDataSet(
                chartData,
                "BMI",
                Color.MAGENTA
        );

        observationLineChart.plot();
    }
}
