package edu.monash.smile.dashboard.patientsTab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import edu.monash.smile.R;
import edu.monash.smile.dashboard.PatientsMonitor;
import edu.monash.smile.data.safeheartsModel.ObservationType;
import edu.monash.smile.data.safeheartsModel.ShPatient;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;

public class PatientArrayAdapter extends ArrayAdapter<ShPatient> {
    private List<ShPatient> patients;
    private PatientsMonitor patientsMonitor;

    /**
     * Handles view to display a patient details, as well as toggles to track/untrack a patient by its type
     * @param context the android context
     * @param patients the patients to show in this list
     * @param patientsMonitor controller to handle selection of patients
     */
    PatientArrayAdapter(
            Context context,
            List<ShPatient> patients,
            PatientsMonitor patientsMonitor
    ) {
        super(context, 0, patients);
        this.patients = new ArrayList<>();
        this.patientsMonitor = patientsMonitor;
    }

    /**
     * Called when the underlying data source changes (e.g. when new patients arrive)
     * @param patients the patients to show in this list
     */
    void updatePatients(List<ShPatient> patients) {
        this.patients = patients;
    }

    /**
     * The number of views to show.
     * @return count of patients
     */
    @Override
    public int getCount() {
        return patients.size();
    }

    /**
     * Displays a patient at a given index
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View patientListItem = convertView;

        if (patientListItem == null) {
            patientListItem = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_patient_row, parent, false
            );
        }

        ShPatientReference shPatientReference = patients.get(position).getReference();

        // Display patient name
        TextView patientName = patientListItem.findViewById(R.id.patientName);
        patientName.setText(patients.get(position).getName());

        // Display patient filter for CHOLESTEROL
        Chip cholesterolChip = patientListItem.findViewById(R.id.cholesterolChip);
        cholesterolChip.setChecked(patientsMonitor.isPatientMonitored(
                shPatientReference.getId(),
                ObservationType.CHOLESTEROL
        ));

        // Handles selection of the chip, by delegating to the patients' monitor
        cholesterolChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                patientsMonitor.monitorPatient(shPatientReference.getId(), ObservationType.CHOLESTEROL);
            } else {
                patientsMonitor.unmonitorPatient(shPatientReference.getId(), ObservationType.CHOLESTEROL);
            }
        });

        return patientListItem;
    }
}
