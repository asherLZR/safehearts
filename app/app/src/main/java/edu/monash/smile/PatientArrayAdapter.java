package edu.monash.smile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import edu.monash.smile.data.model.PatientReference;

public class PatientArrayAdapter extends ArrayAdapter<PatientReference> {
    private List<PatientReference> patients;

    PatientArrayAdapter(@NonNull Context context, List<PatientReference> patients) {
        super(context, 0, patients);
        this.patients = patients;
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
                    R.layout.patient_row, parent, false
            );
        }

        PatientReference patientReference = patients.get(position);
        TextView patientName = patientListItem.findViewById(R.id.patientName);
        patientName.setText(patientReference.getId());
        return patientListItem;
    }
}
