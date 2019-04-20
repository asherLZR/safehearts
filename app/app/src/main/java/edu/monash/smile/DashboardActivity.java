package edu.monash.smile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class DashboardActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
//    public static final PatientController controller = new PatientController();


    private int practitionerId;
    private PatientFragment patientFragment = null;
    private DashboardFragment dbFragment = null;

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
                getSupportFragmentManager().beginTransaction().hide(dbFragment).show(patientFragment).commit();
                return true;
            }else if(id == R.id.dashboard){
                getSupportFragmentManager().beginTransaction().show(dbFragment).hide(patientFragment).commit();
                return true;
            }
            return false;
        });

        if (savedInstanceState == null) {
            Bundle dbArguments = new Bundle();
            dbArguments.putInt(DashboardFragment.PRACTITIONER_ID, practitionerId);
            dbFragment = new DashboardFragment();
            dbFragment.setArguments(dbArguments);

            Bundle patientArguments = new Bundle();
            patientArguments.putInt(PatientFragment.PRACTITIONER_ID, practitionerId);
            patientFragment = new PatientFragment();
            patientFragment.setArguments(patientArguments);

            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, dbFragment)
                    .add(R.id.fragment_container, patientFragment)
                    .hide(patientFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
    }
}
