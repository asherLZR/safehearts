package edu.monash.smile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import edu.monash.smile.observerPattern.Observer;

public class DashboardActivity extends AppCompatActivity implements Observer {
    private static final String TAG = "MainActivity";
    private final PatientController controller = new PatientController();
    private PatientArrayAdapter patientAdapter;
    private StatusCardAdapter statusCardAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Bundle b = getIntent().getExtras();
        assert b != null;
        int practitionerId = b.getInt("practitionerId");

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
        controller.setUp(practitionerId); // TODO: Pass in practitionerId from the home screen activity
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
}
