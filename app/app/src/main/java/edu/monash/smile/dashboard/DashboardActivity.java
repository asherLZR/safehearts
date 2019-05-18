package edu.monash.smile.dashboard;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.monash.smile.MainActivity;
import edu.monash.smile.R;
import edu.monash.smile.dashboard.patientsTab.PatientFragment;
import edu.monash.smile.dashboard.statusTab.StatusFragment;
import edu.monash.smile.data.HealthService;
import edu.monash.smile.data.HealthServiceType;
import edu.monash.smile.polling.Poll;
import edu.monash.smile.preferences.SharedPreferencesHelper;


public class DashboardActivity extends AppCompatActivity {
    private static final int POLL_INTERVAL = 360000;
    public static HealthServiceType healthServiceType;

    private PatientFragment patientFragment = null;
    private StatusFragment statusFragment = null;
    private final Poll poll = new Poll(POLL_INTERVAL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the layout for the dashboard screen
        setContentView(R.layout.activity_dashboard);
        setupBottomNavigation();

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        healthServiceType = (HealthServiceType) bundle.get(MainActivity.BUNDLE_HEALTH_SERVICE_TYPE);

        if (savedInstanceState == null) {
            initialiseFragments();
        }
    }

    /**
     * Creates the fragments (which, in Android, are encapsulated views). Dependencies
     * are provided to fragments from the parent activity and the 1 hour polling starts.
     */
    private void initialiseFragments(){
        PatientsMonitor patientsMonitor = new PatientsMonitor(this.getApplicationContext());
        this.statusFragment = new StatusFragment(patientsMonitor, this.poll, getApplicationContext());
        this.patientFragment = new PatientFragment(patientsMonitor, this.poll, getApplicationContext());
        this.patientFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, statusFragment)
                .add(R.id.fragment_container, patientFragment)
                .hide(patientFragment).commit();
        this.poll.initialisePolling();
    }

    /**
     * Sets-up the navigation in the bottom bar. This code swaps between the two fragments
     * which are held in memory when not shown.
     */
    private void setupBottomNavigation(){
        BottomNavigationView nv = findViewById(R.id.bottom_navigation);
        nv.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.my_patients) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .hide(statusFragment)
                        .show(patientFragment)
                        .commit();
                return true;
            } else if (id == R.id.dashboard) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .show(statusFragment)
                        .hide(patientFragment)
                        .commit();
                statusFragment.handleFragmentSwitched();
                return true;
            }
            return false;
        });
    }

    /**
     * Removes references to existing tasks.
     */
    @Override
    protected void onStop() {
        this.poll.stopRepeatingTask();
        super.onStop();
    }

    /**
     * Deletes preferences when user exits screen.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferencesHelper.removeAllSharedPreferences(this);
        this.finish();
    }

    /**
     * Deletes preferences when app completes.
     */
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferencesHelper.removeAllSharedPreferences(this);
    }
}
