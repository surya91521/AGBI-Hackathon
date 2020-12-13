package com.example.agbi.Patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.agbi.DoctorForum.SetupActivity;
import com.example.agbi.MainActivity;
import com.example.agbi.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.auth.FirebaseAuth;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class PatientDash extends AppCompatActivity {

    Button map,signout,predic;


    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dash);

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // Check if enabled and if not send user to the GPS settings
        if (!enabled) {
            Toast.makeText(getApplicationContext(),"Please enable location",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        requestPermission();

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
                finishAffinity();
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

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(PatientDash.this, new String[]{ACCESS_FINE_LOCATION},1);
    }

    public void onBackPressed() {
        finishAffinity();
    }
}