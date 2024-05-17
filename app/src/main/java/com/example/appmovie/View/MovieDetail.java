package com.example.appmovie.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Window;

import com.example.appmovie.R;
import com.example.appmovie.View.Adapter.ActorRecyclerAdapter;

import java.util.ArrayList;

public class MovieDetail extends AppCompatActivity {
    ArrayList<String> lstActor = new ArrayList<>();
    RecyclerView rvActor;
    ActorRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_movie_detail);

        rvActor = (RecyclerView)findViewById(R.id.rvActor);
        rvActor.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));

        lstActor.add("Cao Heu");
        lstActor.add("Cao Heu");
        lstActor.add("Cao Heu");
        lstActor.add("Cao Heu");
        lstActor.add("Cao Heu");
        lstActor.add("Cao Heu");
        lstActor.add("Cao Heu");

        adapter = new ActorRecyclerAdapter(lstActor);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        rvActor.setLayoutManager(mLayoutManager);
        rvActor.setItemAnimator(new DefaultItemAnimator());
        rvActor.setAdapter(adapter);
    }
}