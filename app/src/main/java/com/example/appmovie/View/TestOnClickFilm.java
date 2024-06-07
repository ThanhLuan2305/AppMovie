package com.example.appmovie.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.appmovie.R;

public class TestOnClickFilm extends AppCompatActivity {

    TextView txtSlug;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_on_click_film);
        txtSlug = findViewById(R.id.txtSlug);
        Intent it = getIntent();
        Bundle bd = it.getBundleExtra("myPackage");
        String slug = bd.getString("slug");
        txtSlug.setText(slug);
    }
}