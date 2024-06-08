package com.example.appmovie.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appmovie.Model.Search.IOnClick;
import com.example.appmovie.Model.Search.Item;
import com.example.appmovie.Model.Search.ListSearch;
import com.example.appmovie.R;
import com.example.appmovie.View.Adapter.SearchMovieRecyclerViewAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SearchMovie extends AppCompatActivity {

    private SearchMovieRecyclerViewAdapter adapterSearchMovies;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    String limit;
    SearchView searchText;
    RecyclerView searchView;
    ProgressBar loadSearch;
    List<Item> lstItem = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);
        initView();
        eventClick();
    }
    private void initView() {
        searchText = findViewById(R.id.textSearch);
        loadSearch = findViewById(R.id.loadSearch);
        searchView = findViewById(R.id.viewSearch);
        searchView.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL,false));
    }
    private void sendReques(String url) {
        lstItem.clear();
        mRequestQueue = Volley.newRequestQueue(this);
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                loadSearch.setVisibility(View.GONE);
                ListSearch lstSearch = gson.fromJson(response, ListSearch.class);
                for (Item x : lstSearch.getData().getItems()) {
                    lstItem.add(x);
                }
                adapterSearchMovies= new SearchMovieRecyclerViewAdapter(lstItem, new IOnClick() {
                    @Override
                    public void onClickItemFilm(com.example.appmovie.Model.Search.Item item) {
                        onClickGoToDetail(item);
                    }
                });
                searchView.setAdapter(adapterSearchMovies);
            }
        }, error -> {
            loadSearch.setVisibility(View.GONE);
            Log.i("AppMovie", "onErrorResponse" + error.toString());
        });
        mRequestQueue.add(mStringRequest);
    }
    void eventClick() {
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query==""){
                    lstItem.clear();
                }
                else {
                    String url = "https://phimapi.com/v1/api/tim-kiem?keyword="+query+"&limit=10";
                    sendReques(url);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    private void onClickGoToDetail(com.example.appmovie.Model.Search.Item item) {
        String slug = item.getSlug();
        Intent it = new Intent(getApplicationContext(), MovieDetail.class);
        Bundle bd = new Bundle();
        bd.putString("slug",slug);
        it.putExtra("myPackage",bd);
        startActivity(it);
    }
}