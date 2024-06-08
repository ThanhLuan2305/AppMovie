package com.example.appmovie.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.appmovie.Model.FavourFilm;
import com.example.appmovie.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends ArrayAdapter<FavourFilm> {

    private Context mContext;
    private int mResource;
    private OnItemClickListener mListener;

    public MovieAdapter(Context context, int resource, ArrayList<FavourFilm> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    // Interface để xử lý sự kiện click
    public interface OnItemClickListener {
        void onItemClick(FavourFilm film);
    }

    // Phương thức để thiết lập trình nghe sự kiện
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(mResource, parent, false);
        }

        ImageView imgMovieItem = view.findViewById(R.id.imgMovieItem);
        TextView titleText = view.findViewById(R.id.titleText);

        FavourFilm film = getItem(position);
        if (film != null) {
            Glide.with(mContext).load(film.poster_url).into(imgMovieItem);
            titleText.setText(film.origin_name);

            // Xử lý sự kiện click
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(film);
                    }
                }
            });
        }

        return view;
    }
}

