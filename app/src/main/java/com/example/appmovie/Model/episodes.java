package com.example.appmovie.Model;

import java.util.List;

public class episodes {
    public String server_name;
    public List<episode> server_data;

    public episodes() {
    }

    public episodes(String server_name, List<episode> server_data) {
        this.server_name = server_name;
        this.server_data = server_data;
    }
}
