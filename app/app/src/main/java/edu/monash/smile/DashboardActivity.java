package edu.monash.smile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class DashboardActivity extends AppCompatActivity {
    private PatientFragment patientFragment = null;
    private DashboardFragment dashboardFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        BottomNavigationView nv = findViewById(R.id.bottom_navigation);
        nv.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.my_patients) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(dashboardFragment)
                        .show(patientFragment).commit();
                return true;
            } else if (id == R.id.dashboard) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(dashboardFragment)
                        .hide(patientFragment)
                        .commit();
                dashboardFragment.handleFragmentSwitched();
                return true;
            }
            return false;
        });

        if (savedInstanceState == null) {
            PatientsMonitor patientsMonitor = new PatientsMonitor(this);
            dashboardFragment = new DashboardFragment(patientsMonitor);
            patientFragment = new PatientFragment(patientsMonitor);
            patientFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, dashboardFragment)
                    .add(R.id.fragment_container, patientFragment)
                    .hide(patientFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
    }
}
