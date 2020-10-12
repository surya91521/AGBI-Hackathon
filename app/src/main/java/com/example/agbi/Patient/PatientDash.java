package com.example.agbi.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.agbi.MainActivity;
import com.example.agbi.R;
import com.google.firebase.auth.FirebaseAuth;

public class PatientDash extends AppCompatActivity {

    Button map,signout,predic;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dash);


        mAuth = FirebaseAuth.getInstance();

        map = (Button)findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientDash.this, Map.class);
                startActivity(intent);
            }
        });

        signout = (Button)findViewById(R.id.out);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(PatientDash.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        predic = (Button) findViewById(R.id.pred);
        predic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(PatientDash.this,Prediction.class);
                startActivity(intent);
            }
        });
    }
}