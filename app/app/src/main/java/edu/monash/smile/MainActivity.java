package edu.monash.smile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import edu.monash.smile.dashboard.DashboardActivity;


public class MainActivity extends AppCompatActivity{
    /**
     * This constant defines the ID to use of the practitioner for activity to fragment
     * communication using a bundle.
     */
    public static final String BUNDLE_PRACTITIONER_ID = "practitionerId";

    /**
     * Creates an activity to get the practitioner ID from user input.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.changePracBtn);
        btn.setOnClickListener(v -> {
            // Launch the Dashboard activity
            Intent i = new Intent(MainActivity.this, DashboardActivity.class);

            // Store the received ID of the practitioner into the bundle
            Bundle b = new Bundle();
            EditText ed = findViewById(R.id.pracIdEditText);
            b.putInt(BUNDLE_PRACTITIONER_ID, Integer.valueOf(ed.getText().toString()));

            i.putExtras(b);

            startActivity(i);
        });
    }
}
