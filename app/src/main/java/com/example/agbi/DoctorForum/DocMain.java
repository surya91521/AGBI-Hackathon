package com.example.agbi.DoctorForum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.agbi.Doctor.DoctorLogin;
import com.example.agbi.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.google.android.material.internal.ContextUtils.getActivity;

public class DocMain extends AppCompatActivity {

    private Toolbar mainToolbar;
    //Declaration of FirebaseAuth class,which helps in Authentication
    private FirebaseAuth mAuth;
    //Declaration of FirebaseFirestore which helps in storing data and url of the images
    private FirebaseFirestore firebaseFirestore;
    //Declaration of current_user_id String
    private String current_user_id;

    //Declaration of Floating Action Button and Bottom Navigation Bar
    private FloatingActionButton addPostBtn;
    private BottomNavigationView mainBottomNav;

    //Fragments which are to be used to replace the default fragment in MainActivity
    private HomeFragment homeFragment;
    private NotificationFragment notificationFragment;
    private AccountFragment accountFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_main);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Binding toolbar to the xml layout and setting the toolbar
        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        //Set the title of the action bar to the PhotoBlog
        getSupportActionBar().setTitle("Photo Blog");

        //If user is logged in and get the user details for further use and display all the posts.
        if(mAuth.getCurrentUser() != null) {
            //Bottom bar initialization and binding with the code that was written in xml with the help of id
            mainBottomNav = findViewById(R.id.mainBottomNav);
            //Add button Initialization
            addPostBtn = findViewById(R.id.add_post_btn);
            //Fragments Initialization
           // homeFragment = new HomeFragment();


            //Firstly on OnCreate() we will replace the fragment with homeFragment in MainActivity
            //replaceFragment(homeFragment);
            notificationFragment = new NotificationFragment();
            accountFragment = new AccountFragment();
            homeFragment = new HomeFragment();




            //Bottom navigation bar items click listener,when user clicks an item in Bottom Navigation bar,
            //it will act according to the switch case condition used and binded by the ids of that particular item
            mainBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.bottom_action_home:
                            replaceFragment(homeFragment);
                            return true;
                        case R.id.bottom_action_notif:
                            replaceFragment(notificationFragment);
                            return true;
                        case R.id.bottom_action_account:
                            replaceFragment(accountFragment);
                            return true;
                        default:
                            return false;

                    }

                }
            });

            //When we click on post button(Floating Action Button) then it will send an Explict Intent to PostActicity
            addPostBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Starts an Explict Intent
                    startActivity(new Intent(DocMain.this, PostActivity.class));
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout_btn:
                logOut();
                return true;
            case R.id.action_settings_btn:
                Intent settingIntent = new Intent(DocMain.this, SetupActivity.class);
                startActivity(settingIntent);
                return true;
            default:
                return false;

        }

    }

    private void logOut() {
        mAuth.signOut();
        sendToLogin();
    }

    private void sendToLogin() {

        //Declaration of explict Intent from MainActivity to LoginActivity
        Intent intent = new Intent(DocMain.this, DoctorLogin.class);
        //Starting of the Intent
        startActivity(intent);
        finishAffinity();
    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        //Initiaization and declaration of FragmentTransaction class and begin the transaction of fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //Replace the fragment by given fragment which was passed as arguement
        fragmentTransaction.replace(R.id.main_content_fragment,fragment);
        //fragmentTransaction.addToBackStack(null);
        //We must commit the transaction so that it can be worked properly
        fragmentTransaction.commit();
        //fragmentManager.executePendingTransactions();



    }






}