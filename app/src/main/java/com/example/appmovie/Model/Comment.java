package com.example.appmovie.Model;

import java.time.LocalDate;

public class Comment {
    public String Id;
    public String Content;
    public String Created_date;
    public String User_id;
    public String User_name;
    public String User_image;
    public String Movie_id;

    public Comment() {
    }

    public Comment(String content, String created_date, String user_id, String user_name, String user_image, String movie_id) {
        Content = content;
        Created_date = created_date;
        User_id = user_id;
        User_name = user_name;
        User_image = user_image;
        Movie_id = movie_id;
    }

    public Comment(String id, String content, String created_date, String user_id, String user_name, String user_image, String Movie_id) {
        Id = id;
        Content = content;
        Created_date = created_date;
        User_id = user_id;
        User_name = user_name;
        User_image = user_image;
        this.Movie_id = Movie_id;
    }
}
