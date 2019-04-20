package edu.monash.smile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import edu.monash.smile.data.model.PatientReference;
import edu.monash.smile.observerPattern.Observer;

public class MainActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_main);

        // Set up toolbar view
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btn = findViewById(R.id.changePracBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                EditText ed = findViewById(R.id.pracIdEditText);
                b.putInt("practitionerId", Integer.valueOf(ed.getText().toString()));

                Intent i = new Intent(MainActivity.this, DashboardActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        EditText tv = findViewById(R.id.pracIdEditText);
//        tv.setText(savedInstanceState.getInt("practitionerId"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        String edValue = findViewById(R.id.pracIdEditText).toString();
//        int practitionerId = Integer.valueOf(edValue);
//        outState.putInt("practitionerId", practitionerId);
    }
}
