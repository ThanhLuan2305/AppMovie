package com.example.appmovie.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.appmovie.R;

public class SearchMovie extends AppCompatActivity {

    SearchView searchText;
    EditText searchEditText;
    RecyclerView searchView;
    ProgressBar loadSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);
    }
    private void initView() {
        searchText = findViewById(R.id.textSearch);
        loadSearch = findViewById(R.id.loadSearch);
        searchView = findViewById(R.id.viewSearch);
    }
}