package edu.monash.smile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import edu.monash.smile.dashboard.DashboardActivity;


public class MainActivity extends AppCompatActivity{
    public static final String BUNDLE_PRACTITIONER_ID = "practitionerId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.changePracBtn);
        btn.setOnClickListener(v -> {
            Bundle b = new Bundle();
            EditText ed = findViewById(R.id.pracIdEditText);
            b.putInt(BUNDLE_PRACTITIONER_ID, Integer.valueOf(ed.getText().toString()));

            Intent i = new Intent(MainActivity.this, DashboardActivity.class);
            i.putExtras(b);
            startActivity(i);
        });
    }
}
