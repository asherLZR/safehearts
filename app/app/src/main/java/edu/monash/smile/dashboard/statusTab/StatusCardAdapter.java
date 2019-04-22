package edu.monash.smile.dashboard.statusTab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import edu.monash.smile.R;
import edu.monash.smile.data.model.ObservedPatient;
import edu.monash.smile.data.model.QuantitativeObservation;

public class StatusCardAdapter extends ArrayAdapter<ObservedPatient> {
    private List<ObservedPatient> observedPatients;

    StatusCardAdapter(@NonNull Context context, List<ObservedPatient> observedPatients) {
        super(context, 0, observedPatients);
        this.observedPatients = observedPatients;
    }

    void updateObservedPatients(List<ObservedPatient> observedPatients) {
        this.observedPatients = observedPatients;
    }

    @Override
    public int getCount() {
        return observedPatients.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View statusCardView = convertView;

        if (statusCardView == null) {
            statusCardView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_status_card, parent, false
            );
        }

        ObservedPatient cardPatient = observedPatients.get(position);

        QuantitativeObservation viewedObservation = cardPatient.getObservations().get(0);

        TextView statusCardHeading = statusCardView.findViewById(R.id.statusCardHeading);
        statusCardHeading.setText(cardPatient.getPatientReference().getId());

        TextView statusCardDescription = statusCardView.findViewById(R.id.statusCardDescription);
        statusCardDescription.setText(viewedObservation.getDescription());

        TextView statusCardValue = statusCardView.findViewById(R.id.statusCardValue);
        statusCardValue.setText(viewedObservation.getValue());

        return statusCardView;
    }
}
