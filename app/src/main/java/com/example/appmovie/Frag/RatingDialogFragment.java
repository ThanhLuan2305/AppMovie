package com.example.appmovie.Frag;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.appmovie.Dto.RatingDto;
import com.example.appmovie.Dto.UserManager;
import com.example.appmovie.Model.Rating;
import com.example.appmovie.Model.User;
import com.example.appmovie.R;
import com.example.appmovie.View.Adapter.CommentListAdapter;
import com.example.appmovie.View.Adapter.RatingAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RatingDialogFragment extends DialogFragment {
    ListView lvRating;
    RatingAdapter adapter;
    RatingDto listRating = new RatingDto();
    Button rateBtn;
    RatingBar ratingBar;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    DocumentReference docRef;

    public static RatingDialogFragment newInstance(RatingDto data, String movie_id) {
        RatingDialogFragment fragment = new RatingDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("list_rating", (Serializable) data);
        args.putSerializable("movie_id", movie_id);

        fragment.setArguments(args);
        return fragment;
    }
    public static RatingDialogFragment newInstance(String movie_id) {
        RatingDialogFragment fragment = new RatingDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("movie_id", movie_id);

        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_rating_dialog_fragment, container, false);
        /*listRating = (RatingDto) getArguments().getSerializable("list_rating");*/

        String movie_id = getArguments().getString("movie_id");

        docRef = firestore.collection("Rating").document(movie_id);
        lvRating = view.findViewById(R.id.lvRating);
        rateBtn = view.findViewById(R.id.btnSubmitRating);
        ratingBar = view.findViewById(R.id.ratingBar);
/*        for(Rating rate : ratings.ratings) {
            listRating.ratings.add(rate);
        }*/
        adapter = new RatingAdapter(getContext(), R.layout.layout_item_rating, listRating.ratings);

        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = UserManager.getInstance().getCurrentUser();
                String imageUrl = "https://yt3.googleusercontent.com/QuxsN5qlZ45kmXeWlScDOG-xr7KBoDbu01DhMLZd7R69JHh_5QicghzOpToNMnMcxVS5PUkoUHs=s900-c-k-c0x00ffffff-no-rj";
                if(user.Image != null) {
                    imageUrl = user.Image;
                }
                int rateValue = (int)ratingBar.getRating();
                Rating newRate = new Rating(user.Name,imageUrl, rateValue);
                addRate(movie_id,newRate);
            }
        });
        getRatingData();
        return view;
    }
    void getRatingData() {
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            RatingDto rating = documentSnapshot.toObject(RatingDto.class);
                            if(rating != null) {
                                listRating = rating;
                                Collections.reverse(listRating.ratings);
                                adapter = new RatingAdapter(getContext(), R.layout.layout_item_rating, listRating.ratings);

                                lvRating.setAdapter(adapter);

                            }
                            else{
                                Map<String, Object> data = new HashMap<>();
                                data.put("ratings", listRating.ratings);
                                docRef.set(data);
                            }
                        }
                    }
                });
    }
    void addRate(String movie_id, Rating newRate){
        firestore.collection("Rating").document(movie_id)
                .update("ratings", FieldValue.arrayUnion(newRate))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Đăng thành công!", Toast.LENGTH_SHORT).show();
                            getRatingData();
                        }
                        else {
                            Toast.makeText(getActivity(), "Đăng thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;

            //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().setAttributes(params);
        }
    }
}