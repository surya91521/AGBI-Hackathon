package com.example.agbi.Doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.agbi.DoctorForum.DocMain;
import com.example.agbi.DoctorForum.PostActivity;
import com.example.agbi.MainActivity;
import com.example.agbi.Patient.PatientDash;
import com.example.agbi.R;
import com.google.firebase.auth.FirebaseAuth;

public class DoctorDash extends AppCompatActivity {

    Button signout,inside;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dash);


        mAuth = FirebaseAuth.getInstance();
        signout = (Button)findViewById(R.id.out);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(DoctorDash.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        inside = (Button)findViewById(R.id.in);
        inside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorDash.this, DocMain.class);
                startActivity(intent);
            }
        });
    }


    public void onBackPressed() {
        finishAffinity();
    }
}