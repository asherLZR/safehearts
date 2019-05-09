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
import edu.monash.smile.data.safeheartsModel.observation.ObservedPatient;
import edu.monash.smile.data.safeheartsModel.observation.CholesterolObservation;

public class StatusCardAdapter extends RecyclerView.Adapter<StatusCardAdapter.StatusViewHolder>{
    private List<ObservedPatient> observedPatients;

    /**
     * The status card is a summary of the patient's health, shown in the dashboard.
     * It displays information about the patient's tracked observations.
     */
    StatusCardAdapter(){
        this.observedPatients = new ArrayList<>();
    }

    /**
     * Inflates the card layout for the ViewHolder.
     */
    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.status_card, viewGroup, false); //CardView inflated as RecyclerView list item
        return new StatusViewHolder(v);
    }

    /**
     * Displays a status card at a given index with the:
     * - Patient name
     * - Patient ID
     * - Patient observation (description and value)
     */
    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
        ObservedPatient cardPatient = observedPatients.get(position);
        CholesterolObservation viewedObservation = cardPatient.getObservations().get(0);
        holder.statusCardHeading.setText(cardPatient.getPatientName());
        holder.patientTextView.setText(cardPatient.getShPatientReference().getFullReference());
        holder.statusCardDescription.setText(viewedObservation.getDescription());
        holder.statusCardValue.setText(viewedObservation.getValue().toPlainString());
        holder.unitTextView.setText(viewedObservation.getUnit());
    }

    /**
     * The number of possible views there are.
     * @return count of patients
     */
    @Override
    public int getItemCount() {
        return this.observedPatients.size();
    }

    /**
     * Representation of each individual card stored.
     */
    static class StatusViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        TextView statusCardHeading;
        TextView patientTextView;
        TextView statusCardDescription;
        TextView statusCardValue;
        TextView unitTextView;

        StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.statusCardHeading = itemView.findViewById(R.id.statusCardHeading);
            this.patientTextView = itemView.findViewById(R.id.patientIdView);
            this.statusCardDescription = itemView.findViewById(R.id.statusCardDescription);
            this.statusCardValue = itemView.findViewById(R.id.statusCardValue);
            this.unitTextView = itemView.findViewById(R.id.unitView);
        }
    }

    /**
     * Called when the underlying data source changes (e.g. when new patients are observed)
     * @param observedPatients the observed patients to show in this list
     */
    void updateObservedPatients(List<ObservedPatient> observedPatients) {
        this.observedPatients = observedPatients;
    }
}
