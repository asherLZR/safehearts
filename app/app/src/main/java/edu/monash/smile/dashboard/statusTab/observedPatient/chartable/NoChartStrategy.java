package edu.monash.smile.dashboard.statusTab.observedPatient.chartable;

import java.util.List;

import edu.monash.smile.dashboard.statusTab.adapterDelegates.TimeSeriesListItemAdapterDelegate;
import edu.monash.smile.data.safeheartsModel.observation.ShObservation;

/**
 * Strategy to not build a chart.
 * */
public class NoChartStrategy implements ChartStrategy {
    @Override
    public void buildChart(TimeSeriesListItemAdapterDelegate.TimeSeriesViewHolder timeSeriesViewHolder,
                           List<? extends ShObservation> observations) {

    }
}
