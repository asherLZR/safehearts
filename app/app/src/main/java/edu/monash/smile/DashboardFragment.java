package edu.monash.smile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Objects;

import edu.monash.smile.observerPattern.Observer;


public class DashboardFragment extends Fragment implements Observer {

    public static final String PRACTITIONER_ID = "practitionerId";

    private int practitionerId;
    private StatusCardAdapter statusCardAdapter;
    public static PatientObservationController patientObservationController;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        if (getArguments().containsKey(PRACTITIONER_ID)) {
            practitionerId = getArguments().getInt(PRACTITIONER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        // Set up status card list view
        statusCardAdapter = new StatusCardAdapter(Objects.requireNonNull(getContext()), new ArrayList<>());
        ListView statusCardListView = rootView.findViewById(R.id.statusCardListView);
        statusCardListView.setAdapter(statusCardAdapter);

        // Listen to data events
        patientObservationController = new PatientObservationController(new PatientsMonitor(getContext()));
        patientObservationController.attach(this);
        patientObservationController.setUp();
        return rootView;
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
