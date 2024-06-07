package com.example.appmovie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.appmovie.Frag.MovieFragment;
import com.example.appmovie.Frag.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

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

        fm = (FrameLayout) findViewById(R.id.frameFragment);
        bttnav = findViewById(R.id.bottom_nav);
        loadFragment(new MovieFragment());
        bttnav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.menu_item_profile)
                {
                    loadFragment(new ProfileFragment());
                    return true;
                }
                else if(id==R.id.nav_home)
                {
                    loadFragment(new MovieFragment());
                    return true;
                }
                return false;
            }
        });
        load();
        event();
    }
    void load() {
//        btn_sign_out = findViewById(R.id.btn_sign_out);
//        btnHome = findViewById(R.id.btnHome);
//        btnDetail = findViewById(R.id.btnDetail);
//        btnLogin = findViewById(R.id.btnLogin);
//        btnProfile = findViewById(R.id.btnProfile);
    }
    void event() {
//        btn_sign_out.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth auth = FirebaseAuth.getInstance();
//                auth.signOut();
//                Intent intent = new Intent(getApplicationContext(), SignIn.class);
//                startActivity(intent);
//            }
//        });




//        btnHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(getApplicationContext(), MovieHome.class);
//                startActivity(it);
//            }
//        });
//        btnProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(getApplicationContext(), Profile.class);
//                startActivity(it);
//            }
//        });
//        btnDetail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(getApplicationContext(), MovieDetail.class);
//                startActivity(it);
//            }
//        });
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(getApplicationContext(), SignIn.class);
//                startActivity(it);
//            }
//        });
    }
    public void loadFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,fragment);
        fragmentTransaction.commit();
    }

}