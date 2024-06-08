package com.example.appmovie.Frag;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appmovie.Dto.UserManager;
import com.example.appmovie.Model.FavourFilm;
import com.example.appmovie.Model.User;
import com.example.appmovie.R;
import com.example.appmovie.View.Adapter.MovieAdapter;
import com.example.appmovie.View.MovieDetail;
import com.example.appmovie.View.SignIn;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private TextView txtUserName, txtUserEmail;
    ListView listView;
    private ImageView imgUser;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ImageButton editProfileButton = view.findViewById(R.id.logout_button);

        txtUserName = view.findViewById(R.id.user_name);
        txtUserEmail = view.findViewById(R.id.user_email);
        imgUser = view.findViewById(R.id.profile_image);

        initView(view);

        User user = UserManager.getInstance().getCurrentUser();
        if (user != null) {

            txtUserName.setText(user.Name);
            txtUserEmail.setText(user.Email);
            Glide.with(this).load(user.Image).into(imgUser);
            for(FavourFilm fv: user.Favour_film){
                Log.d("FILM", fv.origin_name);
            }
            favourMovie(user.Favour_film);
        }

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                Intent intent = new Intent(getActivity(), SignIn.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void favourMovie(ArrayList<FavourFilm> films) {
        ArrayList<String> movieNames = new ArrayList<>();
        for (FavourFilm film : films) {
            movieNames.add(film.origin_name);
        }

        MovieAdapter adapter = new MovieAdapter(getContext(), R.layout.layout_item_movie, films);
        listView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FavourFilm film) {
                onClickGoToDetail(film);
            }
        });
    }

    private void initView(View view) {
        listView = view.findViewById(R.id.listViewMovies);
    }

    private void onClickGoToDetail(FavourFilm film) {
        String slug = film.slug;
        Intent it = new Intent(getActivity(), MovieDetail.class);
        Bundle bd = new Bundle();
        bd.putString("slug",slug);
        it.putExtra("myPackage",bd);
        startActivity(it);
    }
}