package edu.monash.smile.dashboard.patientsTab;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import edu.monash.smile.R;
import edu.monash.smile.dashboard.PatientsMonitor;
import edu.monash.smile.observerPattern.Observer;
import edu.monash.smile.polling.Poll;
import edu.monash.smile.polling.PollCallback;


public class PatientFragment extends Fragment implements Observer, PollCallback {

    private static final String PRACTITIONER_ID = "practitionerId";

    private int practitionerId;
    private PatientArrayAdapter patientAdapter;
    private PatientsMonitor patientsMonitor;
    private PatientController patientController;
    private Poll poll;

    private ProgressBar progressBar;

    public PatientFragment(PatientsMonitor patientsMonitor, Poll poll) {
        this.patientsMonitor = patientsMonitor;
        this.patientController = new PatientController();
        this.poll = poll;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        if (getArguments().containsKey(PRACTITIONER_ID)) {
            this.practitionerId = getArguments().getInt(PRACTITIONER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_patient, container, false);
        this.progressBar = rootView.findViewById(R.id.loadingWheel);

        // Set up patient list view
        patientAdapter = new PatientArrayAdapter(
                getContext(),
                new ArrayList<>(),
                patientsMonitor
        );
        ListView patientListView = rootView.findViewById(R.id.patientListView);
        patientListView.setAdapter(patientAdapter);

        // Listen to data events
        patientController.attach(this);
        this.poll.addCallback(this);

        return rootView;
    }

    @Override
    public void callback() {
        new PatientFragment.ControllerSetUp(this).execute();
    }

    private static class ControllerSetUp extends AsyncTask<Void, Void, Void> {
        private WeakReference<PatientFragment> fragment;

        ControllerSetUp(PatientFragment fragment){
            this.fragment = new WeakReference<>(fragment);
            Log.i("Debug", "PatientFragment: Update");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (fragment != null){
                final PatientFragment parentFragment = fragment.get();
                parentFragment.patientController.setUp(parentFragment.practitionerId);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (fragment != null){
                final PatientFragment parentFragment = fragment.get();
                parentFragment.progressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (fragment != null){
                final PatientFragment parentFragment = fragment.get();
                parentFragment.progressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void update() {
        if (getActivity() == null) {
            return;
        }

        getActivity().runOnUiThread(() -> {
            patientAdapter.updatePatients(patientController.getShPatients());
            patientAdapter.notifyDataSetInvalidated();
        });
    }
}
