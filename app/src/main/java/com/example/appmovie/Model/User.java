package com.example.appmovie.Model;

import java.util.ArrayList;

public class User {
    public String Name;
    public String Image;
    public String Email;
    public ArrayList<FavourFilm> Favour_film = new ArrayList<>();

    public User() {
    }

    public User(String name, String image, String email, ArrayList<FavourFilm> favour_film) {
        Name = name;
        Image = image;
        Email = email;
        Favour_film = favour_film;
    }
}
