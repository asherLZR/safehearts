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
import edu.monash.smile.data.safeheartsModel.observation.ObservedPatient;
import edu.monash.smile.data.safeheartsModel.observation.QuantitativeObservation;
import edu.monash.smile.data.safeheartsModel.observation.SmokingObservation;

public class StatusCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static int CHOLESTEROL_VIEW = 0;
    private static int SMOKING_VIEW = 1;
    private static int BLOOD_PRESSURE_VIEW = 2;
    private List<ObservedPatient<CholesterolObservation>> cholesterolPatients;
    private List<ObservedPatient<SmokingObservation>> smokingPatients;
    private List<ObservedPatient<BloodPressureObservation>> bloodPressurePatients;

    /**
     * The status card is a summary of the patient's health, shown in the dashboard.
     * It displays information about the patient's tracked observations.
     */
    StatusCardAdapter() {
        this.cholesterolPatients = new ArrayList<>();
        this.smokingPatients = new ArrayList<>();
        this.bloodPressurePatients = new ArrayList<>();
    }

    /**
     * Inflates the card layout for the ViewHolder.
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == CHOLESTEROL_VIEW) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_value_numerical_card, viewGroup, false); //CardView inflated as RecyclerView list item
            return new SingleValueNumericalViewHolder(v);
        } else if (viewType == SMOKING_VIEW) {
            View v = LayoutInflater.from(
                    viewGroup.getContext()).inflate(R.layout.single_value_string_card,
                    viewGroup,
                    false);
            return new SingleValueStringViewHolder(v);
        } else if (viewType == BLOOD_PRESSURE_VIEW) {
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
        if (position < cholesterolPatients.size()) {
            return CHOLESTEROL_VIEW;
        } else if (position < (cholesterolPatients.size() + smokingPatients.size())){
            return SMOKING_VIEW;
        }
        return BLOOD_PRESSURE_VIEW;
    }

    /**
     * Displays a status card at a given index with the:
     * - Patient name
     * - Patient ID
     * - Patient observation (description and value)
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SingleValueNumericalViewHolder) {
            SingleValueNumericalViewHolder cholesterolHolder = (SingleValueNumericalViewHolder) holder;
            ObservedPatient<CholesterolObservation> cardPatient = cholesterolPatients.get(position);
            CholesterolObservation viewedObservation = cardPatient.getObservations().get(0);
            cholesterolHolder.numericalCardHeading.setText(cardPatient.getPatientName());
            cholesterolHolder.numericalCardSubheading.setText(cardPatient.getShPatientReference().getFullReference());
            cholesterolHolder.numericalCardDescription.setText(viewedObservation.getDescription());
            cholesterolHolder.numericalCardValue.setText(viewedObservation.getValue().toPlainString());
            cholesterolHolder.numericalCardUnit.setText(viewedObservation.getUnit());
        } else if (holder instanceof SingleValueStringViewHolder) {
            SingleValueStringViewHolder smokingHolder = (SingleValueStringViewHolder) holder;
            ObservedPatient<SmokingObservation> cardPatient = smokingPatients.get(
                    position - cholesterolPatients.size() // Offset by number of cholesterol cards
            );
            smokingHolder.stringCardHeading.setText(cardPatient.getPatientName());
            smokingHolder.stringCardSubheading.setText(cardPatient.getShPatientReference().getFullReference());
            smokingHolder.stringCardStatus.setText(cardPatient.getObservations().get(0).getSmokingStatus());
        } else if (holder instanceof TimeSeriesViewHolder){
            TimeSeriesViewHolder timeSeriesViewHolder = (TimeSeriesViewHolder) holder;
            ObservedPatient<BloodPressureObservation> cardPatient = bloodPressurePatients.get(
                    position - cholesterolPatients.size() - smokingPatients.size() // Offset by number of cholesterol cards
            );
            timeSeriesViewHolder.timeSeriesCardHeading.setText(cardPatient.getPatientName());
            timeSeriesViewHolder.timeSeriesCardSubheading.setText(cardPatient.getShPatientReference().getFullReference());
            LineChartHelper lineChartHelper = new LineChartHelper(timeSeriesViewHolder.lineChart);
            List<BloodPressureObservation> o = cardPatient.getObservations();
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

    /**
     * The number of possible views there are.
     *
     * @return count of patients
     */
    @Override
    public int getItemCount() {
        return this.cholesterolPatients.size() + this.smokingPatients.size() + this.bloodPressurePatients.size();
    }

    /**
     * Called when the underlying data source changes (e.g. when new patients are observed)
     *
     * @param cholesterolPatients the observed patients to show in this list
     */
    void updateCholesterolPatients(List<ObservedPatient<CholesterolObservation>> cholesterolPatients) {
        this.cholesterolPatients = cholesterolPatients;
    }

    void updateSmokingPatients(List<ObservedPatient<SmokingObservation>> smokingPatients) {
        this.smokingPatients = smokingPatients;
    }

    void updateBloodPressurePatients(List<ObservedPatient<BloodPressureObservation>> bloodPressurePatients) {
        this.bloodPressurePatients = bloodPressurePatients;
    }

    /**
     * Representation of each individual card stored.
     */
    static class SingleValueNumericalViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView numericalCardHeading;
        TextView numericalCardSubheading;
        TextView numericalCardDescription;
        TextView numericalCardValue;
        TextView numericalCardUnit;

        SingleValueNumericalViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.numericalCardHeading = itemView.findViewById(R.id.string_heading);
            this.numericalCardSubheading = itemView.findViewById(R.id.string_subheading);
            this.numericalCardDescription = itemView.findViewById(R.id.numerical_description);
            this.numericalCardValue = itemView.findViewById(R.id.numerical_value);
            this.numericalCardUnit = itemView.findViewById(R.id.numerical_unit);
        }
    }

    static class SingleValueStringViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView stringCardHeading;
        TextView stringCardSubheading;
        TextView stringCardStatus;

        SingleValueStringViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.stringCardHeading = itemView.findViewById(R.id.string_heading);
            this.stringCardSubheading = itemView.findViewById(R.id.string_subheading);
            this.stringCardStatus = itemView.findViewById(R.id.string_status);
        }
    }

    static class TimeSeriesViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView timeSeriesCardHeading;
        TextView timeSeriesCardSubheading;
        LineChart lineChart;

        TimeSeriesViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.timeSeriesCardHeading = itemView.findViewById(R.id.timeseries_heading);
            this.timeSeriesCardSubheading = itemView.findViewById(R.id.timeseries_subheading);
            this.lineChart = itemView.findViewById(R.id.line_chart);
        }
    }
}
