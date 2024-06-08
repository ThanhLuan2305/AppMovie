package com.example.appmovie.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.appmovie.Dto.EpUrl;
import com.example.appmovie.Frag.CommentDialogFragment;
import com.example.appmovie.Model.episode;
import com.example.appmovie.Model.episodes;
import com.example.appmovie.R;
import com.example.appmovie.View.Adapter.EpisodeRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class WatchMovie extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView currentEp;
    RecyclerView.LayoutManager layoutManager;
    EpisodeRecyclerAdapter episodeRecyclerAdapter;
    FloatingActionButton playBtn;
    Button viewComment;
    EpUrl url = new EpUrl();
    List<episode> arr;
    String slug= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_movie);

        Intent callerIntent = getIntent();
        Bundle packageFroCaller = callerIntent.getBundleExtra("EpisodesPakage");
        episodes epis = (episodes) packageFroCaller.getSerializable("Episodes");
        slug = (String) packageFroCaller.getString("MovieSlug");
        arr = epis.server_data;
        url.url = epis.server_data.get(0).link_m3u8;
        initView();
        addEvent();
    }
    void initView(){
        currentEp = findViewById(R.id.currentEp);
        playBtn = findViewById(R.id.playbtn);
        recyclerView = findViewById(R.id.episodeList);
        layoutManager = new GridLayoutManager(this,4);
        recyclerView.setLayoutManager(layoutManager);
        episodeRecyclerAdapter = new EpisodeRecyclerAdapter(arr, url, currentEp);
        recyclerView.setAdapter(episodeRecyclerAdapter);
        recyclerView.setHasFixedSize(true);
        viewComment = findViewById(R.id.viewCmtBtn);
    }
    void addEvent(){
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WatchMovie.this, MoviePlayer.class);

                // Put data into the intent as extras
//                String url = "https://s2.phim1280.tv/20240310/Qah2fQHw/index.m3u8";
                intent.putExtra("url", url.url);
                String epName = arr.get(episodeRecyclerAdapter.getSelectedPosition()).filename;
                intent.putExtra("epName", epName);

                // Start SecondActivity
                startActivity(intent);
            }
        });
        viewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentDialogFragment commentDialogFragment = new CommentDialogFragment(slug);
                    commentDialogFragment.show(getSupportFragmentManager(), "comment_dialog");

            }
        });
    }
}