package com.example.agbi.DoctorForum;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.agbi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class PostActivity extends AppCompatActivity {

    private Toolbar newPostToolbar;
    private ImageView newPostImage;
    private EditText newPostDesc;
    private Button newPostBtn;
    private Uri postImageuri = null;

    private ProgressBar newPostProgress;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;
    private Bitmap compressedImageFile;
    String downloadUri;
    StorageReference imagePath;
    String downloadthumbUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        newPostToolbar = findViewById(R.id.new_post_toolbar);
        setSupportActionBar(newPostToolbar);
        getSupportActionBar().setTitle("Add New Post");
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        newPostImage = findViewById(R.id.new_post_image);
        newPostDesc = findViewById(R.id.new_post_desc);
        newPostBtn = findViewById(R.id.post_btn);
        newPostProgress = findViewById(R.id.new_post_progress);
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = firebaseAuth.getCurrentUser().getUid();


        newPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(520, 520)
                        .setAspectRatio(1, 1)
                        .start(PostActivity.this);

            }
        });

        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String desc = newPostDesc.getText().toString().trim();
                if (!TextUtils.isEmpty(desc) && postImageuri != null) {
                    newPostProgress.setVisibility(View.VISIBLE);

                    final String randomname = UUID.randomUUID().toString();
                    final StorageReference filePath = storageReference.child("post_images").child(randomname+".jpg");
                    filePath.putFile(postImageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUri = uri.toString();
                                }
                            });


                            if (task.isSuccessful()) {
                                File newImageFile = new File(postImageuri.getPath());

                                try {
                                    compressedImageFile = new Compressor(PostActivity.this)
                                            .setMaxHeight(100)
                                            .setMaxWidth(100)
                                            .setQuality(3)
                                            .compressToBitmap(newImageFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] thumbData = baos.toByteArray();

                                UploadTask uploadTask = storageReference.child("post_images/thumbs").child(randomname + ".jpg").putBytes(thumbData);

                                  uploadTask.addOnSuccessListener(PostActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                      @Override
                                      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                          Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                                          while(!uri.isComplete());
                                          {
                                              Uri url = uri.getResult();
                                              downloadthumbUri = url.toString();
                                          }



                              /*  uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        imagePath = storageReference.child("post_images/thumbs").child(randomname + ".jpg");
                                        imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                downloadthumbUri = uri.toString();
                                            }
                                        });
                                 */
                                        Map<String, Object> postmap = new HashMap<>();
                                        postmap.put("image_url", downloadUri);
                                        postmap.put("image_thumb",downloadthumbUri);
                                        postmap.put("desc", desc);
                                        postmap.put("user_id", currentUserId);
                                        postmap.put("timestamp", FieldValue.serverTimestamp());
                                        firebaseFirestore.collection("Posts").add(postmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(PostActivity.this, "Post was added", Toast.LENGTH_SHORT).show();
                                                    Intent mainIntent = new Intent(PostActivity.this, DocMain.class);
                                                    startActivity(mainIntent);
                                                    finish();

                                                } else {

                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(PostActivity.this, "FireStore Upload Error " + error, Toast.LENGTH_SHORT).show();

                                                }
                                                newPostProgress.setVisibility(View.INVISIBLE);
                                            }
                                        });


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        String error = e.getMessage();
                                        Toast.makeText(PostActivity.this, "FireBase Thumbnail Upload Error " + error, Toast.LENGTH_SHORT).show();

                                    }
                                });


                            } else {
                                newPostProgress.setVisibility(View.INVISIBLE);
                                String error = task.getException().getMessage();
                                Toast.makeText(PostActivity.this, "Firebase Upload Error " + error, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                postImageuri = result.getUri();
                newPostImage.setImageURI(postImageuri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}