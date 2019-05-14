package edu.monash.smile.dashboard.statusTab;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;

import edu.monash.smile.R;
import edu.monash.smile.charting.ObservationLineChart;
import edu.monash.smile.data.safeheartsModel.observation.BloodPressureObservation;
import edu.monash.smile.data.safeheartsModel.observation.CholesterolObservation;
import edu.monash.smile.data.safeheartsModel.observation.ObservationType;
import edu.monash.smile.data.safeheartsModel.observation.ObservedPatient;
import edu.monash.smile.data.safeheartsModel.observation.QuantitativeObservation;
import edu.monash.smile.data.safeheartsModel.observation.ShObservation;
import edu.monash.smile.data.safeheartsModel.observation.SmokingObservation;

public class StatusCardAdapter extends RecyclerView.Adapter<StatusCardAdapter.BaseCardViewHolder> {
    private static final int SINGLE_NUMERIC_VIEW = 0, SINGLE_TEXT_VIEW = 1, TIME_SERIES_VIEW = 2;
    private List<ObservedPatient<? extends ShObservation>> observationCollectionList = new ArrayList<>();

    /**
     * The status card is a summary of the patient's health, shown in the dashboard.
     * It displays information about the patient's tracked observations.
     */
    StatusCardAdapter() {
    }

    /**
     * Inflates the card layout for the ViewHolder.
     */
    @NonNull
    @Override
    public BaseCardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case SINGLE_NUMERIC_VIEW:
                View singleNumericView = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.single_value_numerical_card,
                        viewGroup,
                        false); //CardView inflated as RecyclerView list item
                return new SingleValueNumericalViewHolder(singleNumericView);
            case SINGLE_TEXT_VIEW:
                View singleTextView = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.single_value_string_card,
                        viewGroup,
                        false);
                return new SingleValueStringViewHolder(singleTextView);
            case TIME_SERIES_VIEW:
                View timeSeriesView = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.time_series_card,
                        viewGroup,
                        false);
                return new TimeSeriesViewHolder(timeSeriesView);
            default:
                throw new IllegalArgumentException("Unhandled view type: " + viewType);
        }
    }

    /**
     * Based on the type of the observation found, determines the kind of view used to display
     * that data.
     */
    @Override
    public int getItemViewType(int position) {
        ObservationType type = observationCollectionList
                .get(position)
                .getObservationType();

        switch (type) {
            case CHOLESTEROL:
                return SINGLE_NUMERIC_VIEW;
            case SMOKING:
                return SINGLE_TEXT_VIEW;
            case BLOOD_PRESSURE:
                return TIME_SERIES_VIEW;
            default:
                throw new IllegalArgumentException("Unhandled observation type: " + type);
        }
    }

    /**
     * Displays a status card at a given index with the:
     * - Patient name
     * - Patient ID
     * - Patient observation (description and value)
     */
    @Override
    public void onBindViewHolder(@NonNull BaseCardViewHolder holder, int position) {
        ObservedPatient<? extends ShObservation> cardPatient = this.observationCollectionList.get(position);

        holder.setUpTitle(
                cardPatient.getPatientName(),
                cardPatient.getShPatientReference().getFullReference()
        );

        ShObservation observationInstance = cardPatient.getObservations().get(0);
        ObservationType observationType = cardPatient.getObservationType();

        if (holder instanceof SingleValueNumericalViewHolder) {
            SingleValueNumericalViewHolder numericalViewHolder = (SingleValueNumericalViewHolder) holder;
            if (observationType == ObservationType.CHOLESTEROL) {
                CholesterolObservation observation = (CholesterolObservation) observationInstance;
                numericalViewHolder.setUpView(
                        observation.getDescription(),
                        observation.getValue().toPlainString(),
                        observation.getUnit()
                );
            }
        } else if (holder instanceof SingleValueStringViewHolder) {
            SingleValueStringViewHolder stringViewHolder = (SingleValueStringViewHolder) holder;
            if (observationType == ObservationType.SMOKING) {
                SmokingObservation observation = (SmokingObservation) observationInstance;
                stringViewHolder.setUpView(observation.getSmokingStatus());
            }
        } else if (holder instanceof TimeSeriesViewHolder) {
            TimeSeriesViewHolder timeSeriesViewHolder = (TimeSeriesViewHolder) holder;
            if (observationType == ObservationType.BLOOD_PRESSURE) {
                List<? extends ShObservation> observations = cardPatient.getObservations();
                List<QuantitativeObservation> systolicObservations = new ArrayList<>();
                List<QuantitativeObservation> diastolicObservations = new ArrayList<>();

                for (int i = 0; i < observations.size(); i++) {
                    BloodPressureObservation observation = (BloodPressureObservation) observations.get(i);
                    QuantitativeObservation systolic = observation.getSystolicObservation();
                    QuantitativeObservation diastolic = observation.getDiastolicObservation();

                    // If at any point in time, the patient exceeds normal thresholds, display an alert
                    if (systolic.getValue().intValue() > 180 || diastolic.getValue().intValue() > 120) {
                        timeSeriesViewHolder.showAlert();
                    } else {
                        timeSeriesViewHolder.hideAlert();
                    }

                    systolicObservations.add(systolic);
                    diastolicObservations.add(diastolic);
                }

                ObservationLineChart observationLineChart = new ObservationLineChart(timeSeriesViewHolder.getLineChartView());
                observationLineChart.createLineDataSet(
                        systolicObservations,
                        "Systolic Blood Pressure",
                        Color.BLUE
                );
                observationLineChart.createLineDataSet(
                        diastolicObservations,
                        "Diastolic Blood Pressure",
                        Color.BLACK
                );
                observationLineChart.plot();
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

    /**
     * Called when the underlying data source changes (e.g. when new patients are observed)
     *
     * @param observationCollection a list of all the observed patients to show in this list
     */
    void updateAllMonitoredPatients(List<ObservedPatient<? extends ShObservation>> observationCollection) {
        this.observationCollectionList = observationCollection;
    }

    /**
     * A generalised version of the view holder to provide common features between all status
     * cards.
     */
    static abstract class BaseCardViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        private TextView cardHeading;
        private TextView cardSubheading;

        BaseCardViewHolder(@NonNull View itemView, int cardHeadingId, int cardSubheadingId) {
            super(itemView);
            this.cardHeading = itemView.findViewById(cardHeadingId);
            this.cardSubheading = itemView.findViewById(cardSubheadingId);
            this.itemView = itemView;
        }

        void setUpTitle(String heading, String subheading) {
            cardHeading.setText(heading);
            cardSubheading.setText(subheading);
        }
    }

    /**
     * Representation of each individual card stored. This deals specifically with displaying
     * data that has only one numerical value to display.
     */
    static class SingleValueNumericalViewHolder extends BaseCardViewHolder {
        private TextView numericalCardDescription;
        private TextView numericalCardValue;
        private TextView numericalCardUnit;

        SingleValueNumericalViewHolder(@NonNull View itemView) {
            super(itemView, R.id.numerical_card_heading, R.id.numerical_card_subheading);
            this.numericalCardDescription = itemView.findViewById(R.id.numerical_description);
            this.numericalCardValue = itemView.findViewById(R.id.numerical_value);
            this.numericalCardUnit = itemView.findViewById(R.id.numerical_unit);
        }

        void setUpView(String description, String cardValue, String cardUnit) {
            numericalCardDescription.setText(description);
            numericalCardValue.setText(cardValue);
            numericalCardUnit.setText(cardUnit);
        }
    }

    /**
     * Representation of each individual card stored. This deals specifically with displaying
     * data that has only one textual value to display.
     */
    static class SingleValueStringViewHolder extends BaseCardViewHolder {
        private TextView stringCardStatus;

        SingleValueStringViewHolder(@NonNull View itemView) {
            super(itemView, R.id.string_heading, R.id.string_subheading);
            this.stringCardStatus = itemView.findViewById(R.id.string_status);
        }

        void setUpView(String status) {
            stringCardStatus.setText(status);
        }
    }

    /**
     * Representation of each individual card stored. This deals specifically with displaying
     * data that may be represented on a continuous scale.
     */
    static class TimeSeriesViewHolder extends BaseCardViewHolder {
        private com.github.mikephil.charting.charts.LineChart lineChart;
        private ImageView alert;

        TimeSeriesViewHolder(@NonNull View itemView) {
            super(itemView, R.id.timeseries_heading, R.id.timeseries_subheading);
            this.lineChart = itemView.findViewById(R.id.line_chart);
            this.alert = itemView.findViewById(R.id.alertImg);
        }

        LineChart getLineChartView() {
            return lineChart;
        }

        void showAlert() {
            alert.setVisibility(View.VISIBLE);
        }

        void hideAlert() {
            alert.setVisibility(View.GONE);
        }
    }
}
