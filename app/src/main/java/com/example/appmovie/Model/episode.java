package com.example.appmovie.Model;

public class episode {
    public String filename;
    public String link_embed;
    public String link_m3u8;
    public String name;
    public String slug;

    public episode() {
    }

    public episode(String filename, String link_embed, String link_m3u8, String name, String slug) {
        this.filename = filename;
        this.link_embed = link_embed;
        this.link_m3u8 = link_m3u8;
        this.name = name;
        this.slug = slug;
    }
}
