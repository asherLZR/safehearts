package edu.monash.smile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import edu.monash.smile.observerPattern.Observer;

public class DashboardActivity extends AppCompatActivity implements Observer {
    private static final String TAG = "MainActivity";
    public static final PatientController controller = new PatientController();
    private StatusCardAdapter statusCardAdapter;

    int practitionerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Bundle b = getIntent().getExtras();
        assert b != null;
        practitionerId = b.getInt("practitionerId");

        BottomNavigationView nv = findViewById(R.id.bottom_navigation);
        nv.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if(id == R.id.my_patients){
                Intent i = new Intent(DashboardActivity.this, PatientActivity.class);
                i.putExtra("practitionerId", practitionerId);
                startActivity(i);
                return true;
            }
            return false;
        });

        // Set up status card list view
        statusCardAdapter = new StatusCardAdapter(this, new ArrayList<>());
        ListView statusCardListView = findViewById(R.id.statusCardListView);
        statusCardListView.setAdapter(statusCardAdapter);

        // Listen to data events
        controller.attach(this);
        controller.setUp(practitionerId);
    }

    @Override
    public void update() {
        runOnUiThread(() -> {
            statusCardAdapter.updateObservedPatients(
                    controller.getObservedPatients()
            );
            statusCardAdapter.notifyDataSetInvalidated();
        });
    }
}
