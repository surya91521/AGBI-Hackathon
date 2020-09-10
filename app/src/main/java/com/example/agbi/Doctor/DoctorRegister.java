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
import android.widget.TextView;
import android.widget.Toast;

import com.example.agbi.DoctorForum.SetupActivity;
import com.example.agbi.Patient.PatientDash;
import com.example.agbi.Patient.PatientLogin;
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



    EditText email,repass;
    EditText password;
    Button register;
    TextView text;


    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    DocumentReference reference;

    String  Email, Password;
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


        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.register);
        text = (TextView) findViewById(R.id.txt_login);
        repass = (EditText) findViewById(R.id.Repassword);


        //  client = LocationServices.getFusedLocationProviderClient(this);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DoctorRegister.this, DoctorLogin.class);
                startActivity(intent);
                finish();
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass;
                progressDialog = new ProgressDialog(DoctorRegister.this);
                progressDialog.setMessage("Registering");
                progressDialog.show();


                Email = email.getText().toString().trim();
                Password = password.getText().toString().trim();
                pass = repass.getText().toString().trim();
               if(Password.equals(pass)) {
                   savedata();
               }else{
                   progressDialog.cancel();
                   Toast.makeText(DoctorRegister.this,"Enter Passowrd Correctly",Toast.LENGTH_SHORT).show();
               }
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