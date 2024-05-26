package com.example.appmovie.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmovie.Model.Home.Category;
import com.example.appmovie.R;

import java.util.List;

public class CtgFilmRecyclerAdapter extends RecyclerView.Adapter<CtgFilmRecyclerAdapter.ViewHolder> {
    List<Category> lstCtg;
    Context context;

    public CtgFilmRecyclerAdapter(List<Category> lstCtg) {
        this.lstCtg = lstCtg;
    }

    @NonNull
    @Override
    public CtgFilmRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_typefilm,parent,false);
        return new CtgFilmRecyclerAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CtgFilmRecyclerAdapter.ViewHolder holder, int position) {
        holder.txtNameCtg.setText(lstCtg.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return lstCtg.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtNameCtg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameCtg = itemView.findViewById(R.id.txtNameCtg);
        }
    }
}
