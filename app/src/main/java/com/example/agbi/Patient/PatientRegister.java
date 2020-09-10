package com.example.agbi.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agbi.Doctor.DoctorRegister;
import com.example.agbi.R;
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

public class PatientRegister extends AppCompatActivity {




    EditText fullname;
    EditText email;
    EditText password ,repass;
    Button register;

    TextView text;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    DocumentReference reference;

    String Fullname,Email , Password;
    String userId;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        fullname = (EditText) findViewById(R.id.fullname);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        register = (Button)findViewById(R.id.register);
         text = (TextView) findViewById(R.id.txt_login);
         repass = (EditText) findViewById(R.id.Repassword);


       text.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent = new Intent(PatientRegister.this,PatientLogin.class);
               startActivity(intent);
               finish();
           }
       });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pass;
                progressDialog = new ProgressDialog(PatientRegister.this);
                progressDialog.setMessage("Registering");
                progressDialog.show();

                Fullname = fullname.getText().toString().trim();
                Email = email.getText().toString().trim();
                Password = password.getText().toString().trim();
                pass = repass.getText().toString().trim();
               if(Password.equals(pass)) {
                   savedata();
               }else{
                   progressDialog.cancel();
                   Toast.makeText(PatientRegister.this,"Enter Passowrd Correctly",Toast.LENGTH_SHORT).show();
               }
            }
        });


    }

    private void savedata() {


        mAuth.createUserWithEmailAndPassword(Email,Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                userId = mAuth.getCurrentUser().getUid();
                reference = firestore.collection("Patients").document(userId);
                Map<String,String> user = new HashMap<>();
                user.put("Name",Fullname);
                user.put("Email",Email);
                user.put("userID",userId);

                reference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {

                        progressDialog.cancel();
                        Toast.makeText(PatientRegister.this,"Successful",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PatientRegister.this,PatientDash.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.cancel();
                        Toast.makeText(PatientRegister.this,"Some error",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.cancel();
                Toast.makeText(PatientRegister.this,"Unable to Register",Toast.LENGTH_SHORT).show();

            }
        });

    }
}