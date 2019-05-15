package edu.monash.smile.dashboard.statusTab.adapterDelegates;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.charts.LineChart;
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate;

import java.util.ArrayList;
import java.util.List;

import edu.monash.smile.R;
import edu.monash.smile.charting.ObservationLineChart;
import edu.monash.smile.data.safeheartsModel.observation.BloodPressureObservation;
import edu.monash.smile.data.safeheartsModel.observation.ObservedPatient;
import edu.monash.smile.data.safeheartsModel.observation.QuantitativeObservation;
import edu.monash.smile.data.safeheartsModel.observation.ShObservation;

public class TimeSeriesListItemAdapterDelegate extends AbsListItemAdapterDelegate<ObservedPatient<ShObservation>, ObservedPatient<? extends ShObservation>, TimeSeriesListItemAdapterDelegate.TimeSeriesViewHolder> {
    private LayoutInflater inflater;

    public TimeSeriesListItemAdapterDelegate(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    protected boolean isForViewType(@NonNull ObservedPatient<? extends ShObservation> item, @NonNull List<ObservedPatient<? extends ShObservation>> items, int position) {
        return item.getObservations() != null;
    }

    @NonNull
    @Override
    protected TimeSeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return new TimeSeriesViewHolder(inflater.inflate(R.layout.time_series_card, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ObservedPatient<ShObservation> item, @NonNull TimeSeriesViewHolder holder, @NonNull List<Object> payloads) {
        holder.setUpTitle(
                item.getPatientName(),
                item.getShPatientReference().getFullReference()
        );

        List<? extends ShObservation> observations = item.getObservations();
        List<QuantitativeObservation> systolicObservations = new ArrayList<>();
        List<QuantitativeObservation> diastolicObservations = new ArrayList<>();

        for (int i = 0; i < observations.size(); i++) {
            BloodPressureObservation observation = (BloodPressureObservation) observations.get(i);
            QuantitativeObservation systolic = observation.getSystolicObservation();
            QuantitativeObservation diastolic = observation.getDiastolicObservation();
            // If at any point in time, the patient exceeds normal thresholds, display an alert
            if (systolic.getValue().intValue() > 180 || diastolic.getValue().intValue() > 120) {
                holder.showAlert();
            } else {
                holder.hideAlert();
            }

            systolicObservations.add(systolic);
            diastolicObservations.add(diastolic);
        }

        ObservationLineChart observationLineChart = new ObservationLineChart(holder.getLineChartView());

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
