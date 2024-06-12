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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MovieAdapter extends ArrayAdapter<FavourFilm> {

    private Context mContext;
    private int mResource;
    private OnItemClickListener mListener;

    public MovieAdapter(Context context, int resource, ArrayList<FavourFilm> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    public interface OnItemClickListener {
        void onItemClick(FavourFilm film);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(mResource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.imgMovieItem = view.findViewById(R.id.imgMovieItem);
            viewHolder.titleText = view.findViewById(R.id.titleText);
            viewHolder.titleText2 = view.findViewById(R.id.titleText2);
            viewHolder.createAt = view.findViewById(R.id.create_at);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        FavourFilm film = getItem(position);
        if (film != null) {
            Glide.with(mContext).load(film.poster_url).into(viewHolder.imgMovieItem);
            viewHolder.titleText.setText(film.name);
            viewHolder.titleText2.setText(film.origin_name);
            if (film.create_at != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String dateString = dateFormat.format(film.create_at);
                viewHolder.createAt.setText("Thời gian thêm: " + dateString);
            } else {
                viewHolder.createAt.setText("không xác định");
            }
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null && film != null) {
                    mListener.onItemClick(film);
                }
            }
        });

        return view;
    }

    static class ViewHolder {
        ImageView imgMovieItem;
        TextView titleText;
        TextView titleText2;
        TextView createAt;
    }
}

