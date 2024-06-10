package com.example.appmovie.Dto;

import com.example.appmovie.Model.Comment;
import com.example.appmovie.Model.Rating;

import java.io.Serializable;
import java.util.ArrayList;

public class RatingDto implements Serializable {
    public ArrayList<Rating> ratings;

    public RatingDto() {
        this.ratings = new ArrayList<>();
    }
}
