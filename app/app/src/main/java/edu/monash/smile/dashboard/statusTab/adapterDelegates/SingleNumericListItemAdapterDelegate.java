package edu.monash.smile.dashboard.statusTab.adapterDelegates;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate;

import java.util.List;

import edu.monash.smile.R;
import edu.monash.smile.dashboard.statusTab.ObservedPatient;
import edu.monash.smile.data.safeheartsModel.observation.QuantitativeObservation;
import edu.monash.smile.data.safeheartsModel.observation.ShObservation;

public class SingleNumericListItemAdapterDelegate extends AbsListItemAdapterDelegate<ObservedPatient<QuantitativeObservation>, ObservedPatient<? extends ShObservation>, SingleNumericListItemAdapterDelegate.SingleValueNumericalViewHolder> {
    private LayoutInflater inflater;

    public SingleNumericListItemAdapterDelegate(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    protected boolean isForViewType(@NonNull ObservedPatient<? extends ShObservation> item, @NonNull List<ObservedPatient<? extends ShObservation>> items, int position) {
        return item.getObservations().get(0) instanceof QuantitativeObservation;
    }

    @NonNull
    @Override
    protected SingleValueNumericalViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return new SingleValueNumericalViewHolder(inflater.inflate(R.layout.single_value_numerical_card, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ObservedPatient<QuantitativeObservation> item, @NonNull SingleValueNumericalViewHolder holder, @NonNull List<Object> payloads) {
        holder.setUpTitle(
                item.getPatientName(),
                item.getShPatientReference().getFullReference()
        );

        QuantitativeObservation observation = item.getObservations().get(0);

        holder.setUpView(
                observation.getDescription(),
                observation.getValue().toPlainString(),
                observation.getUnit()
        );
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

}
