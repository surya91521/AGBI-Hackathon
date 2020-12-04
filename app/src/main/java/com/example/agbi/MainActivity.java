package com.example.agbi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agbi.Doctor.DoctorDash;
import com.example.agbi.Doctor.DoctorLogin;
import com.example.agbi.Doctor.DoctorRegister;
import com.example.agbi.Patient.PatientDash;
import com.example.agbi.Patient.PatientLogin;
import com.example.agbi.Patient.PatientRegister;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    Button patientR;
    Button doctorR;
    TextView patientL , doctorL;


    CircleImageView circleImageView;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;


    CollectionReference reference;

    ProgressDialog progressDialog;
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(MainActivity.this);
        mAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        if(mAuth.getCurrentUser()!=null)
        {
            progressDialog.setMessage("Logging In");
            progressDialog.show();
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
                            progressDialog.cancel();
                            startActivity(intent);
                            finishAffinity();
                        }else{
                            Intent intent = new Intent(MainActivity.this,PatientDash.class);
                            progressDialog.cancel();
                            startActivity(intent);
                            finishAffinity();
                        }
                    }else{
                        progressDialog.cancel();
                    }
                }
            });

        }
            setContentView(R.layout.activity_main);


        patientR = (Button)findViewById(R.id.button1);
        doctorR = (Button)findViewById(R.id.button2);
        patientL = (TextView)findViewById(R.id.button3);
        doctorL = (TextView)findViewById(R.id.button4);

        circleImageView = (CircleImageView)findViewById(R.id.imageView);


        patientR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PatientLogin.class);
                startActivity(intent);
            }
        });

        doctorR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DoctorLogin.class);
                startActivity(intent);
            }
        });

        patientL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PatientRegister.class);
                startActivity(intent);
            }
        });

        doctorL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DoctorRegister .class);
                startActivity(intent);
            }
        });

            isStoragePermissionGranted();

    }

    @Override
    public void onStart() {
        super.onStart();
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // Check if enabled and if not send user to the GPS settings

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


    }



    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Msg","Permission is granted");
                return true;
            } else {

                Log.v("Mg","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Msg","Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("Msg", "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
        }
    }



}