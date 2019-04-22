package edu.monash.smile.dashboard.statusTab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Objects;

import edu.monash.smile.R;
import edu.monash.smile.dashboard.PatientsMonitor;
import edu.monash.smile.observerPattern.Observer;


public class StatusFragment extends Fragment implements Observer {
    private StatusCardAdapter statusCardAdapter;
    private PatientObservationController patientObservationController;

    public StatusFragment(PatientsMonitor patientsMonitor) {
        this.patientObservationController = new PatientObservationController(patientsMonitor);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_status, container, false);
        // Set up status card list view
        statusCardAdapter = new StatusCardAdapter(Objects.requireNonNull(getContext()), new ArrayList<>());
        ListView statusCardListView = rootView.findViewById(R.id.statusCardListView);
        statusCardListView.setAdapter(statusCardAdapter);

        // Listen to data events
        patientObservationController.attach(this);
        patientObservationController.setUp();
        return rootView;
    }

    public void handleFragmentSwitched() {
        patientObservationController.setUp();
    }

    @Override
    public void update() {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            statusCardAdapter.updateObservedPatients(
                patientObservationController.getObservedPatients()
            );
            statusCardAdapter.notifyDataSetInvalidated();
        });
    }
}
