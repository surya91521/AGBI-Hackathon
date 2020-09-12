package com.example.agbi.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.agbi.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Map extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap maps;

    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        firestore = FirebaseFirestore.getInstance();



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
                        String lat = (String) documentSnapshot.get("Latitude");
                        String lon = (String) documentSnapshot.get("Longitude");

                        double lati = Double.parseDouble(lat);
                        double longi = Double.parseDouble(lon);

                        LatLng pos = new LatLng(lati,longi);
                        maps.addMarker(new MarkerOptions().position(pos).title((String) documentSnapshot.get("Name")));


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

        LatLng Maharastra = new LatLng(18.6069 , 73.8751 );
        maps.addMarker(new MarkerOptions().position(Maharastra).title("AIT"));
        maps.moveCamera(CameraUpdateFactory.newLatLng(Maharastra));


    }
}