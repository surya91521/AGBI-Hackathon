package com.example.agbi.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.agbi.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Map extends FragmentActivity implements OnMapReadyCallback , AdapterView.OnItemSelectedListener {

    GoogleMap maps;
     Spinner spin;
    FirebaseFirestore firestore;
    List<Marker> AllMarkers = new ArrayList<Marker>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        firestore = FirebaseFirestore.getInstance();

        spin = (Spinner)findViewById(R.id.special);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.speciality, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        maps = googleMap;
        firestore.collection("Doctors").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                    {
                       if(documentSnapshot.get("Speciality").equals(spin.getSelectedItem().toString().trim())) {
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

                final LatLng pos =marker.getPosition();

                Log.i("Position", String.valueOf(pos.latitude)+" "+ String.valueOf(pos.longitude));

                firestore.collection("Doctors").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                            {
                                String lat = (String) documentSnapshot.get("Latitude");
                                String lon = (String) documentSnapshot.get("Longitude");

                                double lati = Double.parseDouble(lat);
                                double longi = Double.parseDouble(lon);

                                if(lati==pos.latitude && longi == pos.longitude)
                                {
                                    Intent intent = new Intent(Map.this,Doc_Prof.class);
                                    intent.putExtra("imageUrl",documentSnapshot.get("Image").toString());
                                    intent.putExtra("name",documentSnapshot.get("Name").toString());
                                    intent.putExtra("address",documentSnapshot.get("Address").toString());
                                    intent.putExtra("contact",documentSnapshot.get("Contact").toString());
                                    intent.putExtra("qualification",documentSnapshot.get("Qualification").toString());
                                    intent.putExtra("speciality",documentSnapshot.get("Speciality").toString());
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

        LatLng Maharastra = new LatLng(18.6069 , 73.8751 );
        maps.addMarker(new MarkerOptions().position(Maharastra).title("AIT")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        maps.moveCamera(CameraUpdateFactory.newLatLng(Maharastra));


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