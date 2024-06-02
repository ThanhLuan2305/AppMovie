package com.example.appmovie.View.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.appmovie.Model.Home.Item;
import com.example.appmovie.Model.Home.ItemClickListener;
import com.example.appmovie.R;
import com.example.appmovie.View.MovieDetail;
import com.example.appmovie.View.TestOnClickFilm;

import java.util.List;

public class FilmListAdapter extends RecyclerView.Adapter<FilmListAdapter.ViewHolder> {
    private List<Item> lstItem;
    private Context context;
    private ItemClickListener itemClickListener;

    public FilmListAdapter(List<Item> lstItem,ItemClickListener listener ) {
        this.lstItem = lstItem;
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public FilmListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_item_movie, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmListAdapter.ViewHolder holder, int position) {
        Item item = lstItem.get(position);
        holder.textTitle.setText(item.getName());

        RequestOptions requestOptions = new RequestOptions()
                .transform(new CenterCrop(), new RoundedCorners(30));

        Glide.with(context)
                .load(item.getThumbUrl())
                .apply(requestOptions)
                .into(holder.pic);

        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClickItemFilm(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstItem.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        ImageView pic;
        ConstraintLayout layoutItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.titleText);
            pic = itemView.findViewById(R.id.imgMovieItem);
            layoutItem = itemView.findViewById(R.id.layoutItem);
        }
    }
}
