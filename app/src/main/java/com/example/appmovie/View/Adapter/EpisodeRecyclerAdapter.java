package com.example.appmovie.View.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmovie.R;

public class EpisodeRecyclerAdapter extends RecyclerView.Adapter<EpisodeRecyclerAdapter.MyViewHolder> {

    int[] arr;

    public EpisodeRecyclerAdapter(int[] arr) {
        this.arr = arr;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.episode_recycler_view, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        position++;
        holder.episodesBtn.setText("" + position);
    }

    @Override
    public int getItemCount() {
        return arr.length;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{

        Button episodesBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            episodesBtn = itemView.findViewById(R.id.episodeBtn);
        }
    }
}
