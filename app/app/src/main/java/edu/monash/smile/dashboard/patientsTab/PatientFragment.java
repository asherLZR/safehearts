package edu.monash.smile.dashboard.patientsTab;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import edu.monash.smile.MainActivity;
import edu.monash.smile.R;
import edu.monash.smile.dashboard.PatientsMonitor;
import edu.monash.smile.observerPattern.Observer;
import edu.monash.smile.polling.Poll;
import edu.monash.smile.polling.PollCallback;


public class PatientFragment extends Fragment implements Observer, PollCallback {
    private int practitionerId;
    private PatientArrayAdapter patientAdapter;
    private PatientController patientController;
    private Poll poll;

    private ProgressBar progressBar;

    /**
     * Displays the patient details.
     * @param patientsMonitor The controller that handles selection of patients and their observation types
     * @param poll The poll that handles new data notifications
     * @param context The Android context
     */
    public PatientFragment(
            PatientsMonitor patientsMonitor,
            Poll poll,
            Context context
    ) {
        this.poll = poll;
        // Set up patient list view
        this.patientAdapter = new PatientArrayAdapter(
                context,
                new ArrayList<>(),
                patientsMonitor
        );
        this.patientController = new PatientController();
    }

    /**
     * Restores the selected practitioner from the parent activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        if (getArguments().containsKey(MainActivity.BUNDLE_PRACTITIONER_ID)) {
            this.practitionerId = getArguments().getInt(MainActivity.BUNDLE_PRACTITIONER_ID);
        }
    }

    /**
     * Creates the view showing the list of patients, and listens to new events.
     */
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_patient, container, false);
        this.progressBar = rootView.findViewById(R.id.loadingWheel);

        ListView patientListView = rootView.findViewById(R.id.patientListView);
        patientListView.setAdapter(patientAdapter);

        // Listen to data events
        patientController.attach(this);
        this.poll.addCallback(this);

        return rootView;
    }

    /**
     * Called when the poll notifies of new data. This method will refresh the controller as the
     * data has changed.
     */
    @Override
    public void callback() {
        new PatientFragment.ControllerSetUp(this).execute();
    }

    /**
     * Displays a loading screen while data is asynchronous fetched in the background when the
     * controller is set up.
     */
    private static class ControllerSetUp extends AsyncTask<Void, Void, Void> {
        private WeakReference<PatientFragment> fragment;

        private ControllerSetUp(PatientFragment fragment){
            this.fragment = new WeakReference<>(fragment);
        }

        /**
         * In a background thread, start the controller.
         */
        @Override
        protected Void doInBackground(Void... voids) {
            if (fragment != null){
                final PatientFragment parentFragment = fragment.get();
                parentFragment.patientController.setUp(parentFragment.practitionerId);
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
                final PatientFragment parentFragment = fragment.get();
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
                final PatientFragment parentFragment = fragment.get();
                parentFragment.progressBar.setVisibility(View.GONE);
            }
        }
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
            patientAdapter.updatePatients(patientController.getShPatients());
            patientAdapter.notifyDataSetChanged();
        });
    }
}
