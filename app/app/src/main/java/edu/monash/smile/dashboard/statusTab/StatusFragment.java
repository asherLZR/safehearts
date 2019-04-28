package edu.monash.smile.dashboard.statusTab;

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
import java.util.Objects;

import edu.monash.smile.R;
import edu.monash.smile.dashboard.PatientsMonitor;
import edu.monash.smile.data.HealthServiceType;
import edu.monash.smile.observerPattern.Observer;
import edu.monash.smile.polling.Poll;
import edu.monash.smile.polling.PollCallback;


public class StatusFragment extends Fragment implements Observer, PollCallback {
    private StatusCardAdapter statusCardAdapter;
    private PatientObservationController patientObservationController;
    private ProgressBar progressBar;
    private Poll poll;
    private HealthServiceType healthServiceType;

    public StatusFragment(PatientsMonitor patientsMonitor, Poll poll, HealthServiceType healthServiceType) {
        this.poll = poll;
        this.healthServiceType = healthServiceType;
        this.patientObservationController = new PatientObservationController(patientsMonitor, this.healthServiceType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_status, container, false);

        this.progressBar = rootView.findViewById(R.id.loadingWheel);

        // Set up status card list view
        this.statusCardAdapter = new StatusCardAdapter(Objects.requireNonNull(getContext()), new ArrayList<>());
        ListView statusCardListView = rootView.findViewById(R.id.statusCardListView);
        statusCardListView.setAdapter(this.statusCardAdapter);

        // Listen to data events
        this.patientObservationController.attach(this);
        this.poll.addCallback(this);

        return rootView;
    }

    @Override
    public void callback() {
        new ControllerSetUp(this).execute();
    }

    private static class ControllerSetUp extends AsyncTask<Void, Void, Void> {
        private WeakReference<StatusFragment> fragment;

        ControllerSetUp(StatusFragment fragment){
            Log.i("Debug", "StatusFragment: Update");
            this.fragment = new WeakReference<>(fragment);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (fragment != null){
                final StatusFragment parentFragment = fragment.get();
                parentFragment.patientObservationController.setUp();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (fragment != null){
                final StatusFragment parentFragment = fragment.get();
                parentFragment.progressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (fragment != null){
                final StatusFragment parentFragment = fragment.get();
                parentFragment.progressBar.setVisibility(View.GONE);
            }
        }
    }

    public void handleFragmentSwitched() {
        new ControllerSetUp(this).execute();
    }

    @Override
        public void update() {
        if (getActivity() == null) {
            return;
        }

        getActivity().runOnUiThread(() -> {
            statusCardAdapter.updateObservedPatients(
                    patientObservationController.getObservedPatients()
            );
            statusCardAdapter.notifyDataSetInvalidated();
        });
    }
}
