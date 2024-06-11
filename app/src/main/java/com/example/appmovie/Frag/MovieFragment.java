package com.example.appmovie.Frag;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.appmovie.Model.Home.Category;
import com.example.appmovie.Model.Home.Item;
import com.example.appmovie.Model.Home.ItemClickListener;
import com.example.appmovie.Model.Home.ListFilm;
import com.example.appmovie.Model.IOnClickCtgMovie;
import com.example.appmovie.Model.SliderItems;
import com.example.appmovie.R;
import com.example.appmovie.View.CategoryActivity;
import com.example.appmovie.View.Adapter.CtgFilmRecyclerAdapter;
import com.example.appmovie.View.Adapter.FilmListAdapter;
import com.example.appmovie.View.Adapter.SliderAdapters;
import com.example.appmovie.View.MovieDetail;
import com.example.appmovie.View.SearchMovie;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieFragment extends Fragment {

    private CtgFilmRecyclerAdapter adapterCategory;
    private Spinner spnPage;
    private FilmListAdapter adapterNewMovies,adapterAllMovie;
    private RecyclerView recyclerViewNewMovies, recyclerViewAllMovie, recyclerViewCategory;
    private RequestQueue mRequestQueue1,mRequestQueue2;
    private StringRequest mStringRequest1, mStringRequest2;
    private ProgressBar loading1, loading2, loading3;
    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();
    TextView txtSeach;
    ListFilm lstFilm = new ListFilm();
    ListFilm lstAllFilm = new ListFilm();
    List<Item> lstItem = new ArrayList<>();
    ArrayList<String> lstPage = new ArrayList<>(); 
    List<Item> lstItemAll = new ArrayList<>();
    List<Category> lstCtg = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MovieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieFragment newInstance(String param1, String param2) {
        MovieFragment fragment = new MovieFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        initView(view);
        banners();
        sendReques();
        loadCateglory();
        eventClick();
        return view;
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
        adapterCategory= new CtgFilmRecyclerAdapter(lstCtg, new IOnClickCtgMovie() {
            @Override
            public void onClick(Category typeMovie) {
                onClickGoToCategory(typeMovie);
            }
        });
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
        mRequestQueue1 = Volley.newRequestQueue(getActivity());
        mStringRequest1 = new StringRequest(Request.Method.GET, "https://phimapi.com/danh-sach/phim-moi-cap-nhat?page=1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                loading1.setVisibility(View.GONE);
                ListFilm listFilm = gson.fromJson(response, ListFilm.class);
                lstFilm = listFilm;
                loadSpinner(listFilm);
                for (Item x : lstFilm.getItems()) {
                    lstItem.add(x);
                }
                adapterNewMovies= new FilmListAdapter(lstItem, new ItemClickListener() {
                    @Override
                    public void onClickItemFilm(Item item) {
                        onClickGoToDetail(item);
                    }
                });
                recyclerViewNewMovies.setAdapter(adapterNewMovies);
            }
        }, error -> {
            loading1.setVisibility(View.GONE);
            Log.i("AppMovie", "onErrorResponse" + error.toString());
        });
        mRequestQueue1.add(mStringRequest1);
    }
    private void sendRequesAllMovie(String url) {
        mRequestQueue1 = Volley.newRequestQueue(getActivity());
        lstItemAll.clear();
        mStringRequest1 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                loading3.setVisibility(View.GONE);
                ListFilm listFilm = gson.fromJson(response, ListFilm.class);
                lstAllFilm = listFilm;
                for (Item x : lstAllFilm.getItems()) {
                    lstItemAll.add(x);
                }
                adapterAllMovie= new FilmListAdapter(lstItemAll, new ItemClickListener() {
                    @Override
                    public void onClickItemFilm(Item item) {
                        onClickGoToDetail(item);
                    }
                });
                recyclerViewAllMovie.setAdapter(adapterAllMovie);
            }
        }, error -> {
            loading3.setVisibility(View.GONE);
            Log.i("AppMovie", "onErrorResponse" + error.toString());
        });
        mRequestQueue1.add(mStringRequest1);
    }
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable,2000);
    }
    
    private void initView(View view) {
        viewPager2=view.findViewById(R.id.viewpagerSlider);
        recyclerViewNewMovies = view.findViewById(R.id.view1);
        recyclerViewNewMovies.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        recyclerViewAllMovie = view.findViewById(R.id.viewCtg);
        recyclerViewAllMovie.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL,false));
        recyclerViewCategory = view.findViewById(R.id.view2);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        loading1 = view.findViewById(R.id.progressBar1);
        loading2 = view.findViewById(R.id.progressBar2);
        loading3 = view.findViewById(R.id.progressBarCtg);
        txtSeach = view.findViewById(R.id.txtSeach);
        spnPage = view.findViewById(R.id.spinnerPageCtg);
    }

    private void eventClick() {
        txtSeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), SearchMovie.class);
                startActivity(it);
            }
        });
        spnPage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String page = lstPage.get(position);
                String[] splitPage = page.split(" ");
                String numPage = splitPage[splitPage.length - 1];
                String url = "https://phimapi.com/danh-sach/phim-moi-cap-nhat?page=" + numPage;
                sendRequesAllMovie(url);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void loadSpinner(ListFilm lstFilm) {
        int page = lstFilm.getPagination().getTotalPages();
        for (int i = 1; i<=page;i++) {
            lstPage.add("Trang " + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), com.bumptech.glide.R.layout.support_simple_spinner_dropdown_item, lstPage);
        spnPage.setAdapter(adapter);
        
    }
    private void onClickGoToDetail(Item item) {
        String slug = item.getSlug();
        Intent it = new Intent(getActivity(), MovieDetail.class);
        Bundle bd = new Bundle();
        bd.putString("slug",slug);
        it.putExtra("myPackage",bd);
        startActivity(it);
    }
    private void onClickGoToCategory(Category ctg) {
        String id = ctg.getId();
        String name = ctg.getName();
        Intent it = new Intent(getActivity(), CategoryActivity.class);
        Bundle bd = new Bundle();
        bd.putString("id",id);
        it.putExtra("myPackage",bd);
        startActivity(it);
    }
}