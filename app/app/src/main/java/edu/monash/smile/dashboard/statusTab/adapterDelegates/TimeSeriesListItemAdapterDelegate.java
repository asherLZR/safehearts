package edu.monash.smile.dashboard.statusTab.adapterDelegates;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.charts.LineChart;
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate;

import java.util.List;

import edu.monash.smile.R;
import edu.monash.smile.dashboard.statusTab.observedPatient.ObservedPatient;
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
        if (item.alertIf(item.getObservations())){
            holder.showAlert();
        }else{
            holder.hideAlert();
        }
        item.chart(holder, item.getObservations());
    }

    /**
     * Representation of each individual card stored. This deals specifically with displaying
     * data that may be represented on a continuous scale.
     */
    public static class TimeSeriesViewHolder extends BaseCardViewHolder {
        private com.github.mikephil.charting.charts.LineChart lineChart;
        private ImageView alert;

        TimeSeriesViewHolder(@NonNull View itemView) {
            super(itemView, R.id.timeseries_heading, R.id.timeseries_subheading);
            this.lineChart = itemView.findViewById(R.id.line_chart);
            this.alert = itemView.findViewById(R.id.alertImg);
        }

        public LineChart getLineChartView() {
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
