package edu.monash.smile;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import edu.monash.smile.data.model.PatientReference;
import edu.monash.smile.observerPattern.Observer;

public class MainActivity extends AppCompatActivity implements Observer {
    private static final String TAG = "MainActivity";
    private final PatientController controller = new PatientController();
    private PatientArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Set up toolbar view
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up patient list view
        adapter = new PatientArrayAdapter(this, new ArrayList<>());
        ListView patientListView = findViewById(R.id.patientListView);
        patientListView.setAdapter(adapter);

        // Listen to data events
        controller.attach(this);
        controller.fetchPatients(3252); // TODO: Pass in practitionerId from user
    }

    @Override
    public void update() {
        runOnUiThread(() -> {
            List<PatientReference> patientReferences = controller.getPatientReferences();
            adapter.updatePatients(patientReferences);
            adapter.notifyDataSetInvalidated();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.detach(this);
    }
}
