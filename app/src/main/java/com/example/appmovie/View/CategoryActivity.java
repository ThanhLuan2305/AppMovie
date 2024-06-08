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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appmovie.Model.Home.ListFilm;
import com.example.appmovie.Model.Search.IOnClick;
import com.example.appmovie.Model.Search.Item;
import com.example.appmovie.Model.Search.ListSearch;
import com.example.appmovie.R;
import com.example.appmovie.View.Adapter.SearchMovieRecyclerViewAdapter;
import com.example.appmovie.View.MovieDetail;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    RecyclerView viewCtg;

    Spinner spinner;
    private SearchMovieRecyclerViewAdapter adapterCtg;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    TextView txtCtg;
    ProgressBar loadCtg;
    ListSearch listSearchs = new ListSearch();
    List<Item> lstItem = new ArrayList<>();

    ArrayList<String> lstPage = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        initView();
        Intent it = getIntent();
        Bundle bd = it.getBundleExtra("myPackage");
        String idCtg = bd.getString("id");
        String url = "https://phimapi.com/v1/api/danh-sach/" + idCtg;
        //txtCtg.setText(nameCtg);
        sendRequesPage(url);
        eventClick(idCtg);
    }
    private void initView() {
        txtCtg = findViewById(R.id.txtNameCtg);
        spinner = findViewById(R.id.spinnerPageCtg);
        loadCtg = findViewById(R.id.progressBarCtg);
        viewCtg = findViewById(R.id.viewCtg);
        viewCtg.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL,false));
    }
    private void eventClick(String nameCtg) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String page = lstPage.get(position);
                String[] splitPage = page.split(" ");
                String numPage = splitPage[splitPage.length - 1];
                String url = "https://phimapi.com/v1/api/danh-sach/"+nameCtg+"?page=" + numPage;
                sendReques(url);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void sendReques(String url) {
        lstItem.clear();
        mRequestQueue = Volley.newRequestQueue(this);
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                loadCtg.setVisibility(View.GONE);
                ListSearch lstSearch = gson.fromJson(response, ListSearch.class);
                for (Item x : lstSearch.getData().getItems()) {
                    lstItem.add(x);
                }
                adapterCtg= new SearchMovieRecyclerViewAdapter(lstItem, new IOnClick() {
                    @Override
                    public void onClickItemFilm(com.example.appmovie.Model.Search.Item item) {
                        onClickGoToDetail(item);
                    }
                });
                viewCtg.setAdapter(adapterCtg);
            }
        }, error -> {
            loadCtg.setVisibility(View.GONE);
            Log.i("AppMovie", "onErrorResponse" + error.toString());
        });
        mRequestQueue.add(mStringRequest);
    }
    private void sendRequesPage(String url) {
        lstItem.clear();
        mRequestQueue = Volley.newRequestQueue(this);
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                ListSearch lstSearch = gson.fromJson(response, ListSearch.class);
                loadSpinner(lstSearch);
            }
        }, error -> {
            Log.i("AppMovie", "onErrorResponse" + error.toString());
        });
        mRequestQueue.add(mStringRequest);
    }
    private void loadSpinner(ListSearch listSearchs) {
        int page = listSearchs.getData().getParams().getPagination().getTotalPages();
        for (int i = 1; i<=page;i++) {
            lstPage.add("Trang " + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, lstPage);
        spinner.setAdapter(adapter);

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