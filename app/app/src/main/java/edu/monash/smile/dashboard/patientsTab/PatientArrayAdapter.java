package edu.monash.smile.dashboard.patientsTab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import edu.monash.smile.R;
import edu.monash.smile.dashboard.PatientsMonitor;
import edu.monash.smile.data.safeheartsModel.ShPatient;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;
import edu.monash.smile.data.safeheartsModel.observation.ObservationType;

public class PatientArrayAdapter extends RecyclerView.Adapter<PatientArrayAdapter.PatientViewHolder> {
    private List<ShPatient> patients;
    private final PatientsMonitor patientsMonitor;

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
     * Displays a patient at a given index.
     */
    @Override
    public void onBindViewHolder(@NonNull PatientArrayAdapter.PatientViewHolder holder, int position) {
        ShPatientReference shPatientReference = patients.get(position).getReference();
        // Display patient name.
        holder.patientName.setText(patients.get(position).getName());

        // Dynamically create chips for each observation type.
        ChipGroup chipGroup = holder.chipGroup;
        ObservationType[] types = ObservationType.values();
        for (int i=0; i < types.length; i ++){
            ObservationType type = types[i];
            Chip chip = new Chip(holder.itemView.getContext());
            setChipOnType(shPatientReference, type, chip, i);
            chipGroup.addView(chip);
        }

        chipGroup.setSingleSelection(false);
    }

    private void setChipOnType(ShPatientReference shPatientReference,
                               ObservationType type,
                               Chip chip,
                               int alternatingIndex) {
        // Set up UI of chip.
        chip.setText(type.toString().toLowerCase().replace("_", " "));
        chip.setClickable(true);
        chip.setCheckable(true);
        if (alternatingIndex % 2 == 0){
            chip.setChipBackgroundColorResource(R.color.colorChip1);
        }else{
            chip.setChipBackgroundColorResource(R.color.colorChip2);
        }

        // Show checked chip if the patient is already monitored for the given type.
        chip.setChecked(patientsMonitor.isPatientMonitored(
                shPatientReference.getId(),
                type
        ));

        // Handles selection of the chip, by delegating to the patient monitor.
        chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                patientsMonitor.monitorPatient(shPatientReference.getId(), type);
            } else {
                patientsMonitor.unmonitorPatient(shPatientReference.getId(), type);
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
        final View itemView;
        final TextView patientName;
        final ChipGroup chipGroup;

        PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.patientName = itemView.findViewById(R.id.patientName);
            this.chipGroup = itemView.findViewById(R.id.chip_group);
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
