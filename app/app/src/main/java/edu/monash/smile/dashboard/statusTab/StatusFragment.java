package edu.monash.smile.dashboard.statusTab;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.Objects;

import edu.monash.smile.R;
import edu.monash.smile.dashboard.PatientsMonitor;
import edu.monash.smile.observerPattern.Observer;
import edu.monash.smile.polling.Poll;
import edu.monash.smile.polling.PollCallback;


public class StatusFragment extends Fragment implements Observer, PollCallback {
    private StatusCardAdapter statusCardAdapter;
    private PatientObservationController patientObservationController;
    private ProgressBar progressBar;
    private Poll poll;

    public StatusFragment(PatientsMonitor patientsMonitor, Poll poll) {
        this.poll = poll;
        this.patientObservationController = new PatientObservationController(patientsMonitor);
    }

    /**
     * Creates the view showing the list of status cards, and listens to new events.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_status, container, false);

        this.progressBar = rootView.findViewById(R.id.loadingWheel);
        this.progressBar.bringToFront();

        // Set up status card view
        this.statusCardAdapter = new StatusCardAdapter(this.getActivity());
        RecyclerView statusRecycler = rootView.findViewById(R.id.statusRecycler);
        statusRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Objects.requireNonNull(this.getActivity()));
        statusRecycler.setLayoutManager(layoutManager);
        statusRecycler.setAdapter(this.statusCardAdapter);

        // Listen to data events
        this.patientObservationController.attach(this);
        this.poll.addCallback(this);

        return rootView;
    }

    /**
     * Called when the poll notifies of new data. This method will refresh the controller as the
     * data has changed.
     */
    @Override
    public void callback() {
        new ControllerSetUp(this).execute();
    }

    /**
     * Displays a loading screen while data is asynchronous fetched in the background when the
     * controller is set up.
     */
    private static class ControllerSetUp extends AsyncTask<Void, Void, Void> {
        private WeakReference<StatusFragment> fragment;

        private ControllerSetUp(StatusFragment fragment){
            this.fragment = new WeakReference<>(fragment);
        }

        /**
         * In a background thread, start the controller.
         */
        @Override
        protected Void doInBackground(Void... voids) {
            if (fragment != null){
                final StatusFragment parentFragment = fragment.get();
                parentFragment.patientObservationController.setUp();
            }
            return null;
        }

        /**
         * Show the progress bar
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (fragment != null){
                final StatusFragment parentFragment = fragment.get();
                parentFragment.progressBar.setVisibility(View.VISIBLE);
            }
        }

        /**
         * Hide the progress bar.
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (fragment != null){
                final StatusFragment parentFragment = fragment.get();
                parentFragment.progressBar.setVisibility(View.GONE);
            }
        }
    }

    /**
     * To be used when the focus is returned to this fragment, as the patients which are observed
     * could be changed in other screens of the app.
     */
    public void handleFragmentSwitched() {
        new ControllerSetUp(this).execute();
    }

    /**
     * Observed changes to the data requires that the list shown on this screen is refreshed with
     * new data.
     */
    @Override
        public void update() {
        if (getActivity() == null) {
            return;
        }

        getActivity().runOnUiThread(() -> {
            statusCardAdapter.updateAllMonitoredPatients(patientObservationController.getAllObservationCollections());

            statusCardAdapter.notifyDataSetChanged();
        });
    }
}
