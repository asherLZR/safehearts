package edu.monash.smile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_main);

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
}
