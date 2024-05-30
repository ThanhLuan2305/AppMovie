package com.example.appmovie.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.appmovie.R;
import com.example.appmovie.View.Adapter.EpisodeRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WatchMovie extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    EpisodeRecyclerAdapter episodeRecyclerAdapter;
    FloatingActionButton playBtn;
    int[] arr = {0,1,2,2,3,4,3,1,3,3,2,1,2,212,31,32,3,123,123,12,312,313,123,12,312,3,123,123};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_movie);
        initView();
        addEvent();
    }
    void initView(){
        playBtn = findViewById(R.id.playbtn);
        recyclerView = findViewById(R.id.episodeList);
        layoutManager = new GridLayoutManager(this,4);
        recyclerView.setLayoutManager(layoutManager);
        episodeRecyclerAdapter = new EpisodeRecyclerAdapter(arr);
        recyclerView.setAdapter(episodeRecyclerAdapter);
        recyclerView.setHasFixedSize(true);
    }
    void addEvent(){
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WatchMovie.this, MoviePlayer.class);

                // Put data into the intent as extras
                String url = "https://s2.phim1280.tv/20240310/Qah2fQHw/index.m3u8";
                intent.putExtra("url", url);

                // Start SecondActivity
                startActivity(intent);
            }
        });
    }
}