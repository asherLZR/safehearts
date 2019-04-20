package edu.monash.smile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import edu.monash.smile.data.model.PatientReference;
import edu.monash.smile.observerPattern.Observer;

public class DashboardActivity extends AppCompatActivity implements Observer {
//    private static final String TAG = "MainActivity";
    private final AppController controller = new AppController();
    private PatientArrayAdapter adapter;
    private Integer practitionerId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Bundle b = getIntent().getExtras();
        practitionerId = b.getInt("practitionerId");


        // Set up patient list view
        adapter = new PatientArrayAdapter(this, new ArrayList<PatientReference>());
        ListView patientListView = findViewById(R.id.patientListView);
        patientListView.setAdapter(adapter);

        // Listen to data events
        controller.attach(this);
        controller.fetchPatients(practitionerId);
    }

    @Override
    public void update() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<PatientReference> patientReferences = controller.getPatientReferences();
                adapter.updatePatients(patientReferences);
                adapter.notifyDataSetInvalidated();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.detach(this);
    }
}
