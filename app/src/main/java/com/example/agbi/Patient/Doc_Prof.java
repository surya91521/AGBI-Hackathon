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
    TextView textView,textView1,textView2,textView3,textView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc__prof);

        textView = (TextView) findViewById(R.id.name);
        textView1 = (TextView) findViewById(R.id.address);
        textView2 = (TextView) findViewById(R.id.studies);
        textView3 = (TextView) findViewById(R.id.speciality);
        textView4 = (TextView) findViewById(R.id.contact);
        imageView = (CircleImageView)findViewById(R.id.image);

        Intent inn= getIntent();
        Bundle b = inn.getExtras();

        String naam = (String) b.get("name");
        String path =(String) b.get("imageUrl");
        String cont = (String)b.get("contact");
        String speciali =(String) b.get("speciality");
        String quali = (String) b.get("qualification");
        String add = (String) b.get("address");
        textView.setText(naam);
        textView1.setText(add);
        textView2.setText(quali);
        textView3.setText(speciali);
        textView4.setText(cont);

        RequestOptions placeholderRequest = new RequestOptions();
        placeholderRequest.placeholder(R.drawable.default_image);
        Glide.with(Doc_Prof.this).
                setDefaultRequestOptions(placeholderRequest).load(path).into(imageView);

    }
}