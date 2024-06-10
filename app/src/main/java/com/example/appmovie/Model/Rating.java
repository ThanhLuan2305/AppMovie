package com.example.appmovie.Model;

public class Rating {
    public String User_name;
    public String User_image;
    public int Rating;

    public Rating() {
    }

    public Rating(String User_name, String user_image, int rating) {
        this.User_name = User_name;
        this.User_image = user_image;
        this.Rating = rating;
    }
}
