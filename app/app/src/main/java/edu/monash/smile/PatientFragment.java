package edu.monash.smile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Objects;

import edu.monash.smile.observerPattern.Observer;


public class PatientFragment extends Fragment implements Observer {

    public static final String PRACTITIONER_ID = "practitionerId";

    private int practitionerId;
    private PatientArrayAdapter patientAdapter;
    private PatientsMonitor patientsMonitor;
    private PatientController patientController = new PatientController();

    public PatientFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        if (getArguments().containsKey(PRACTITIONER_ID)) {
            practitionerId = getArguments().getInt(PRACTITIONER_ID);
        }
        // Listen to data events
        patientController.attach(this);
        patientController.setUp(practitionerId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_patient, container, false);

        // Set up patients monitor
        patientsMonitor = new PatientsMonitor(getContext());

//        // Set up patient list view
        patientAdapter = new PatientArrayAdapter(Objects.requireNonNull(getContext()), new ArrayList<>(), patientsMonitor);
        ListView patientListView = rootView.findViewById(R.id.patientListView);
        patientListView.setAdapter(patientAdapter);

        return rootView;
    }

    @Override
    public void update() {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            patientAdapter.updatePatients(patientController.getPatientReferences());
            patientAdapter.notifyDataSetInvalidated();
        });
    }
}
