package edu.monash.smile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import edu.monash.smile.observerPattern.Observer;

import static edu.monash.smile.DashboardActivity.controller;

public class PatientActivity extends AppCompatActivity implements Observer {

    private PatientArrayAdapter patientAdapter;
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

        // Set up patient list view
        patientAdapter = new PatientArrayAdapter(this, new ArrayList<>());
        ListView patientListView = findViewById(R.id.patientListView);
        patientListView.setAdapter(patientAdapter);

        // Listen to data events
        controller.attach(this);
        controller.setUp(practitionerId);
    }

    @Override
    public void update() {
        runOnUiThread(() -> {
            patientAdapter.updatePatients(controller.getPatientReferences());
            patientAdapter.notifyDataSetInvalidated();
        });
    }
}
