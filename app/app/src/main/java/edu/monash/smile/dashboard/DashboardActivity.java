package edu.monash.smile.dashboard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.monash.smile.R;
import edu.monash.smile.dashboard.patientsTab.PatientFragment;
import edu.monash.smile.dashboard.statusTab.StatusFragment;
import edu.monash.smile.preferences.SharedPreferencesHelper;


public class DashboardActivity extends AppCompatActivity {
    private PatientFragment patientFragment = null;
    private StatusFragment statusFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);
        setupBottomNavigation();
        if (savedInstanceState == null) {
            initialiseFragments();
        }
    }

    private void initialiseFragments(){
        PatientsMonitor patientsMonitor = new PatientsMonitor(this);
        statusFragment = new StatusFragment(patientsMonitor);
        patientFragment = new PatientFragment(patientsMonitor);
        patientFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, statusFragment)
                .add(R.id.fragment_container, patientFragment)
                .hide(patientFragment).commit();
    }

    private void setupBottomNavigation(){
        BottomNavigationView nv = findViewById(R.id.bottom_navigation);
        nv.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.my_patients) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(statusFragment)
                        .show(patientFragment).commit();
                return true;
            } else if (id == R.id.dashboard) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(statusFragment)
                        .hide(patientFragment)
                        .commit();
                statusFragment.handleFragmentSwitched();
                return true;
            }
            return false;
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferencesHelper.removeAllSharedPreferences(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferencesHelper.removeAllSharedPreferences(this);
    }
}
