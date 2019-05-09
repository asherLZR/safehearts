package edu.monash.smile.dashboard.patientsTab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import edu.monash.smile.R;
import edu.monash.smile.dashboard.PatientsMonitor;
import edu.monash.smile.data.safeheartsModel.ShPatient;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;
import edu.monash.smile.data.safeheartsModel.observation.ObservationType;

public class PatientArrayAdapter extends RecyclerView.Adapter<PatientArrayAdapter.PatientViewHolder> {
    private List<ShPatient> patients;
    private PatientsMonitor patientsMonitor;

    /**
     * Handles view to display a patient details, as well as toggles to track/untrack a patient by its type
     * @param patientsMonitor controller to handle selection of patients
     */
    PatientArrayAdapter(PatientsMonitor patientsMonitor){
        this.patients = new ArrayList<>();
        this.patientsMonitor = patientsMonitor;
    }

    /**
     * Inflates the card layout for the ViewHolder.
     */
    @NonNull
    @Override
    public PatientArrayAdapter.PatientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.patient_card, viewGroup, false);
        return new PatientViewHolder(v);
    }

    /**
     * Displays a patient at a given index
     */
    @Override
    public void onBindViewHolder(@NonNull PatientArrayAdapter.PatientViewHolder holder, int position) {
        ShPatientReference shPatientReference = patients.get(position).getReference();
        // Display patient name
        holder.patientName.setText(patients.get(position).getName());
        // Display patient filter for CHOLESTEROL
        holder.cholesterolChip.setChecked(patientsMonitor.isPatientMonitored(
                shPatientReference.getId(),
                ObservationType.CHOLESTEROL
        ));

        // Handles selection of the chip, by delegating to the patients' monitor
        holder.cholesterolChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                patientsMonitor.monitorPatient(shPatientReference.getId(), ObservationType.CHOLESTEROL);
            } else {
                patientsMonitor.unmonitorPatient(shPatientReference.getId(), ObservationType.CHOLESTEROL);
            }
        });

        // Display patient filter for SMOKING
        holder.smokingChip.setChecked(patientsMonitor.isPatientMonitored(
                shPatientReference.getId(),
                ObservationType.SMOKING
        ));
        holder.smokingChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                patientsMonitor.monitorPatient(shPatientReference.getId(), ObservationType.SMOKING);
            } else {
                patientsMonitor.unmonitorPatient(shPatientReference.getId(), ObservationType.SMOKING);
            }
        });
    }

    /**
     * The number of possible views there are.
     * @return count of patients
     */
    @Override
    public int getItemCount() {
        return this.patients.size();
    }

    /**
     * Representation of each individual card stored.
     */
    static class PatientViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        TextView patientName;
        Chip cholesterolChip;
        Chip smokingChip;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.patientName = itemView.findViewById(R.id.patientName);
            this.cholesterolChip = itemView.findViewById(R.id.cholesterolChip);
            this.smokingChip = itemView.findViewById(R.id.smokingChip);
        }
    }


    /**
     * Called when the underlying data source changes (e.g. when new patients arrive)
     * @param patients the patients to show in this list
     */
    void updatePatients(List<ShPatient> patients) {
        this.patients = patients;
    }
}
