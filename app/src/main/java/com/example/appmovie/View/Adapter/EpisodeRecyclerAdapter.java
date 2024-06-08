package com.example.appmovie.View.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmovie.Dto.EpUrl;
import com.example.appmovie.Model.episode;
import com.example.appmovie.R;

import java.util.List;

public class EpisodeRecyclerAdapter extends RecyclerView.Adapter<EpisodeRecyclerAdapter.MyViewHolder> {

    List<episode> arr;
    EpUrl url;
    TextView currentEp;
    private int selectedPosition = 0;

    public EpisodeRecyclerAdapter(List<episode> arr, EpUrl url, TextView currentEp) {
        this.arr = arr;
        this.url = url;
        this.currentEp = currentEp;
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
        int finalPosition = position;
        position++;
        holder.episodesBtn.setText("" + position);
        if (selectedPosition == finalPosition) {
            holder.episodesBtn.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.highlight_color));
        } else {
            holder.episodesBtn.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.ep));
        }

        holder.episodesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url.url = arr.get(finalPosition).link_m3u8;
                selectedPosition = finalPosition;
                currentEp.setText("Tap " + (selectedPosition + 1));
                notifyDataSetChanged();
            }
        });
       /* holder.episodesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url.url = arr.get(finalPosition).link_m3u8;
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{

        Button episodesBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            episodesBtn = itemView.findViewById(R.id.episodeBtn);
        }
    }
    public int getSelectedPosition(){
        return selectedPosition;
    }
}
