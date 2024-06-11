package com.example.appmovie.Frag;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.appmovie.Dto.CommentDto;
import com.example.appmovie.Dto.UserManager;
import com.example.appmovie.Model.Comment;
import com.example.appmovie.Model.User;
import com.example.appmovie.R;
import com.example.appmovie.View.Adapter.CommentListAdapter;
import com.example.appmovie.View.MovieDetail;
import com.example.appmovie.View.SignIn;
import com.example.appmovie.View.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CommentDialogFragment extends DialogFragment {
    private final String Movie_id;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    DocumentReference docRef;
    ArrayList<Comment> comments = new ArrayList<>();
    CommentListAdapter adapter;
    ListView listView;
    public CommentDialogFragment(String Movie_id) {
        this.Movie_id = Movie_id;
        docRef = firestore.collection("Comments").document(Movie_id);
    }
    private void fetchComments() {
        // Replace with actual DocumentReference
        getCommentsAsync();
    }
    public void getCommentsAsync() {
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    CommentDto obj = task.getResult().toObject(CommentDto.class);
                    if (document.exists()) {
                        // Document exists, get the comments array
                        comments = obj.comments;
                        if (comments == null) {
                            // Comments array is null, create an empty array
                            comments = new ArrayList<>();
                            Map<String, Object> data = new HashMap<>();
                            data.put("comments", comments);
                            docRef.set(data);

                        } else {
                            Collections.reverse(comments);
                            // Comments array is not null, complete future with comments
                            adapter = new CommentListAdapter(requireContext(), R.layout.comment_recycler_view, comments);
                            listView.setAdapter(adapter);
                        }
                    } else {
                        // Document does not exist, create it with an empty comments array
                        comments = new ArrayList<>();
                        Map<String, Object> data = new HashMap<>();
                        data.put("comments", comments);
                        docRef.set(data);

                    }
                } else {
                    Toast.makeText(getActivity(), "Failed to load data. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void addComment(Comment newComment){

        firestore.collection("Comments").document(newComment.Movie_id)
                .update("comments", FieldValue.arrayUnion(newComment))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            getCommentsAsync();
                            Toast.makeText(getActivity(), "Đăng thành công!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(), "Đăng thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_comment, null);

        EditText cmtContent = view.findViewById(R.id.cmtContentTxt);
        Button submitButton = view.findViewById(R.id.dangBtn);
        listView = view.findViewById(R.id.cmtList);
        fetchComments();

       /* comments.add(new Comment("Id","Content",LocalDate.now().toString(),"User_id", "User_name",
                "https://yt3.googleusercontent.com/QuxsN5qlZ45kmXeWlScDOG-xr7KBoDbu01DhMLZd7R69JHh_5QicghzOpToNMnMcxVS5PUkoUHs=s900-c-k-c0x00ffffff-no-rj","MovieId"));*/


        builder.setView(view)
                .setNegativeButton(R.string.Cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = UserManager.getInstance().getCurrentUser();
                String imageUrl = "https://yt3.googleusercontent.com/QuxsN5qlZ45kmXeWlScDOG-xr7KBoDbu01DhMLZd7R69JHh_5QicghzOpToNMnMcxVS5PUkoUHs=s900-c-k-c0x00ffffff-no-rj";
                if(user.Image != null) {
                    imageUrl = user.Image;
                }
                Comment comment = new Comment(cmtContent.getText().toString(),LocalDate.now().toString(),
                        "" , user.Name,imageUrl , Movie_id);
                comment.Id = "";
                addComment(comment);

            }
        });
        return builder.create();
    }
}
