package edu.monash.smile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import edu.monash.smile.dashboard.DashboardActivity;
import edu.monash.smile.data.HealthService;
import edu.monash.smile.data.HealthServiceType;


public class MainActivity extends AppCompatActivity{
    /**
     * This constant defines the ID to use of the practitioner for activity to fragment
     * communication using a bundle.
     */
    public static final String BUNDLE_PRACTITIONER_ID = "practitionerId";
    public static final String BUNDLE_HEALTH_SERVICE_TYPE = "healthServiceType";
    private HealthServiceType healthServiceType = HealthServiceType.FHIR;

    /**
     * Creates an activity to get the practitioner ID from user input.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.changePracBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the Dashboard activity
                Intent i = new Intent(MainActivity.this, DashboardActivity.class);

                // Store the received ID of the practitioner into the bundle
                Bundle b = new Bundle();
                EditText ed = findViewById(R.id.pracIdEditText);
                b.putInt(BUNDLE_PRACTITIONER_ID, Integer.valueOf(ed.getText().toString()));

                ChipGroup chipGroup = findViewById(R.id.choice_chip_group);
                int chipId = chipGroup.getCheckedChipId();
                HealthServiceType healthServiceType = HealthServiceType.FHIR;
                if (chipId == R.id.sqlChip){
                    healthServiceType = HealthServiceType.SQL_NOT_FHIR;
                }

                b.putSerializable(BUNDLE_HEALTH_SERVICE_TYPE, healthServiceType);

                i.putExtras(b);

                startActivity(i);
            }
        });

        Chip fhirChip = findViewById(R.id.fhirChip);
        fhirChip.setChecked(true);
    }
}
