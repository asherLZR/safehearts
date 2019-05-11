package edu.monash.smile.dashboard.alertTab;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.monash.smile.R;

class AlertViewHolder extends RecyclerView.ViewHolder {
    TextView patientTextView;

    AlertViewHolder(@NonNull View v) {
        super(v);
        this.patientTextView = v.findViewById(R.id.string_subheading);
    }
}
