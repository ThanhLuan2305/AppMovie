package com.example.appmovie.View.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.appmovie.Model.Rating;
import com.example.appmovie.R;

import java.util.ArrayList;

public class RatingAdapter extends ArrayAdapter {
    ArrayList<Rating> listRating;
    Context context;
    int layout;

    public RatingAdapter(@NonNull Context context, int resource, ArrayList<Rating> objects) {
        super(context, resource, objects);
        this.context = context;
        this.listRating = objects;
        this.layout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout, null);
        }
        Rating rating = listRating.get(position);
        ImageView imgUser = convertView.findViewById(R.id.imgUser);
        RequestOptions requestOptions = new RequestOptions()
                .transform(new CenterCrop(), new RoundedCorners(30));
        Glide.with(context)
                .load(rating.User_image)
                .apply(requestOptions)
                .into(imgUser);
        TextView textView = convertView.findViewById(R.id.tvUsername);
        textView.setText(rating.User_name);

        RatingBar ratingBar = convertView.findViewById(R.id.ratingBarShow);
        ratingBar.setRating(rating.Rating);
        return convertView;
    }
}
