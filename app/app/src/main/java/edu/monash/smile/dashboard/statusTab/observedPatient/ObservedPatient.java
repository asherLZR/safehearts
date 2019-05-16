package edu.monash.smile.dashboard.statusTab.observedPatient;

import java.util.List;

import edu.monash.smile.dashboard.statusTab.adapterDelegates.TimeSeriesListItemAdapterDelegate;
import edu.monash.smile.dashboard.statusTab.observedPatient.alertStrategy.AlertStrategy;
import edu.monash.smile.dashboard.statusTab.observedPatient.chartStrategy.ChartStrategy;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;
import edu.monash.smile.data.safeheartsModel.observation.ShObservation;

public class ObservedPatient<T extends ShObservation> {
    private final List<T> observations;
    private final ShPatientReference shPatientReference;
    private final String patientName;
    private final AlertStrategy alertStrategy;
    private final ChartStrategy chartStrategy;


    /**
     * Represents a patient observed for a particular observation type.
     * @param observations A list of observations for the given patient
     * @param shPatientReference The reference of the patient
     * @param patientName The full name of the patient
     * @param alertStrategy The strategy to use to determine if an alert should be shown
     * @param chartStrategy The strategy to use to build the chart (if any)
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

    /**
     * Determine if this patient data requires an alert.
     *
     * @param observations The list of observations to base the alert decision on
     * @return true, if the alert should be shown
     */
    public boolean showAlert(List<? extends ShObservation> observations) {
        return this.alertStrategy.isAlertRequired(observations);
    }

    /**
     * Populates a chart. This may not populate the chart if the data is not suitable
     * for charting.
     * @param timeSeriesViewHolder View holder suitable for charting
     * @param observations The data to plot
     */
    public void buildChart(TimeSeriesListItemAdapterDelegate.TimeSeriesViewHolder timeSeriesViewHolder,
                           List<? extends ShObservation> observations) {
        this.chartStrategy.buildChart(timeSeriesViewHolder, observations);
    }

    /**
     * Get the observations of the patient.
     * @return A list of observations
     */
    public List<T> getObservations() {
        return observations;
    }

    /**
     * Get the reference of the patient.
     * @return The patient reference.
     */
    public ShPatientReference getShPatientReference() {
        return shPatientReference;
    }

    /**
     * Get the patient's full name.
     * @return The full name of the patient.
     */
    public String getPatientName() {
        return patientName;
    }
}
