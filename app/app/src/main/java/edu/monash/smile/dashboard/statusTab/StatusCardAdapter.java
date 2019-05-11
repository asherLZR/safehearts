package edu.monash.smile.dashboard.statusTab;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.monash.smile.R;
import edu.monash.smile.charting.LineChartHelper;
import edu.monash.smile.data.safeheartsModel.observation.BloodPressureObservation;
import edu.monash.smile.data.safeheartsModel.observation.CholesterolObservation;
import edu.monash.smile.data.safeheartsModel.observation.ObservationCollection;
import edu.monash.smile.data.safeheartsModel.observation.ObservedPatient;
import edu.monash.smile.data.safeheartsModel.observation.QuantitativeObservation;
import edu.monash.smile.data.safeheartsModel.observation.QuantityVariableType;
import edu.monash.smile.data.safeheartsModel.observation.ShObservation;
import edu.monash.smile.data.safeheartsModel.observation.SmokingObservation;

public class StatusCardAdapter extends RecyclerView.Adapter<StatusCardAdapter.StatusCardViewHolder> {
    private static final int SINGLE_NUMERIC_VIEW = 0;
    private static final int SINGLE_TEXT_VIEW = 1;
    private static final int TIME_SERIES_VIEW = 2;
    private List<ObservedPatient> observationCollectionList = new ArrayList<>();

    /**
     * The status card is a summary of the patient's health, shown in the dashboard.
     * It displays information about the patient's tracked observations.
     */
    StatusCardAdapter() {}

    /**
     * Inflates the card layout for the ViewHolder.
     */
    @NonNull
    @Override
    public StatusCardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == SINGLE_NUMERIC_VIEW) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_value_numerical_card,
                    viewGroup,
                    false); //CardView inflated as RecyclerView list item
            return new SingleValueNumericalViewHolder(v);
        } else if (viewType == SINGLE_TEXT_VIEW) {
            View v = LayoutInflater.from(
                    viewGroup.getContext()).inflate(R.layout.single_value_string_card,
                    viewGroup,
                    false);
            return new SingleValueStringViewHolder(v);
        } else if (viewType == TIME_SERIES_VIEW) {
            View v = LayoutInflater.from(
                    viewGroup.getContext()).inflate(R.layout.time_series_card,
                    viewGroup,
                    false);
            return new TimeSeriesViewHolder(v);
        }
        throw new IllegalArgumentException("Unhandled view type: " + viewType);
    }

    @Override
    public int getItemViewType(int position) {
        ShObservation shObservation = (ShObservation) this.observationCollectionList.get(position)
                .getObservations().get(0);
        QuantityVariableType quantityVariableType = shObservation.getQuantityVariableType();
        switch (quantityVariableType){
            case SINGLE_NUMERIC:
                return SINGLE_NUMERIC_VIEW;
            case SINGLE_TEXT:
                return SINGLE_TEXT_VIEW;
            case TIME_SERIES:
                return TIME_SERIES_VIEW;
            default:
                throw new IllegalArgumentException("Unhandled quantity variable type" + quantityVariableType);
        }
    }

    /**
     * Displays a status card at a given index with the:
     * - Patient name
     * - Patient ID
     * - Patient observation (description and value)
     */
    @Override
    public void onBindViewHolder(@NonNull StatusCardViewHolder holder, int position) {
        ObservedPatient cardPatient = this.observationCollectionList.get(position);
        holder.cardHeading.setText(cardPatient.getPatientName());
        holder.cardSubheading.setText(cardPatient.getShPatientReference().getFullReference());
        ShObservation observationInstance = (ShObservation) cardPatient.getObservations().get(0);   // used to compare types

        if (holder instanceof SingleValueNumericalViewHolder) {
            SingleValueNumericalViewHolder numericalViewHolder = (SingleValueNumericalViewHolder) holder;
            if (observationInstance instanceof CholesterolObservation){
                CholesterolObservation viewedObservation = (CholesterolObservation) observationInstance;
                numericalViewHolder.numericalCardDescription.setText(viewedObservation.getDescription());
                numericalViewHolder.numericalCardValue.setText(viewedObservation.getValue().toPlainString());
                numericalViewHolder.numericalCardUnit.setText(viewedObservation.getUnit());
            }
        } else if (holder instanceof SingleValueStringViewHolder) {
            SingleValueStringViewHolder stringViewHolder = (SingleValueStringViewHolder) holder;
            if (observationInstance instanceof SmokingObservation){
                SmokingObservation viewedObservation = (SmokingObservation) observationInstance;
                stringViewHolder.stringCardStatus.setText(viewedObservation.getSmokingStatus());
            }
        } else if (holder instanceof TimeSeriesViewHolder){
            TimeSeriesViewHolder timeSeriesViewHolder = (TimeSeriesViewHolder) holder;

            if (observationInstance instanceof BloodPressureObservation){
                List<BloodPressureObservation> o = (List<BloodPressureObservation>) cardPatient.getObservations();
                LineChartHelper lineChartHelper = new LineChartHelper(timeSeriesViewHolder.lineChart);
                List<QuantitativeObservation> systolicObservations = o.stream()
                        .map(BloodPressureObservation::getSystolicObservation)
                        .collect(Collectors.toList());
                List<QuantitativeObservation> diastolicObservations = o.stream()
                        .map(BloodPressureObservation::getDiastolicObservation)
                        .collect(Collectors.toList());
                lineChartHelper.createLineDataSet(
                        systolicObservations,
                        "Systolic Blood Pressure",
                        Color.BLUE
                );
                lineChartHelper.createLineDataSet(
                        diastolicObservations,
                        "Diastolic Blood Pressure",
                        Color.BLACK
                );
                lineChartHelper.plot();
            }
        }
    }

    /**
     * The number of possible views there are.
     *
     * @return count of patients
     */
    @Override
    public int getItemCount() {
        return this.observationCollectionList.size();
    }

//    /**
//     * Called when the underlying data source changes (e.g. when new patients are observed)
//     *
//     * @param cholesterolPatients the observed patients to show in this list
//     */

    void updateAllMonitoredPatients(List<ObservationCollection> observationCollections){
        this.observationCollectionList = new ArrayList<>();
        for (ObservationCollection observationCollection : observationCollections){
            List<ObservedPatient> observedPatientList = observationCollection.getObservations();
            this.observationCollectionList.addAll(observedPatientList);
        }
    }

    static abstract class StatusCardViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView cardHeading;
        TextView cardSubheading;

        StatusCardViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    /**
     * Representation of each individual card stored.
     */
    static class SingleValueNumericalViewHolder extends StatusCardViewHolder {
        TextView numericalCardDescription;
        TextView numericalCardValue;
        TextView numericalCardUnit;

        SingleValueNumericalViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardHeading = itemView.findViewById(R.id.numerical_card_heading);
            this.cardSubheading = itemView.findViewById(R.id.numerical_card_subheading);
            this.numericalCardDescription = itemView.findViewById(R.id.numerical_description);
            this.numericalCardValue = itemView.findViewById(R.id.numerical_value);
            this.numericalCardUnit = itemView.findViewById(R.id.numerical_unit);
        }
    }

    static class SingleValueStringViewHolder extends StatusCardViewHolder {
        TextView stringCardStatus;

        SingleValueStringViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardHeading = itemView.findViewById(R.id.string_heading);
            this.cardSubheading = itemView.findViewById(R.id.string_subheading);
            this.stringCardStatus = itemView.findViewById(R.id.string_status);
        }
    }

    static class TimeSeriesViewHolder extends StatusCardViewHolder {
        LineChart lineChart;

        TimeSeriesViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardHeading = itemView.findViewById(R.id.timeseries_heading);
            this.cardSubheading = itemView.findViewById(R.id.timeseries_subheading);
            this.lineChart = itemView.findViewById(R.id.line_chart);
        }
    }
}
