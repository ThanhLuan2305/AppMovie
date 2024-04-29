package com.example.appmovie.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.ProgressiveMediaSource;
import androidx.media3.ui.PlayerView;

import android.content.Context;
import android.content.Intent;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;

import com.example.appmovie.R;

import java.net.URL;

public class MoviePlayer extends AppCompatActivity {
    PlayerView playerView;
    ExoPlayer exoPlayer;
    String sampleVideo = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_player);

        initView();
    }
    void initView(){
        Intent intent = getIntent();
        sampleVideo = intent.getStringExtra("url");

        playerView = findViewById(R.id.PlayerViewMovie);
        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);

        MediaItem mediaItem = MediaItem.fromUri(sampleVideo);
        //MediaItem mediaItem = MediaItem.fromUri("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();

    }

    @Override
    protected void onStart() {
        super.onStart();
        exoPlayer.play();
    }

    @Override
    protected void onStop() {
        super.onStop();
        exoPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exoPlayer.release();
    }
}