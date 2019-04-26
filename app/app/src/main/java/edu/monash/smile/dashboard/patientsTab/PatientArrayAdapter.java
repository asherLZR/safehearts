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

    PatientArrayAdapter(
            Context context,
            List<ShPatient> patients,
            PatientsMonitor patientsMonitor
    ) {
        super(context, 0, patients);
        this.patients = new ArrayList<>();
        this.patientsMonitor = patientsMonitor;
    }

    void updatePatients(List<ShPatient> patients) {
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
