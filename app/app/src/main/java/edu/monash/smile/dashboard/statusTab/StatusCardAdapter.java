package edu.monash.smile.dashboard.statusTab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.monash.smile.R;
import edu.monash.smile.data.safeheartsModel.observation.CholesterolObservation;
import edu.monash.smile.data.safeheartsModel.observation.ObservedPatient;
import edu.monash.smile.data.safeheartsModel.observation.SmokingObservation;

public class StatusCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static int CHOLESTEROL_VIEW = 0;
    private static int SMOKING_VIEW = 1;
    private List<ObservedPatient<CholesterolObservation>> cholesterolPatients;
    private List<ObservedPatient<SmokingObservation>> smokingPatients;

    /**
     * The status card is a summary of the patient's health, shown in the dashboard.
     * It displays information about the patient's tracked observations.
     */
    StatusCardAdapter() {
        this.cholesterolPatients = new ArrayList<>();
        this.smokingPatients = new ArrayList<>();
    }

    /**
     * Inflates the card layout for the ViewHolder.
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == CHOLESTEROL_VIEW) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cholesterol_card, viewGroup, false); //CardView inflated as RecyclerView list item
            return new CholesterolViewHolder(v);
        } else if (viewType == SMOKING_VIEW) {
            View v = LayoutInflater.from(
                    viewGroup.getContext()).inflate(R.layout.smoking_card,
                    viewGroup,
                    false
            );

            return new SmokingViewHolder(v);
        }

        throw new IllegalArgumentException("Unhandled view type: " + viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < cholesterolPatients.size()) {
            return CHOLESTEROL_VIEW;
        }

        return SMOKING_VIEW;
    }

    /**
     * Displays a status card at a given index with the:
     * - Patient name
     * - Patient ID
     * - Patient observation (description and value)
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CholesterolViewHolder) {
            CholesterolViewHolder cholesterolHolder = (CholesterolViewHolder) holder;
            ObservedPatient<CholesterolObservation> cardPatient = cholesterolPatients.get(position);
            CholesterolObservation viewedObservation = cardPatient.getObservations().get(0);
            cholesterolHolder.statusCardHeading.setText(cardPatient.getPatientName());
            cholesterolHolder.patientTextView.setText(cardPatient.getShPatientReference().getFullReference());
            cholesterolHolder.cholesterolCardDescription.setText(viewedObservation.getDescription());
            cholesterolHolder.cholesterolCardValue.setText(viewedObservation.getValue().toPlainString());
            cholesterolHolder.unitTextView.setText(viewedObservation.getUnit());
        } else if (holder instanceof SmokingViewHolder) {
            SmokingViewHolder smokingHolder = (SmokingViewHolder) holder;
            ObservedPatient<SmokingObservation> cardPatient = smokingPatients.get(
                    position - cholesterolPatients.size() // Offset by number of cholesterol cards
            );
            smokingHolder.smokingCardHeading.setText(cardPatient.getPatientName());
            smokingHolder.smokingStatus.setText(cardPatient.getObservations().get(0).getSmokingStatus());
        }
    }

    /**
     * The number of possible views there are.
     *
     * @return count of patients
     */
    @Override
    public int getItemCount() {
        return this.cholesterolPatients.size() + this.smokingPatients.size();
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

    /**
     * Representation of each individual card stored.
     */
    static class CholesterolViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView statusCardHeading;
        TextView patientTextView;
        TextView cholesterolCardDescription;
        TextView cholesterolCardValue;
        TextView unitTextView;

        CholesterolViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.statusCardHeading = itemView.findViewById(R.id.cholesterolCardHeading);
            this.patientTextView = itemView.findViewById(R.id.patientIdView);
            this.cholesterolCardDescription = itemView.findViewById(R.id.cholesterolCardDescription);
            this.cholesterolCardValue = itemView.findViewById(R.id.cholesterolCardValue);
            this.unitTextView = itemView.findViewById(R.id.unitView);
        }
    }

    static class SmokingViewHolder extends RecyclerView.ViewHolder {
        TextView smokingCardHeading;
        TextView smokingStatus;

        SmokingViewHolder(@NonNull View itemView) {
            super(itemView);
            this.smokingCardHeading = itemView.findViewById(R.id.smokingCardHeading);
            this.smokingStatus = itemView.findViewById(R.id.smokingStatus);
        }
    }
}
