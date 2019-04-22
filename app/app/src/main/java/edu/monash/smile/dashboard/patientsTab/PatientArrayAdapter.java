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

import java.util.List;

import edu.monash.smile.R;
import edu.monash.smile.dashboard.PatientsMonitor;
import edu.monash.smile.data.model.ObservationType;
import edu.monash.smile.data.model.PatientReference;

public class PatientArrayAdapter extends ArrayAdapter<PatientReference> {
    private List<PatientReference> patients;
    private PatientsMonitor patientsMonitor;

    PatientArrayAdapter(
            Context context,
            List<PatientReference> patients,
            PatientsMonitor patientsMonitor
    ) {
        super(context, 0, patients);
        this.patients = patients;
        this.patientsMonitor = patientsMonitor;
    }

    void updatePatients(List<PatientReference> patients) {
        this.patients = patients;
    }

    @Override
    public int getCount() {
        return patients.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View patientListItem = convertView;

        if (patientListItem == null) {
            patientListItem = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_patient_row, parent, false
            );
        }

        PatientReference patientReference = patients.get(position);

        // Display patient name
        TextView patientName = patientListItem.findViewById(R.id.patientName);
        patientName.setText(patientReference.getId());

        // Display patient filter for cholesterol
        Chip cholesterolChip = patientListItem.findViewById(R.id.cholesterolChip);
        cholesterolChip.setChecked(patientsMonitor.isPatientMonitored(
                patientReference.getId(),
                ObservationType.cholesterol
        ));
        cholesterolChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                patientsMonitor.monitorPatient(patientReference.getId(), ObservationType.cholesterol);
            } else {
                patientsMonitor.unmonitorPatient(patientReference.getId(), ObservationType.cholesterol);
            }
        });

        return patientListItem;
    }
}
