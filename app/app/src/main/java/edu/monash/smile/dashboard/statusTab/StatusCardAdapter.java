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
import edu.monash.smile.data.safeheartsModel.ObservedPatient;
import edu.monash.smile.data.safeheartsModel.QuantitativeObservation;

public class StatusCardAdapter extends ArrayAdapter<ObservedPatient> {
    private List<ObservedPatient> observedPatients;

    /**
     * The status card is a summary of the patient's health, shown in the dashboard.
     * It displays information about the patient's tracked observations.
     */
    StatusCardAdapter(@NonNull Context context, List<ObservedPatient> observedPatients) {
        super(context, 0, observedPatients);
        this.observedPatients = observedPatients;
    }

    /**
     * Called when the underlying data source changes (e.g. when new patients are observed)
     * @param observedPatients the observed patients to show in this list
     */
    void updateObservedPatients(List<ObservedPatient> observedPatients) {
        this.observedPatients = observedPatients;
    }

    /**
     * The number of views to show.
     * @return count of patients
     */
    @Override
    public int getCount() {
        return observedPatients.size();
    }

    /**
     * Displays a status card at a given index with the:
     * - Patient name
     * - Patient ID
     * - Patient observation (description and value)
     */
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
        statusCardHeading.setText(cardPatient.getPatientName());

        TextView patientTextView = statusCardView.findViewById(R.id.patientIdView);
        patientTextView.setText(cardPatient.getShPatientReference().getFullReference());

        TextView statusCardDescription = statusCardView.findViewById(R.id.statusCardDescription);
        statusCardDescription.setText(viewedObservation.getDescription());

        TextView statusCardValue = statusCardView.findViewById(R.id.statusCardValue);
        statusCardValue.setText(viewedObservation.getValue());

        return statusCardView;
    }
}
