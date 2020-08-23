package com.example.agbi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.agbi.Doctor.DoctorRegister;
import com.example.agbi.Patient.PatientRegister;

public class MainActivity extends AppCompatActivity {

    Button patient;
    Button doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        patient = (Button)findViewById(R.id.button1);
        doctor = (Button)findViewById(R.id.button2);

        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PatientRegister.class);
                startActivity(intent);
            }
        });

        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DoctorRegister.class);
                startActivity(intent);
            }
        });
    }
}