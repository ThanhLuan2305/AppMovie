package com.example.appmovie.View.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.appmovie.Model.Comment;
import com.example.appmovie.R;

import java.util.ArrayList;

public class CommentListAdapter extends ArrayAdapter<Comment> {
    private final ArrayList<Comment> comments;
    public CommentListAdapter(@NonNull Context context,int resource, @NonNull ArrayList<Comment> objects) {
        super(context, resource, objects);
        comments = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Comment comment = comments.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment_recycler_view, parent, false);
        }
        // Lookup view for data population
        TextView cmtName = convertView.findViewById(R.id.cmtName);
        TextView cmtDate = convertView.findViewById(R.id.cmtDate);
        TextView cmtContent = convertView.findViewById(R.id.cmtContent);
        ImageView cmtImage = convertView.findViewById(R.id.cmtImage);

        cmtName.setText(comment.User_name);
        Log.d("Name", "getView: " + comment.User_name);
        cmtDate.setText(comment.Created_date.toString());
        Log.d("Date", "getView: " + comment.Created_date.toString());
        cmtContent.setText(comment.Content);
        Log.d("Content", "getView: " + comment.Content);
        Glide.with(getContext())
                .load(comment.User_image)
                .into(cmtImage);
        Log.d("User_image", "getView: " + comment.User_image);
        return convertView;
    }
}
