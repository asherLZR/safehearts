package edu.monash.smile.dashboard.statusTab;

import android.app.Activity;

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter;

import java.util.ArrayList;
import java.util.List;

import edu.monash.smile.dashboard.statusTab.adapterDelegates.SingleNumericListItemAdapterDelegate;
import edu.monash.smile.dashboard.statusTab.adapterDelegates.SingleStatusListItemAdapterDelegate;
import edu.monash.smile.dashboard.statusTab.adapterDelegates.TimeSeriesListItemAdapterDelegate;
import edu.monash.smile.data.safeheartsModel.observation.ObservedPatient;
import edu.monash.smile.data.safeheartsModel.observation.ShObservation;

public class StatusCardAdapter extends ListDelegationAdapter<List<ObservedPatient<? extends ShObservation>>> {
    private List<ObservedPatient<? extends ShObservation>> observationCollectionList = new ArrayList<>();

    /**
     * The status card is a summary of the patient's health, shown in the dashboard.
     * It displays information about the patient's tracked observations.
     */
    StatusCardAdapter(Activity activity) {
        delegatesManager
                .addDelegate(new SingleNumericListItemAdapterDelegate(activity))
                .addDelegate(new SingleStatusListItemAdapterDelegate(activity))
                .addDelegate(new TimeSeriesListItemAdapterDelegate(activity));

        setItems(observationCollectionList);
    }

    /**
     * Called when the underlying data source changes (e.g. when new patients are observed)
     *
     * @param observationCollection a list of all the observed patients to show in this list
     */
    void updateAllMonitoredPatients(List<ObservedPatient<? extends ShObservation>> observationCollection) {
        this.observationCollectionList = observationCollection;
        setItems(observationCollectionList);
    }
}
