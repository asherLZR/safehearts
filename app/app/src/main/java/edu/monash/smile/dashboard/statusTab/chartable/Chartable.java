package edu.monash.smile.dashboard.statusTab.chartable;

import java.util.List;

import edu.monash.smile.dashboard.statusTab.adapterDelegates.TimeSeriesListItemAdapterDelegate.TimeSeriesViewHolder;
import edu.monash.smile.data.safeheartsModel.observation.ShObservation;

/**
 * Allows charting with a view holder that has a chart view.
 * */
public interface Chartable {
    void chart(TimeSeriesViewHolder timeSeriesViewHolder, List<? extends ShObservation> observations);
}
