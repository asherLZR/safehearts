package edu.monash.smile;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import edu.monash.smile.observerPattern.Observer;

public class MainActivity extends AppCompatActivity implements Observer {
    private static final String TAG = "MainActivity";
    private final AppController controller = new AppController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up view
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Listen to data events
        controller.attach(this);
        controller.fetchPatients(3252); // TODO: Pass in practitionerId from user
    }

    @Override
    public void update() {
        Log.d(TAG, "update: " + controller.getPatientReferences());
    }
}
