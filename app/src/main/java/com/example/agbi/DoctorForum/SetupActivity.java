package com.example.agbi.DoctorForum;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.agbi.Doctor.DoctorDash;
import com.example.agbi.Doctor.DoctorRegister;
import com.example.agbi.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class SetupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String download_uri;
    FusedLocationProviderClient client;
    private CircleImageView setupImage;
    private Uri mainImageURI = null;
    private EditText setupName;
    private Button setupBtn;
    private String user_id;
    private boolean isChanged = false;
    private ProgressBar setupProgress;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String lati,longi;
    StorageReference image_path;

    String addr , special , contact , quali;

    Spinner spin;

    EditText address, specialit , contactin,qualification;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Toolbar setupToolbar = findViewById(R.id.setupToolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Account Settings");

        requestPermission();

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


            user_id = firebaseAuth.getCurrentUser().getUid();


        client = LocationServices.getFusedLocationProviderClient(this);

        firebaseFirestore = FirebaseFirestore.getInstance();

        setupImage = findViewById(R.id.setup_image);

        setupName = findViewById(R.id.setup_name);
        setupBtn = findViewById(R.id.setup_btn);
        setupProgress = findViewById(R.id.setup_progress);
        setupProgress.setVisibility(View.VISIBLE);
        setupBtn.setEnabled(false);



        spin = (Spinner)findViewById(R.id.special);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.speciality, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);

        address = (EditText) findViewById(R.id.address);
        contactin=(EditText) findViewById(R.id.contact);
        qualification= (EditText) findViewById(R.id.studies);


        firebaseFirestore.collection("Doctors").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if (task.getResult().exists()) {
                        String name = task.getResult().getString("Name");
                        String image = task.getResult().getString("Image");
                        String add = task.getResult().getString("Address");
                        String quali = task.getResult().getString("Qualification");
                        String cont = task.getResult().getString("Contact");
                        String sp = task.getResult().getString("Speciality");
                        mainImageURI = Uri.parse(image);

                        setupName.setText(name);
                        address.setText(add);
                        contactin.setText(cont);
                        qualification.setText(quali);
                        for(int i=0;i<spin.getCount();i++)
                        {
                            if(spin.getItemAtPosition(i).toString().trim().equals(sp))
                            {
                                spin.setSelection(i);
                                break;
                            }
                        }

                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.placeholder(R.drawable.default_image);
                        Glide.with(SetupActivity.this).
                                setDefaultRequestOptions(placeholderRequest).load(image).into(setupImage);
                    }
                }
                else{
                    String error = task.getException().getMessage();
                    Toast.makeText(SetupActivity.this, "firestore Retrieval Error "+error, Toast.LENGTH_SHORT).show();
                }
                setupProgress.setVisibility(View.INVISIBLE);
                setupBtn.setEnabled(true);
            }
        });


        setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String user_name = setupName.getText().toString().trim();
                addr = address.getText().toString().trim();
                special = spin.getSelectedItem().toString().trim();
                contact =  contactin.getText().toString().trim();
                quali = qualification.getText().toString().trim();

                if (!TextUtils.isEmpty(user_name) && mainImageURI != null) {
                    setupProgress.setVisibility(View.VISIBLE);


                    if(isChanged){

                        user_id = firebaseAuth.getCurrentUser().getUid();

                         image_path = storageReference.child("profile_images").child(user_id+".jpg");

                        image_path.putFile(mainImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        storeFirestore(uri,user_name);
                                    }
                                });


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


                    }

                    else{
                        storeFirestore(null,user_name);
                    }
                }
            }
        });


        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(SetupActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {

                        BringImagePicker();
                    }
                } else {

                    BringImagePicker();

                }


            }
        });
    }

    private void storeFirestore(Uri task,String user_name) {


        final String use_name = user_name;
        if(task != null){
           download_uri = task.toString();


        }else {
            download_uri = mainImageURI.toString();
        }


        if (ActivityCompat.checkSelfPermission(SetupActivity.this, ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)  {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        client.getLastLocation().addOnSuccessListener(SetupActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                lati = String.valueOf(location.getLatitude());
                longi = String.valueOf(location.getLongitude());



                Map<String, String> userMap = new HashMap<>();
                userMap.put("Name", use_name);
                userMap.put("Image", download_uri);
                userMap.put("Address", addr);
                userMap.put("Speciality", special);
                userMap.put("Contact", contact);
                userMap.put("Qualification", quali);
                userMap.put("userID",user_id);
                userMap.put("Latitude",lati);
                userMap.put("Longitude",longi);
                firebaseFirestore.collection("Doctors").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(SetupActivity.this, "User Settings Updated", Toast.LENGTH_SHORT).show();
                            Intent mainIntent = new Intent(SetupActivity.this, DoctorDash.class);
                            startActivity(mainIntent);
                            finish();

                        }else{
                            Toast.makeText(SetupActivity.this, "Fire base Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        setupProgress.setVisibility(View.INVISIBLE);
                    }
                });




            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                setupProgress.setVisibility(View.INVISIBLE);
                Toast.makeText(SetupActivity.this,"Unable to Register",Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void BringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(SetupActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageURI = result.getUri();
                setupImage.setImageURI(mainImageURI);
                isChanged=true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{ACCESS_FINE_LOCATION},1);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}