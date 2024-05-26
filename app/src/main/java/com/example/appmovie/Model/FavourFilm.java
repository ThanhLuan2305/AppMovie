package com.example.appmovie.Model;

public class FavourFilm {
    public String movie_id;
    public String slug;
    public String origin_name;
    public String poster_url;

    public FavourFilm() {
    }

    public FavourFilm(String movie_id, String slug, String origin_name, String poster_url) {
        this.movie_id = movie_id;
        this.slug = slug;
        this.origin_name = origin_name;
        this.poster_url = poster_url;
    }
}
