package com.example.appmovie.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appmovie.Model.Home.Category;
import com.example.appmovie.Model.Home.Item;
import com.example.appmovie.Model.Home.ItemClickListener;
import com.example.appmovie.Model.Home.ListFilm;
import com.example.appmovie.Model.SliderItems;
import com.example.appmovie.R;
import com.example.appmovie.View.Adapter.FilmListAdapter;
import com.example.appmovie.View.Adapter.SliderAdapters;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MovieHome extends AppCompatActivity {
    private RecyclerView.Adapter adapterCategory;
    private FilmListAdapter adapterNewMovies,adapterUpcomming;
    private RecyclerView recyclerViewNewMovies, recyclerViewUpcomming, recyclerViewCategory;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest1, mStringRequest2, mStringRequest3;
    private ProgressBar loading1, loading2, loading3;
    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();
    TextView txtSeach;
    List<ListFilm> lstFilm = new ArrayList<>();
    List<Item> lstItem = new ArrayList<>();
    List<Category> lstCtg = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_home);
        initView();
        banners();
        sendReques();
        loadCateglory();
        clickSearch();
    }

    private void loadCateglory() {
        Category category1 = new Category("phim-bo", "Phim Bộ");

        Category category2 = new Category("phim-le", "Phim Lẻ");

        Category category3 = new Category("tv-shows", "TV Show");

        Category category4 = new Category("hoat-hinh", "Hoạt Hình");
        lstCtg.add(category1);
        lstCtg.add(category2);
        lstCtg.add(category3);
        lstCtg.add(category4);
        loading2.setVisibility(View.GONE);
        //adapterCategory= new CtgFilmRecyclerAdapter(lstCtg);
        recyclerViewCategory.setAdapter(adapterCategory);
    }
    private void banners() {
        List<SliderItems> sliderItems= new ArrayList<>();
        sliderItems.add(new SliderItems(R.drawable.wide1));
        sliderItems.add(new SliderItems(R.drawable.wide2));
        sliderItems.add(new SliderItems(R.drawable.wide3));

        viewPager2.setAdapter(new SliderAdapters(sliderItems,viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer=new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r=1-Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setCurrentItem(1);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
            }
        });
    }
    private void sendReques() {
        mRequestQueue = Volley.newRequestQueue(this);
        mStringRequest1 = new StringRequest(Request.Method.GET, "https://phimapi.com/danh-sach/phim-moi-cap-nhat?page=1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                loading1.setVisibility(View.GONE);
                loading3.setVisibility(View.GONE);
                ListFilm listFilm = gson.fromJson(response, ListFilm.class);
                lstFilm.add(listFilm);
                for (ListFilm x : lstFilm) {
                    lstItem = x.getItems();
                }
                adapterNewMovies= new FilmListAdapter(lstItem, new ItemClickListener() {
                    @Override
                    public void onClickItemFilm(Item item) {
                        onClickGoToDetail(item);
                    }
                });
                recyclerViewNewMovies.setAdapter(adapterNewMovies);

                adapterUpcomming= new FilmListAdapter(lstItem, new ItemClickListener() {
                    @Override
                    public void onClickItemFilm(Item item) {
                        onClickGoToDetail(item);
                    }
                });
                recyclerViewUpcomming.setAdapter(adapterUpcomming);
            }
        }, error -> {
            loading1.setVisibility(View.GONE);
            loading3.setVisibility(View.GONE);
            Log.i("AppMovie", "onErrorResponse" + error.toString());
        });
        mRequestQueue.add(mStringRequest1);
    }
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable,2000);
    }

    private void initView() {
        viewPager2=findViewById(R.id.viewpagerSlider);
        recyclerViewNewMovies = findViewById(R.id.view1);
        recyclerViewNewMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        recyclerViewUpcomming = findViewById(R.id.viewCtg);
        recyclerViewUpcomming.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        recyclerViewCategory = findViewById(R.id.view2);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        loading1 = findViewById(R.id.progressBar1);
        loading2 = findViewById(R.id.progressBar2);
        loading3 = findViewById(R.id.progressBarCtg);
        txtSeach = findViewById(R.id.txtSeach);
    }

    private void clickSearch() {
        txtSeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), SearchMovie.class);
                startActivity(it);
            }
        });
    }
    private void onClickGoToDetail(Item item) {
        String slug = item.getSlug();
        Intent it = new Intent(getApplicationContext(), MovieDetail.class);
        Bundle bd = new Bundle();
        bd.putString("slug",slug);
        it.putExtra("myPackage",bd);
        startActivity(it);
    }
}