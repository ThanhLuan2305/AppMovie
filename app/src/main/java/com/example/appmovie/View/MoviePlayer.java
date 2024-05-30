package com.example.appmovie.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.appmovie.R;

public class MoviePlayer extends AppCompatActivity {
    PlayerView playerView;
    ExoPlayer exoPlayer;
    String sampleVideo = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4";
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_player);

        initView();
    }
    private void toggleBackButtonVisibility() {
        if (backButton.getVisibility() == View.VISIBLE) {
            backButton.setVisibility(View.GONE);
        } else {
            backButton.setVisibility(View.VISIBLE);
        }
    }
    void initView(){
        backButton = findViewById(R.id.backButton);

        Intent intent = getIntent();
        sampleVideo = intent.getStringExtra("url");

        playerView = findViewById(R.id.PlayerViewMovie);
        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);

        playerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                CharSequence test = v.getContentDescription();
                String testString = test.toString();
                if(testString.startsWith("Hide")){
                    backButton.setVisibility(View.VISIBLE);
                }
                else if(testString.startsWith("Show")){
                    backButton.setVisibility(View.GONE);
                }
            }
        });

        MediaItem mediaItem = MediaItem.fromUri(sampleVideo);
        //MediaItem mediaItem = MediaItem.fromUri("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        // Set touch listener on the PlayerView to detect taps

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // This will close the current activity and go back to the previous one
            }
        });
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