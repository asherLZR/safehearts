package edu.monash.smile.dashboard.alertTab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.monash.smile.R;
import edu.monash.smile.data.safeheartsModel.AlertPatient;

public class AlertArrayAdapter extends RecyclerView.Adapter<AlertViewHolder> {
    private List<AlertPatient> alertPatients;

    AlertArrayAdapter() {
        this.alertPatients = new ArrayList<>();
    }

    @NonNull
    @Override
    public AlertViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.alert_card, viewGroup, false);
        return new AlertViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertViewHolder holder, int position) {
        AlertPatient cardPatient = alertPatients.get(position);
        holder.patientTextView.setText(cardPatient.getPatientName());
    }

    @Override
    public int getItemCount() {
        return this.alertPatients.size();
    }

    void updateAlertPatients(List<AlertPatient> alertPatients) {
        this.alertPatients = alertPatients;
    }
}
