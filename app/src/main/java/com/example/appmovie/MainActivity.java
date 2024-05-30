package com.example.appmovie;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.appmovie.View.MovieDetail;
import com.example.appmovie.View.MovieHome;
import com.example.appmovie.View.Profile;
import com.example.appmovie.View.SignIn;
import com.example.appmovie.View.SplashActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ActionBar actionBar;
    FrameLayout fm;
    BottomNavigationView bttnav;
    Button btn_sign_out, btnHome, btnDetail, btnProfile, btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar=getSupportActionBar();
//        fm = findViewById(R.id.frameFragment);
//        bttnav = findViewById(R.id.bottom_nav);
        load();
        event();
    }
    void load() {
        btn_sign_out = findViewById(R.id.btn_sign_out);
        btnHome = findViewById(R.id.btnHome);
        btnDetail = findViewById(R.id.btnDetail);
        btnLogin = findViewById(R.id.btnLogin);
        btnProfile = findViewById(R.id.btnProfile);
    }
    void event() {
        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(intent);
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), MovieHome.class);
                startActivity(it);
            }
        });
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), Profile.class);
                startActivity(it);
            }
        });
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), MovieDetail.class);
                startActivity(it);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), SignIn.class);
                startActivity(it);
            }
        });
    }
//    public void loadFragment(Fragment fragment)
//    {
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.frameFragment,fragment);
//        ft.commit();
//    }

}