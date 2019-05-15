package edu.monash.smile.dashboard.statusTab.adapterDelegates;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate;

import java.util.List;

import edu.monash.smile.R;
import edu.monash.smile.data.safeheartsModel.observation.ObservedPatient;
import edu.monash.smile.data.safeheartsModel.observation.ShObservation;
import edu.monash.smile.data.safeheartsModel.observation.StatusObservation;

public class SingleStatusListItemAdapterDelegate extends AbsListItemAdapterDelegate<ObservedPatient<StatusObservation>, ObservedPatient<? extends ShObservation>, SingleStatusListItemAdapterDelegate.SingleValueStringViewHolder> {
    private LayoutInflater inflater;

    public SingleStatusListItemAdapterDelegate(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    protected boolean isForViewType(@NonNull ObservedPatient<? extends ShObservation> item, @NonNull List<ObservedPatient<? extends ShObservation>> items, int position) {
        return item.getObservations().get(0) instanceof StatusObservation;
    }

    @NonNull
    @Override
    protected SingleValueStringViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return new SingleValueStringViewHolder(inflater.inflate(R.layout.single_value_string_card, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ObservedPatient<StatusObservation> item, @NonNull SingleValueStringViewHolder holder, @NonNull List<Object> payloads) {
        holder.setUpTitle(
                item.getPatientName(),
                item.getShPatientReference().getFullReference()
        );

        holder.setUpView(
                item.getObservations().get(0).getStatus()
        );
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

}
