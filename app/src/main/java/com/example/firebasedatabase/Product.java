package com.example.firebasedatabase;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Product {
    private String name;
    private String description;
    private String image;


    public Product() {}
    public Product(String name,  String description ,String image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public String getName() {
        return name;
    }



    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }





}
