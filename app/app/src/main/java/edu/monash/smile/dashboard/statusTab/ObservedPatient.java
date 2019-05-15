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
    private Alertable alertable;
    private Chartable chartable;

    /**
     * A detailed patient linked with its observations and ID.
     */
    ObservedPatient(
            List<T> observations,
            ShPatientReference shPatientReference,
            String patientName,
            Alertable alertable,
            Chartable chartable
    ){
        this.observations = observations;
        this.shPatientReference = shPatientReference;
        this.patientName = patientName;
        this.alertable = alertable;
        this.chartable = chartable;
    }

    @Override
    public boolean alertIf(List<? extends ShObservation> observations) {
        return this.alertable.alertIf(observations);
    }

    @Override
    public void chart(TimeSeriesListItemAdapterDelegate.TimeSeriesViewHolder timeSeriesViewHolder,
                      List<? extends ShObservation> observations) {
        this.chartable.chart(timeSeriesViewHolder, observations);
    }

    public List<T> getObservations() {
        return observations;
    }

    public ObservationType getObservationType() {
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
