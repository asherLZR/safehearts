package edu.monash.smile.dashboard.alertTab;

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

import edu.monash.smile.R;
import edu.monash.smile.dashboard.PatientsMonitor;
import edu.monash.smile.observerPattern.Observer;
import edu.monash.smile.polling.Poll;
import edu.monash.smile.polling.PollCallback;

public class AlertFragment extends Fragment implements Observer, PollCallback {
    private AlertArrayAdapter alertArrayAdapter;
    private AlertController alertController;
    private ProgressBar progressBar;
    private Poll poll;

    public AlertFragment(PatientsMonitor patientsMonitor, Poll poll) {
        this.poll = poll;
        this.alertController = new AlertController(patientsMonitor);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_status, container, false);

        this.progressBar = rootView.findViewById(R.id.loadingWheel);
        this.progressBar.bringToFront();

        this.alertArrayAdapter = new AlertArrayAdapter();
        RecyclerView alertRecyclerView = rootView.findViewById(R.id.alertRecycler);
        alertRecyclerView.setHasFixedSize(true);
        alertRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        alertRecyclerView.setAdapter(this.alertArrayAdapter);

        // Listen to data events
        this.alertController.attach(this);
        this.poll.addCallback(this);

        return rootView;
    }

    @Override
    public void callback() {
        new AlertFragment.ControllerSetUp(this).execute();
    }

    public void handleFragmentSwitched() {
        new AlertFragment.ControllerSetUp(this).execute();
    }

    @Override
    public void update() {
        if (getActivity() == null) {
            return;
        }

        getActivity().runOnUiThread(() -> {
            alertArrayAdapter.updateAlertPatients(
                    alertController.getAlertPatients()
            );
            alertArrayAdapter.notifyDataSetChanged();
        });
    }

    private static class ControllerSetUp extends AsyncTask<Void, Void, Void> {
        private WeakReference<AlertFragment> fragment;

        private ControllerSetUp(AlertFragment fragment) {
            this.fragment = new WeakReference<>(fragment);
        }

        /**
         * In a background thread, start the controller.
         */
        @Override
        protected Void doInBackground(Void... voids) {
            if (fragment != null) {
                final AlertFragment parentFragment = fragment.get();
                parentFragment.alertController.setUp();
            }
            return null;
        }

        /**
         * Show the progress bar
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (fragment != null) {
                final AlertFragment parentFragment = fragment.get();
                parentFragment.progressBar.setVisibility(View.VISIBLE);
            }
        }

        /**
         * Hide the progress bar.
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (fragment != null) {
                final AlertFragment parentFragment = fragment.get();
                parentFragment.progressBar.setVisibility(View.GONE);
            }
        }
    }
}
