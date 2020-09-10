package com.example.agbi.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agbi.Patient.PatientDash;
import com.example.agbi.Patient.PatientLogin;
import com.example.agbi.Patient.PatientRegister;
import com.example.agbi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DoctorLogin extends AppCompatActivity {


    EditText email,password;
    Button login;

    String Email,Password;
    TextView text;

    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);

        mAuth = FirebaseAuth.getInstance();

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);

        text = (TextView) findViewById(R.id.txt_signup);


        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DoctorLogin.this,DoctorRegister.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(DoctorLogin.this);
                progressDialog.setMessage("Logging In");
                progressDialog.show();
                Email = email.getText().toString().trim();
                Password = password.getText().toString();

                Logged();

            }
        });

    }

    private void Logged() {

        mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    progressDialog.cancel();
                    Intent intent = new Intent(DoctorLogin.this, DoctorDash.class);
                    startActivity(intent);
                    finishAffinity();
                }else{
                    progressDialog.cancel();
                    Toast.makeText(DoctorLogin.this,"User not found",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}