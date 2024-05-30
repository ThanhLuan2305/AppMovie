package com.example.appmovie.View.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.appmovie.Model.Home.Item;
import com.example.appmovie.Model.Home.ItemClickListener;
import com.example.appmovie.R;
import com.example.appmovie.View.MovieDetail;

import java.util.List;

public class FilmListAdapter extends RecyclerView.Adapter<FilmListAdapter.ViewHolder> {
    Item item ;
    List<Item> lstItem ;
    Context context;
    public FilmListAdapter(List<Item> lstItem ) {
        this.lstItem = lstItem;
    }
    @NonNull
    @Override
    public FilmListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_movie,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmListAdapter.ViewHolder holder, int position) {
        holder.textTitle.setText(lstItem.get(position).getName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));

        Glide.with(context)
                .load(lstItem.get(position).getThumbUrl())
                .apply(requestOptions)
                .into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return lstItem.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle;
        ImageView pic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.titleText);
            pic = itemView.findViewById(R.id.imgMovieItem);
        }
    }
}
