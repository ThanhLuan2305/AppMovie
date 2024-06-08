package com.example.appmovie.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmovie.Model.Home.Category;
import com.example.appmovie.Model.IOnClickCtgMovie;
import com.example.appmovie.R;

import java.util.List;

public class CtgFilmRecyclerAdapter extends RecyclerView.Adapter<CtgFilmRecyclerAdapter.ViewHolder> {
    List<Category> lstCtg;
    Context context;
    private IOnClickCtgMovie itemClickListener;

    public CtgFilmRecyclerAdapter(List<Category> lstCtg, IOnClickCtgMovie itemClickListener) {
        this.lstCtg = lstCtg;
        this.itemClickListener = itemClickListener;
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
        Category ctg = lstCtg.get(position);
        holder.txtNameCtg.setText(ctg.getName());
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClick(ctg);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstCtg.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtNameCtg;
        ConstraintLayout layoutItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameCtg = itemView.findViewById(R.id.txtNameCtg);
            layoutItem = itemView.findViewById(R.id.layoutCtg);
        }
    }
}
