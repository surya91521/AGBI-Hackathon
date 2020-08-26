package com.example.agbi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.agbi.Doctor.DoctorDash;
import com.example.agbi.Doctor.DoctorLogin;
import com.example.agbi.Doctor.DoctorRegister;
import com.example.agbi.Patient.PatientDash;
import com.example.agbi.Patient.PatientLogin;
import com.example.agbi.Patient.PatientRegister;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    Button patientR;
    Button doctorR;
    Button patientL , doctorL;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;


    CollectionReference reference;
    CollectionReference reference1;

    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        if(mAuth.getCurrentUser()!=null)
        {
            DocumentReference ref = firestore.collection("Doctors").document(mAuth.getUid());
            ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        DocumentSnapshot document =  task.getResult();
                        if(document.exists())
                        {
                            Intent intent = new Intent(MainActivity.this,DoctorDash.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(MainActivity.this,PatientDash.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            });

        }
        setContentView(R.layout.activity_main);


        patientR = (Button)findViewById(R.id.button1);
        doctorR = (Button)findViewById(R.id.button2);
        patientL = (Button)findViewById(R.id.button3);
        doctorL = (Button)findViewById(R.id.button4);

        patientR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PatientRegister.class);
                startActivity(intent);
            }
        });

        doctorR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DoctorRegister.class);
                startActivity(intent);
            }
        });

        patientL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PatientLogin.class);
                startActivity(intent);
            }
        });

        doctorL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DoctorLogin.class);
                startActivity(intent);
            }
        });
    }
}