package edu.monash.smile.dashboard.statusTab.observedPatient;

import java.util.List;

import edu.monash.smile.dashboard.statusTab.adapterDelegates.TimeSeriesListItemAdapterDelegate;
import edu.monash.smile.dashboard.statusTab.observedPatient.alertable.AlertStrategy;
import edu.monash.smile.dashboard.statusTab.observedPatient.chartable.ChartStrategy;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;
import edu.monash.smile.data.safeheartsModel.observation.ObservationType;
import edu.monash.smile.data.safeheartsModel.observation.ShObservation;

public class ObservedPatient<T extends ShObservation> {
    private List<T> observations;
    private ShPatientReference shPatientReference;
    private String patientName;
    private AlertStrategy alertStrategy;
    private ChartStrategy chartStrategy;

    /**
     * A detailed patient linked with its observations and ID.
     */
    ObservedPatient(
            List<T> observations,
            ShPatientReference shPatientReference,
            String patientName,
            AlertStrategy alertStrategy,
            ChartStrategy chartStrategy
    ){
        this.observations = observations;
        this.shPatientReference = shPatientReference;
        this.patientName = patientName;
        this.alertStrategy = alertStrategy;
        this.chartStrategy = chartStrategy;
    }

    public boolean showAlert(List<? extends ShObservation> observations) {
        return this.alertStrategy.isAlertRequired(observations);
    }

    public void buildChart(TimeSeriesListItemAdapterDelegate.TimeSeriesViewHolder timeSeriesViewHolder,
                           List<? extends ShObservation> observations) {
        this.chartStrategy.buildChart(timeSeriesViewHolder, observations);
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
