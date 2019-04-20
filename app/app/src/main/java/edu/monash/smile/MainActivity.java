package edu.monash.smile;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import edu.monash.smile.observerPattern.Observer;

public class MainActivity extends AppCompatActivity implements Observer {
    private static final String TAG = "MainActivity";
    private final PatientController controller = new PatientController();
    private PatientArrayAdapter patientAdapter;
    private StatusCardAdapter statusCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Set up toolbar view
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up patient list view
        patientAdapter = new PatientArrayAdapter(this, new ArrayList<>());
        ListView patientListView = findViewById(R.id.patientListView);
        patientListView.setAdapter(patientAdapter);

        // Set up status card list view
        statusCardAdapter = new StatusCardAdapter(this, new ArrayList<>());
        ListView statusCardListView = findViewById(R.id.statusCardListView);
        statusCardListView.setAdapter(statusCardAdapter);

        // Listen to data events
        controller.attach(this);
        controller.setUp(3252); // TODO: Pass in practitionerId from the home screen activity
    }

    @Override
    public void update() {
        runOnUiThread(() -> {
            patientAdapter.updatePatients(controller.getPatientReferences());
            patientAdapter.notifyDataSetInvalidated();

            statusCardAdapter.updateObservedPatients(
                    controller.getObservedPatients()
            );
            statusCardAdapter.notifyDataSetInvalidated();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.detach(this);
    }
}
