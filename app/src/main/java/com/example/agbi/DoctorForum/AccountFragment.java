package com.example.agbi.DoctorForum;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.agbi.Patient.Doc_Prof;
import com.example.agbi.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AccountFragment extends Fragment {

    public AccountFragment() {
        // Required empty public constructor
    }

    FirebaseFirestore firestore;
    FirebaseAuth mAuth;

    TextView textView5,textView1,textView2,textView3,textView4;
    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_account_fragment, container, false);

        firestore = FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

         imageView = (ImageView)view.findViewById(R.id.image);
         textView1 = (TextView)view.findViewById(R.id.name);
         textView2 = (TextView)view.findViewById(R.id.address);
         textView3 = (TextView)view.findViewById(R.id.studies);
         textView4 = (TextView)view.findViewById(R.id.speaciality);
         textView5 = (TextView)view.findViewById(R.id.contact);


        firestore.collection("Doctors").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot =  task.getResult();
                        String naam = (String) documentSnapshot.get("Name");
                        String path =(String) documentSnapshot.get("Image");
                        String cont = (String)documentSnapshot.get("Contact");
                        String speciali =(String) documentSnapshot.get("Speciality");
                        String quali = (String) documentSnapshot.get("Qualification");
                        String add = (String) documentSnapshot.get("Address");
                        textView1.setText(naam);
                        textView2.setText(add);
                        textView3.setText(quali);
                        textView4.setText(speciali);
                        textView5.setText(cont);

                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.placeholder(R.drawable.default_image);
                        Glide.with(AccountFragment.this).
                                setDefaultRequestOptions(placeholderRequest).load(path).into(imageView);
                }else{
                    Toast.makeText(getContext(),"Firebase error",Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }
}