package com.example.appmovie.View.Adapter;

import android.content.Context;
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
import com.example.appmovie.Model.Search.Item;
import com.example.appmovie.Model.Search.IOnClick;
import com.example.appmovie.R;

import java.util.List;

public class SearchMovieRecyclerViewAdapter  extends RecyclerView.Adapter<SearchMovieRecyclerViewAdapter.ViewHolder> {
    private List<Item> lstItem;
    private Context context;
    private IOnClick itemClickListener;

    public SearchMovieRecyclerViewAdapter(List<Item> lstItem,IOnClick listener ) {
        this.lstItem = lstItem;
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public SearchMovieRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_item_movie, parent, false);
        return new SearchMovieRecyclerViewAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = lstItem.get(position);
        holder.textTitle.setText(item.getName());

        RequestOptions requestOptions = new RequestOptions()
                .transform(new CenterCrop(), new RoundedCorners(30));
        String urlImg = "https://img.phimapi.com/" + item.getThumbUrl();
        Glide.with(context)
                .load(urlImg)
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
