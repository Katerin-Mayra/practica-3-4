package com.example.practica34.ui.home;

public class ItemImage {
    private String url,name;
    ItemImage(String url, String name){
        this.url=url;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
