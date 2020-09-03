package com.example.agbi.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.agbi.DoctorForum.SetupActivity;
import com.example.agbi.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class Doc_Prof extends AppCompatActivity {

    CircleImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc__prof);

        textView = (TextView) findViewById(R.id.name);
        imageView = (CircleImageView)findViewById(R.id.image);

        Intent inn= getIntent();
        Bundle b = inn.getExtras();

        String naam = (String) b.get("name");
        String path =(String) b.get("imageUrl");
        textView.setText(naam);

        RequestOptions placeholderRequest = new RequestOptions();
        placeholderRequest.placeholder(R.drawable.default_image);
        Glide.with(Doc_Prof.this).
                setDefaultRequestOptions(placeholderRequest).load(path).into(imageView);

    }
}