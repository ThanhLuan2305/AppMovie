package com.example.appmovie.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.example.appmovie.R;

public class TrailerPlayer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer_player);

        WebView wvTrailer = findViewById(R.id.wvTrailer);
        String trailer = "<iframe width=\"100%\" height=\"315\" src=\"https://www.youtube.com/embed/V2KCAfHjySQ?si=WhTiQCvT3q3szzGw\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        wvTrailer.loadData(trailer, "text/html", "utf-8");
        wvTrailer.getSettings().setJavaScriptEnabled(true);
        wvTrailer.setWebChromeClient(new WebChromeClient());
    }
}