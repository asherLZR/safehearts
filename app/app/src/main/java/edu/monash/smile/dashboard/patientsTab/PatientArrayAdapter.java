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
import edu.monash.smile.data.safeheartsModel.ObservationType;
import edu.monash.smile.data.safeheartsModel.ShPatientReference;

public class PatientArrayAdapter extends ArrayAdapter<ShPatientReference> {
    private List<ShPatientReference> patients;
    private PatientsMonitor patientsMonitor;

    PatientArrayAdapter(
            Context context,
            List<ShPatientReference> patients,
            PatientsMonitor patientsMonitor
    ) {
        super(context, 0, patients);
        this.patients = patients;
        this.patientsMonitor = patientsMonitor;
    }

    void updatePatients(List<ShPatientReference> patients) {
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

        ShPatientReference shPatientReference = patients.get(position);

        // Display patient name
        TextView patientName = patientListItem.findViewById(R.id.patientName);
        patientName.setText(shPatientReference.getId());

        // Display patient filter for cholesterol
        Chip cholesterolChip = patientListItem.findViewById(R.id.cholesterolChip);
        cholesterolChip.setChecked(patientsMonitor.isPatientMonitored(
                shPatientReference.getId(),
                ObservationType.cholesterol
        ));
        cholesterolChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                patientsMonitor.monitorPatient(shPatientReference.getId(), ObservationType.cholesterol);
            } else {
                patientsMonitor.unmonitorPatient(shPatientReference.getId(), ObservationType.cholesterol);
            }
        });

        return patientListItem;
    }
}
