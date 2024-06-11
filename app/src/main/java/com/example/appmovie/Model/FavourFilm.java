package com.example.appmovie.Model;

import androidx.annotation.Nullable;

import java.util.Date;
import java.util.Objects;

public class FavourFilm {
    public String movie_id;
    public String slug;
    public String origin_name;
    public String poster_url;
    public  String name;
    public Date create_at;

    public FavourFilm() {
    }

    public FavourFilm(String movie_id, String slug, String origin_name, String poster_url, String name, Date create_at) {
        this.movie_id = movie_id;
        this.slug = slug;
        this.origin_name = origin_name;
        this.poster_url = poster_url;
        this.name = name;
        this.create_at = create_at;
    }

    public FavourFilm(String movie_id, String slug, String origin_name, String poster_url, String name) {
        this.movie_id = movie_id;
        this.slug = slug;
        this.origin_name = origin_name;
        this.poster_url = poster_url;
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(movie_id, slug, origin_name, poster_url);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FavourFilm that = (FavourFilm) obj;
        return Objects.equals(movie_id, that.movie_id) && // Sử dụng equals() thay vì ==
                Objects.equals(slug, that.slug) &&
                Objects.equals(origin_name, that.origin_name) &&
                Objects.equals(poster_url, that.poster_url);
    }
}
