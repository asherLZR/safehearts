package edu.monash.smile.dashboard.statusTab.chartable;

import edu.monash.smile.dashboard.statusTab.adapterDelegates.TimeSeriesListItemAdapterDelegate.TimeSeriesViewHolder;

/**
 * Allows charting with a view holder that has a chart view.
 * */
public interface Chartable {
    void chart(TimeSeriesViewHolder timeSeriesViewHolder);
}
