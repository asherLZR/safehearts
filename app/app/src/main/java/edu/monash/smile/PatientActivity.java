package edu.monash.smile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import edu.monash.smile.observerPattern.Observer;


public class PatientActivity extends AppCompatActivity implements Observer {
    private PatientArrayAdapter patientAdapter;
    private PatientController patientController = new PatientController();
    private int practitionerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        practitionerId = getIntent().getIntExtra("practitionerId", 0);

        BottomNavigationView nv = findViewById(R.id.bottom_navigation);
        nv.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if(id == R.id.dashboard){
                Intent i = new Intent(PatientActivity.this, DashboardActivity.class);
                i.putExtra("practitionerId", practitionerId);
                startActivity(i);
                return true;
            }
            return false;
        });

        // Set up patients monitor
        PatientsMonitor patientsMonitor = new PatientsMonitor(this);

        // Set up patient list view
        patientAdapter = new PatientArrayAdapter(this, new ArrayList<>(), patientsMonitor);
        ListView patientListView = findViewById(R.id.patientListView);
        patientListView.setAdapter(patientAdapter);

        // Listen to data events
        patientController.attach(this);
        patientController.setUp(practitionerId);
    }

    @Override
    public void update() {
        runOnUiThread(() -> {
            patientAdapter.updatePatients(patientController.getPatientReferences());
            patientAdapter.notifyDataSetInvalidated();
        });
    }
}
