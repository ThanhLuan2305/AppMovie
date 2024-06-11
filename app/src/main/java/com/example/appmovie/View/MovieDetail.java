package com.example.appmovie.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appmovie.Dto.RatingDto;
import com.example.appmovie.Model.Rating;
import com.example.appmovie.Dto.UserManager;
import com.example.appmovie.Frag.CustomDialogFragment;
import com.example.appmovie.Frag.RatingDialogFragment;
import com.example.appmovie.Model.FavourFilm;
import com.example.appmovie.Model.Movie;
import com.example.appmovie.Model.User;
import com.example.appmovie.Model.episode;
import com.example.appmovie.Model.episodes;
import com.example.appmovie.R;
import com.example.appmovie.View.Adapter.ActorRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

public class MovieDetail extends AppCompatActivity {
    ArrayList<String> lstActor = new ArrayList<>();
    episodes epis = new episodes();
    User currentUser;
    String userId = "";
    Set<FavourFilm> listFavourFilm = new HashSet<>();
    Movie movie = new Movie();
    RecyclerView rvActor;
    ActorRecyclerAdapter adapter;
    Dialog mDialog;
    String slug = "";
    ImageView thumbImg;
    TextView tvInfoMovie, tvCategory, tvTime, tvTitle, tvContent, tvCountry, tvDirector, tvLastUpdate;
    Button btnEpisodeCurrent, btnShared, btnWatchNow, btnWatchTrailer, btnRate;
    ToggleButton btnFavorite;
    String url = "https://phimapi.com/phim/";
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference node_ref = firestore.collection("Users");
    CollectionReference node_rating_ref = firestore.collection("Rating");
    RatingDto listRating = new RatingDto();
    Date currentDate = new Date();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = UserManager.getInstance().getCurrentUser();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        listFavourFilm.addAll(currentUser.Favour_film);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_movie_detail);
        Intent it = getIntent();
        Bundle bd = it.getBundleExtra("myPackage");
        slug = bd.getString("slug");
        String urlSlug = url+slug;
        addControls();
        loadFavour();
        getDataMovie(urlSlug);
        addEvents();
        mDialog = new Dialog(MovieDetail.this);
    }
    void loadAdapterActor() {
        rvActor.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        adapter = new ActorRecyclerAdapter(lstActor);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        rvActor.setLayoutManager(mLayoutManager);
        rvActor.setItemAnimator(new DefaultItemAnimator());
        rvActor.setAdapter(adapter);
    }
    void addControls() {
        rvActor = (RecyclerView)findViewById(R.id.rvActor);
        thumbImg = (ImageView) findViewById(R.id.thumbImg);
        tvInfoMovie = (TextView) findViewById(R.id.tvInfoMovie);
        tvCategory = (TextView) findViewById(R.id.tvCategory);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvCountry = (TextView) findViewById(R.id.tvCountry);
        tvDirector = (TextView) findViewById(R.id.tvDirector);
        tvLastUpdate = (TextView) findViewById(R.id.tvLastUpdate);
        btnEpisodeCurrent = (Button) findViewById(R.id.btnEpisodeCurrent);
        btnShared = (Button) findViewById(R.id.btnShared);
        btnWatchNow = (Button) findViewById(R.id.btnWatchNow);
        btnWatchTrailer = (Button) findViewById(R.id.btnWatchTrailer);
        btnFavorite = (ToggleButton) findViewById(R.id.btnFavorite);
        btnRate = (Button) findViewById(R.id.btnRate);
    }
    void addEvents() {
        btnShared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Your link film");
                startActivity(Intent.createChooser(intent, "Share via"));
            }
        });
        btnFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    addFavourFilm();
                }
                else {
                    deleteFavourFilm();
                }
            }
        });
        btnWatchNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(epis.server_data.size() <= 0) {
                    Toast.makeText(MovieDetail.this, "Không có phim", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(MovieDetail.this, WatchMovie.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Episodes", epis);
                    bundle.putString("MovieSlug",slug);
                    intent.putExtra("EpisodesPakage", bundle);
                    startActivity(intent);
                }
            }
        });
        btnWatchTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(movie.trailer_url.isEmpty()) {
                    Toast.makeText(MovieDetail.this, "Không có trailer", Toast.LENGTH_SHORT).show();
                }
                else {
                    showDialog();
                }
            }
        });
        btnEpisodeCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog_v2();
            }
        });
    }
    public void showDialog() {
        CustomDialogFragment dialogFragment = CustomDialogFragment.newInstance(movie.trailer_url);
        dialogFragment.show(getSupportFragmentManager(), "custom_dialog");
    }
    public void showRatingDialog() {
        getRatingData();

    }
    public void showRatingDialog_v2() {
        RatingDialogFragment dialogFragment = RatingDialogFragment.newInstance(movie.id);
        dialogFragment.show(getSupportFragmentManager(), "custom_dialog");

    }
    void getRatingData() {
        node_rating_ref.document(movie.id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            RatingDto rating = documentSnapshot.toObject(RatingDto.class);
                            if(rating != null) {
                                for (Rating rate : rating.ratings) {
                                    listRating.ratings.add(rate);
                                }
                            }
                            else{
                                Map<String, Object> data = new HashMap<>();
                                data.put("ratings", listRating.ratings);
                                node_rating_ref.document(movie.id).set(data);
                            }
                            RatingDialogFragment dialogFragment = RatingDialogFragment.newInstance(listRating, movie.id);
                            dialogFragment.show(getSupportFragmentManager(), "custom_dialog");
                        }
                    }
                });
    }
    void loadFavour() {
        node_ref.document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            User user = documentSnapshot.toObject(User.class);
                            btnFavorite.setChecked(false);
                            if(user != null && user.Favour_film != null) {
                                for(FavourFilm favourFilm : user.Favour_film) {
                                    if(favourFilm.movie_id.equals(movie.id)) {
                                        btnFavorite.setChecked(true);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                });
    }
    public void getDataMovie(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(MovieDetail.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            parseJsonData(response);
                        } catch (JSONException | ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MovieDetail.this, "Error when loading infor movie", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }
    void addFavourFilm() {
        currentUser.Favour_film.add(new FavourFilm(
                movie.id,
                movie.slug,
                movie.origin_name,
                movie.poster_url,
                movie.name,
                currentDate

        ));
        Set<FavourFilm> listFavour = new LinkedHashSet<>(currentUser.Favour_film);
        currentUser.Favour_film.clear();
        currentUser.Favour_film.addAll(listFavour);
        node_ref.document(userId)
                .set(currentUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Toast.makeText(MovieDetail.this, "Đã thêm vào phim yêu thích", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MovieDetail.this, "Fail", Toast.LENGTH_LONG).show();
                    }
                });
    }
    void deleteFavourFilm() {
        FavourFilm film = new FavourFilm(
                movie.id,
                movie.slug,
                movie.origin_name,
                movie.poster_url,
                movie.name
        );
        currentUser.Favour_film.remove(film);
        node_ref.document(userId)
                .set(currentUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Toast.makeText(MovieDetail.this, "Đã thêm vào phim yêu thích", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MovieDetail.this, "Fail", Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void parseJsonData(String response) throws JSONException, ParseException {
        movie.category = new ArrayList<>();
        movie.actor = new ArrayList<>();
        movie.country = new ArrayList<>();
        movie.director = new ArrayList<>();
        epis.server_data = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(response);
        JSONArray episodeArray = jsonObject.getJSONArray("episodes");

        for(int i = 0; i < episodeArray.length(); i++) {
            JSONObject episodeObj = episodeArray.getJSONObject(i);
            epis.server_name = episodeObj.getString("server_name");
            JSONArray serverDataArray = new JSONArray(episodeObj.getString("server_data"));

            for (int j = 0; j < serverDataArray.length(); j++) {
                JSONObject episode = serverDataArray.getJSONObject(j);
                episode epi = new episode(
                        episode.getString("filename"),
                        episode.getString("link_embed"),
                        episode.getString("link_m3u8"),
                        episode.getString("name"),
                        episode.getString("slug")
                );
                epis.server_data.add(epi);
            }
        }

        JSONObject movieObj = jsonObject.getJSONObject("movie");
        movie.id = movieObj.getString("_id");
        movie.name = movieObj.getString("name");
        movie.slug = movieObj.getString("slug");
        movie.origin_name = movieObj.getString("origin_name");
        movie.content = movieObj.getString("content");

        JSONArray categoryArray = new JSONArray(movieObj.getString("category"));
        for(int i = 0; i < categoryArray.length(); i++) {
            JSONObject category = categoryArray.getJSONObject(i);
            movie.category.add(category.getString("name"));
        }

        movie.thumb_url = movieObj.getString("thumb_url");
        movie.poster_url = movieObj.getString("poster_url");
        movie.episode_total = movieObj.getInt("episode_total");
        movie.year = movieObj.getInt("year");
        movie.subtitle = movieObj.getString("lang");
        movie.time = movieObj.getString("time");
        movie.trailer_url = movieObj.getString("trailer_url");

        JSONArray actorArray = new JSONArray(movieObj.getString("actor"));
        for(int i = 0; i < actorArray.length(); i++) {
            String actor = actorArray.getString(i);
            lstActor.add(actor);
            movie.actor.add(actor);
        }

        JSONArray countryArr = new JSONArray(movieObj.getString("country"));
        for(int i = 0; i < countryArr.length(); i++) {
            movie.country.add(countryArr.getJSONObject(i).getString("name"));
        }

        JSONArray directorArr = new JSONArray(movieObj.getString("director"));
        for(int i = 0; i < directorArr.length(); i++) {
            movie.director.add(directorArr.getString(i));
        }

        JSONObject tObj = new JSONObject(movieObj.getString("modified"));
        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String outputPattern = "HH:mm:ss dd/MM/yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        inputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = inputFormat.parse(tObj.getString("time"));
        movie.last_update = outputFormat.format(date);
        movie.episode_current = movieObj.getString("episode_current");

        asignData();
    }

    String convertToInfoMovie() {
        return movie.episode_total + " tập  |   " +
                movie.year + "  |   " +
                movie.subtitle;
    }
    String convertToCategoryList() {
        String result = "";
        for(String category : movie.category) {
            if(movie.category.indexOf(category) == (movie.category.size()-1)) {
                result += category;
            }
            else {
                result += (category + " • ");
            }
        }
        return result;
    }
    String convertToCountyList() {
        String result = "";
        for(String country : movie.country) {
            if(movie.country.indexOf(country) == (movie.country.size()-1)) {
                result += country;
            }
            else {
                result += (country + ", ");
            }
        }
        return result;
    }
    String convertToDirectorList() {
        String result = "";
        for(String country : movie.director) {
            if(movie.director.indexOf(country) == (movie.director.size()-1)) {
                result += country;
            }
            else {
                result += (country + ", ");
            }
        }
        return result;
    }
    void asignData() {
        loadAdapterActor();
        Picasso.with(MovieDetail.this).load(movie.thumb_url).into(thumbImg);
        tvInfoMovie.setText(convertToInfoMovie());
        tvCategory.setText(convertToCategoryList());
        tvTime.setText(movie.time);
        tvTitle.setText(movie.name);
        tvContent.setText(movie.content);
        btnEpisodeCurrent.setText(movie.episode_current);
        tvCountry.setText(convertToCountyList());
        tvDirector.setText(convertToDirectorList());
        tvLastUpdate.setText(movie.last_update);
        loadFavour();
    }
}