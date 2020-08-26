package com.example.agbi.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agbi.DoctorForum.SetupActivity;
import com.example.agbi.Patient.PatientDash;
import com.example.agbi.Patient.PatientRegister;
import com.example.agbi.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class DoctorRegister extends AppCompatActivity {

    //FusedLocationProviderClient client;


    EditText fullname;
    EditText email;
    EditText password;
    Button register;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    DocumentReference reference;

    String Fullname, Email, Password;
    String userId;

     String lati ;
     String longi;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_register);

        //requestPermission();
        mAuth = FirebaseAuth.getInstance();
       // firestore = FirebaseFirestore.getInstance();

        fullname = (EditText) findViewById(R.id.fullname);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.register);


      //  client = LocationServices.getFusedLocationProviderClient(this);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(DoctorRegister.this);
                progressDialog.setMessage("Registering");
                progressDialog.show();

                Fullname = fullname.getText().toString().trim();
                Email = email.getText().toString().trim();
                Password = password.getText().toString().trim();
                savedata();
            }
        });
    }


    private void savedata() {


        mAuth.createUserWithEmailAndPassword(Email, Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {




                        progressDialog.cancel();
                        Toast.makeText(DoctorRegister.this,"Successful",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DoctorRegister.this, SetupActivity.class);
                        startActivity(intent);
                        finishAffinity();




                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.cancel();
                        Toast.makeText(DoctorRegister.this,"Unable to Register",Toast.LENGTH_SHORT).show();

                    }
                });


            }

}