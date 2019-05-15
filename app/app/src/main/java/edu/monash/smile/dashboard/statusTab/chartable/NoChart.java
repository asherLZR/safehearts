package edu.monash.smile.dashboard.statusTab.chartable;

import edu.monash.smile.dashboard.statusTab.adapterDelegates.TimeSeriesListItemAdapterDelegate;

/**
 * Always does nothing with a Chart view.
 * */
public class NoChart implements Chartable{
    @Override
    public void chart(TimeSeriesListItemAdapterDelegate.TimeSeriesViewHolder timeSeriesViewHolder) {

    }
}
