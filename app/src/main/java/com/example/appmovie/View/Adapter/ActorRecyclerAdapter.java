package com.example.appmovie.View.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appmovie.R;

import java.util.ArrayList;

public class ActorRecyclerAdapter extends RecyclerView.Adapter<ActorRecyclerAdapter.MyViewHolder> {
    ArrayList<String> lstActor;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameActor;
        public MyViewHolder(View view) {
            super(view);
            nameActor = (TextView) view.findViewById(R.id.tvNameActor);
        }
    }
    public ActorRecyclerAdapter(ArrayList<String> lstActor) {this.lstActor = lstActor;}
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_actor,
                parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nameActor.setText(lstActor.get(position));
    }

    @Override
    public int getItemCount() {
        return lstActor.size();
    }
}
