package com.example.agbi.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.agbi.Doctor.DoctorDash;
import com.example.agbi.DoctorForum.SetupActivity;
import com.example.agbi.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Map extends FragmentActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    GoogleMap maps;
    FusedLocationProviderClient client;
    String latiii, longiii;

    Spinner spin;
    FirebaseFirestore firestore;
    List<Marker> AllMarkers = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);



        firestore = FirebaseFirestore.getInstance();
        spin = (Spinner) findViewById(R.id.special);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.speciality, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);
        client = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void solve() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            return;
        }
        client.getLastLocation().addOnSuccessListener(Map.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                  if(location!=null) {
                      latiii = String.valueOf(location.getLatitude());
                      longiii = String.valueOf(location.getLongitude());

                      LatLng Maharastra = new LatLng(Double.parseDouble(latiii) , Double.parseDouble(longiii));
                      maps.addMarker(new MarkerOptions().position(Maharastra).title("YOU")
                              .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                      maps.moveCamera(CameraUpdateFactory.newLatLng(Maharastra));
                  }else{
                      Toast.makeText(Map.this,"Unable to find you location go back and open maps again",Toast.LENGTH_LONG).show();
                  }



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                Toast.makeText(Map.this, "Unable to Register", Toast.LENGTH_SHORT).show();

            }
        });



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        maps = googleMap;

        firestore.collection("Doctors").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        if (documentSnapshot.get("Speciality").equals(spin.getSelectedItem().toString().trim())) {
                            String lat = (String) documentSnapshot.get("Latitude");
                            String lon = (String) documentSnapshot.get("Longitude");

                            double lati = Double.parseDouble(lat);
                            double longi = Double.parseDouble(lon);

                            LatLng pos = new LatLng(lati, longi);
                            maps.addMarker(new MarkerOptions().position(pos).title((String) documentSnapshot.get("Name"))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                        }

                    }
                }
            }
        });

        maps.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                final LatLng pos = marker.getPosition();

                Log.i("Position", String.valueOf(pos.latitude) + " " + String.valueOf(pos.longitude));

                firestore.collection("Doctors").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String lat = (String) documentSnapshot.get("Latitude");
                                String lon = (String) documentSnapshot.get("Longitude");

                                double lati = Double.parseDouble(lat);
                                double longi = Double.parseDouble(lon);

                                if (lati == pos.latitude && longi == pos.longitude) {
                                    Intent intent = new Intent(Map.this, Doc_Prof.class);
                                    intent.putExtra("imageUrl", documentSnapshot.get("Image").toString());
                                    intent.putExtra("name", documentSnapshot.get("Name").toString());
                                    intent.putExtra("address", documentSnapshot.get("Address").toString());
                                    intent.putExtra("contact", documentSnapshot.get("Contact").toString());
                                    intent.putExtra("qualification", documentSnapshot.get("Qualification").toString());
                                    intent.putExtra("speciality", documentSnapshot.get("Speciality").toString());
                                    startActivity(intent);
                                    break;
                                }


                            }
                        }
                    }
                });

                return false;
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        maps.clear();
        solve();


        firestore.collection("Doctors").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                    {
                        for(QueryDocumentSnapshot documentSnapsho:task.getResult()) {
                            if (documentSnapsho.get("Speciality").equals(spin.getSelectedItem().toString().trim())) {
                                String lat = (String) documentSnapsho.get("Latitude");
                                String lon = (String) documentSnapsho.get("Longitude");

                                double lati = Double.parseDouble(lat);
                                double longi = Double.parseDouble(lon);

                                LatLng pos = new LatLng(lati, longi);
                                maps.addMarker(new MarkerOptions().position(pos).title((String) documentSnapsho.get("Name"))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                            }

                        }
                    }
                }
            }
        });


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}