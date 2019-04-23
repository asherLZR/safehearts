package edu.monash.smile.dashboard.statusTab;

import android.os.AsyncTask;
import android.os.Bundle;
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
import edu.monash.smile.observerPattern.Observer;


public class StatusFragment extends Fragment implements Observer {
    private StatusCardAdapter statusCardAdapter;
    private PatientObservationController patientObservationController;
    private ProgressBar progressBar;

    public StatusFragment(PatientsMonitor patientsMonitor) {
        this.patientObservationController = new PatientObservationController(patientsMonitor);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_status, container, false);

        this.progressBar = rootView.findViewById(R.id.loadingWheel);

        // Set up status card list view
        statusCardAdapter = new StatusCardAdapter(Objects.requireNonNull(getContext()), new ArrayList<>());
        ListView statusCardListView = rootView.findViewById(R.id.statusCardListView);
        statusCardListView.setAdapter(statusCardAdapter);

        // Listen to data events
        patientObservationController.attach(this);
        new ControllerSetUp(this).execute();

        return rootView;
    }

    private static class ControllerSetUp extends AsyncTask<Void, Void, Void> {
        private WeakReference<StatusFragment> fragment;

        ControllerSetUp(StatusFragment fragment){
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
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            statusCardAdapter.updateObservedPatients(
                patientObservationController.getObservedPatients()
            );
            statusCardAdapter.notifyDataSetInvalidated();
        });
    }
}
